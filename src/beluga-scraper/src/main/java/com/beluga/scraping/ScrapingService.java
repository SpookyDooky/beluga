package com.beluga.scraping;

import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.attribute.AttributeExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionDataPointConfiguration;
import com.beluga.execution.model.task.extraction_configuration.html.HtmlExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.image.ImageExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.text.TextExtractionConfiguration;
import com.beluga.http.model.HttpResult;
import com.beluga.http.service.HttpService;
import com.beluga.scraping.model.ScrapingResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ScrapingService {

    private static final Pattern XML_CONTENT_TYPE = Pattern.compile("(application|text)/\\w*\\+?xml.*", Pattern.CASE_INSENSITIVE);

    private final Logger logger = LogManager.getLogger();

    private final HttpService httpService;

    public ScrapingService(final HttpService httpService) {
        this.httpService = httpService;
    }

    public ScrapingResult scrape(final URL url,
                                 final ScrapingConfiguration scrapingProperties) {
        final HttpResult httpResult = httpService.get(url)
                .orElseThrow(() -> new IllegalStateException("Failed to retrieve page " + url.toString() + "."));

        validateIsScrapable(url, httpResult);
        final Document document = parse(httpResult);

        final List<Element> elements = selectElements(document, scrapingProperties.getItemSelector());
        logger.info("Found " + elements.size() + " in document");

        final List<Map<String, Object>> result = elements.stream()
                .map(element -> extractData(element, scrapingProperties.getExtractionConfigurations()))
                .toList();

        return new ScrapingResult(
                document.toString(),
                result
        );
    }

    /**
     * The accepted content types are the same as jsoup accepts when it fetches a page itself: text, XML, or no content type at all.
     *
     * @param url        the url of the page.
     * @param httpResult the response for the page.
     * @throws IllegalStateException when the content type is not supported.
     */
    private void validateIsScrapable(final URL url,
                                     final HttpResult httpResult) {
        final String contentType = httpResult.getContentType();
        if (contentType != null && !isParsableContentType(contentType)) {
            throw new IllegalStateException("Failed to retrieve page " + url + ": unsupported content type " + contentType + ".");
        }
    }

    private boolean isParsableContentType(final String contentType) {
        return contentType.toLowerCase(Locale.ROOT).startsWith("text/")
                || XML_CONTENT_TYPE.matcher(contentType).matches();
    }

    private Document parse(final HttpResult httpResult) {
        try {
            return Jsoup.parse(
                    new ByteArrayInputStream(httpResult.getBody()),
                    httpResult.getCharset(),
                    httpResult.getUri().toString()
            );
        } catch (final IOException e) {
            throw new IllegalStateException("Failed to parse page " + httpResult.getUri() + ".", e);
        }
    }

    private List<Element> selectElements(final Document document,
                                         final String elementSelector) {
        return document.body()
                .select(elementSelector);
    }

    private Map<String, Object> extractData(final Element element,
                                            final List<ExtractionConfiguration> dataPointConfigurations) {
        final Map<String, Object> result = new HashMap<>();

        for (final ExtractionConfiguration dataPointConfiguration : dataPointConfigurations) {
            final Map<String, Object> dataPointResult = extractData(element, dataPointConfiguration);

            if (dataPointResult != null) {
                result.putAll(dataPointResult);
            }
        }
        return result;
    }

    private Map<String, Object> extractData(final Element element,
                                            final ExtractionConfiguration extractionConfiguration) {
        return switch (extractionConfiguration) {
            case AttributeExtractionConfiguration attributeExtractionConfiguration ->
                    extractData(element, attributeExtractionConfiguration);
            case DescriptionListExtractionConfiguration descriptionListExtractionConfiguration ->
                    extractData(element, descriptionListExtractionConfiguration);
            case HtmlExtractionConfiguration htmlExtractionConfiguration ->
                    extractData(element, htmlExtractionConfiguration);
            case ImageExtractionConfiguration imageExtractionConfiguration ->
                    extractData(element, imageExtractionConfiguration);
            case TextExtractionConfiguration textExtractionConfiguration ->
                    extractData(element, textExtractionConfiguration);
            default -> throw new IllegalArgumentException("Unsupported extraction configuration");
        };
    }

    private Map<String, Object> extractData(final Element element,
                                            final AttributeExtractionConfiguration extractionConfiguration) {
        final Elements selectedElements = element.select(extractionConfiguration.getSelector());

        return Map.of(
                extractionConfiguration.getField(),
                selectedElements.attr(extractionConfiguration.getAttribute())
        );
    }

    /**
     * Extracts the selected element as raw HTML.
     *
     * @param element                 the elements to extract the data from.
     * @param extractionConfiguration the configuration containing information about the values to extract data for.
     * @return the raw HTML stored under its configured field.
     */
    private Map<String, Object> extractData(final Element element,
                                            final HtmlExtractionConfiguration extractionConfiguration) {
        final Elements selectedElements = element.select(extractionConfiguration.getSelector());
        return Map.of(
                extractionConfiguration.getField(),
                selectedElements.html()
        );
    }

    /**
     * Extracts data from a description list for matching dt values.
     *
     * @param element                 the elements to extract the data from.
     * @param extractionConfiguration the configuration containing information about the values to extract data for.
     * @return the extracted data from the description list.
     */
    private Map<String, Object> extractData(final Element element,
                                            final DescriptionListExtractionConfiguration extractionConfiguration) {
        final Elements selectedElements = element.select(extractionConfiguration.getSelector());

        if (selectedElements.size() > 1) {
            throw new IllegalArgumentException("Found more than one description list matching css selector.");
        } else if (selectedElements.isEmpty()) {
            return null;
        }

        final List<Element> descriptionListElements = selectedElements.getFirst().children();
        final Map<String, Object> result = new HashMap<>();

        for (final DescriptionListExtractionDataPointConfiguration dataPointConfiguration : extractionConfiguration.getDataPoints()) {
            final Object data = extractDescriptionListData(descriptionListElements, dataPointConfiguration.getDtValue());
            result.put(dataPointConfiguration.getField(), data);
        }

        return result;
    }

    /**
     * Extracts the data from a description list for a matching dt value.
     *
     * @param descriptionListElements list of elements in the description list.
     * @param dtValue                 the value of the dt tag to match.
     * @return The extracted data from the dd element.
     */
    private Object extractDescriptionListData(final List<Element> descriptionListElements,
                                              final String dtValue) {
        boolean matched = false;

        for (final Element descriptionListElement : descriptionListElements) {
            if (descriptionListElement.tag().getName().equals("dt") && dtValue.equals(descriptionListElement.text())) {
                matched = true;
                continue;
            }

            if (matched && descriptionListElement.tag().getName().equals("dd")) {
                return descriptionListElement.text();
            }
        }

        return null;
    }

    /**
     * Not implemented yet, this is scheduled to be implemented in v1.1
     */
    private Map<String, Object> extractData(final Element element,
                                            final ImageExtractionConfiguration extractionConfiguration) {
        return Map.of();
    }

    /**
     * Extracts text data from {@link Elements}
     *
     * @param element                 the elements to extract the data from.
     * @param extractionConfiguration the configuration of how to extract the text.
     * @return extracted text stored under its respective field.
     */
    private Map<String, Object> extractData(final Element element,
                                            final TextExtractionConfiguration extractionConfiguration) {
        final Elements selectedElements = element.select(extractionConfiguration.getSelector());

        return Map.of(
                extractionConfiguration.getField(),
                selectedElements.text()
        );
    }
}
