package com.beluga.scraping;

import com.beluga.execution.model.task.DataPointConfiguration;
import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.execution.model.task.extraction_configuration.attribute.AttributeExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.html.HtmlExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.image.ImageExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.text.TextExtractionConfiguration;
import com.beluga.http.HttpService;
import com.beluga.scraping.model.ScrapingResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScrapingService {

    private final Logger logger = LogManager.getLogger();

    private final HttpService httpService;

    public ScrapingService(final HttpService httpService) {
        this.httpService = httpService;
    }

    public ScrapingResult scrape(final URL url,
                                 final ScrapingConfiguration scrapingProperties) {
        final Document document = httpService.retrievePageAsDocument(url)
                .orElseThrow(() -> new IllegalStateException("Failed to retrieve page " + url.toString() + "."));

        final List<Element> elements = selectElements(document, scrapingProperties.getItemSelector());
        logger.info("Found " + elements.size() + " in document");

        final List<Map<String, Object>> result = elements.stream()
                .map(element -> extractData(element, scrapingProperties.getDataPointConfigurations()))
                .toList();

        return new ScrapingResult(
                document.toString(),
                result
        );
    }

    private List<Element> selectElements(final Document document,
                                         final String elementSelector) {
        return document.body()
                .select(elementSelector);
    }

    private Map<String, Object> extractData(final Element element,
                                            final List<DataPointConfiguration> dataPointConfigurations) {
        final Map<String, Object> result = new HashMap<>();

        for (final DataPointConfiguration dataPointConfiguration : dataPointConfigurations) {
            final Map<String, Object> dataPointResult = extractData(element, dataPointConfiguration);

            if (dataPointResult != null) {
                result.putAll(dataPointResult);
            }
        }
        return result;
    }

    private Map<String, Object> extractData(final Element element,
                                            final DataPointConfiguration dataPointConfiguration) {
        final Elements selectedElements = element.select(dataPointConfiguration.getSelector());

        return switch (dataPointConfiguration.getExtractionConfiguration()) {
            case AttributeExtractionConfiguration extractionConfiguration -> extractData(selectedElements, extractionConfiguration);
            case DescriptionListExtractionConfiguration extractionConfiguration -> extractData(selectedElements, extractionConfiguration);
            case HtmlExtractionConfiguration extractionConfiguration -> extractData(selectedElements, extractionConfiguration);
            case ImageExtractionConfiguration extractionConfiguration -> extractData(selectedElements, extractionConfiguration);
            case TextExtractionConfiguration extractionConfiguration -> extractData(selectedElements, extractionConfiguration);
            default -> throw new IllegalArgumentException("Unsupported extraction configuration");
        };
    }

    private Map<String, Object> extractData(final Elements selectedElements,
                                            final AttributeExtractionConfiguration extractionConfiguration) {
        return Map.of(
                extractionConfiguration.getField(),
                selectedElements.attr(extractionConfiguration.getAttribute())
        );
    }

    private Map<String, Object> extractData(final Elements selectedElements,
                                            final HtmlExtractionConfiguration extractionConfiguration) {
        return Map.of(
                extractionConfiguration.getField(),
                selectedElements.html()
        );
    }

    private Map<String, Object> extractData(final Elements selectedElements,
                                            final DescriptionListExtractionConfiguration extractionConfiguration) {
        return Map.of();
    }

    private Map<String, Object> extractData(final Elements selectedElements,
                                            final ImageExtractionConfiguration extractionConfiguration) {
        return Map.of();
    }

    private Map<String, Object> extractData(final Elements selectedElements,
                                            final TextExtractionConfiguration extractionConfiguration) {
        return Map.of(
                extractionConfiguration.getField(),
                selectedElements.text()
        );
    }
}
