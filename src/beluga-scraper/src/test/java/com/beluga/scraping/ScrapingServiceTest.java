package com.beluga.scraping;

import com.beluga.execution.model.task.DataPointConfiguration;
import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.attribute.AttributeExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionDataPointConfiguration;
import com.beluga.execution.model.task.extraction_configuration.html.HtmlExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.text.TextExtractionConfiguration;
import com.beluga.http.HttpService;
import com.beluga.scraping.model.ScrapingResult;
import org.instancio.Instancio;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScrapingServiceTest {

    private static final URL URL;

    static {
        try {
            URL = new URL("http://localhost:1234");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Mock
    private HttpService httpService;

    @InjectMocks
    private ScrapingService scrapingService;

    @Test
    void shouldExtractAttribute() {
        mockDocumentResponse("src/test/resources/test-files/html/attribute_extraction.html");

        final AttributeExtractionConfiguration extractionConfiguration = new AttributeExtractionConfiguration();
        extractionConfiguration.setField("attribute");
        extractionConfiguration.setAttribute("href");

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration("a.some_attribute", extractionConfiguration);

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, scrapingConfiguration);

        assertEquals("attribute", scrapingResult.getResult().getFirst().get("attribute"));
    }

    void mockDocumentResponse(final String responseFile) {
        final String fileContent = readFile(responseFile);
        final Element element = Jsoup.parse(fileContent);

        final Document document = mock();
        when(document.body()).thenReturn(element);

        when(httpService.retrievePageAsDocument(URL)).thenReturn(Optional.of(document));
    }

    String readFile(final String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    ScrapingConfiguration createScrapingConfiguration(final String selector,
                                                      final ExtractionConfiguration extractionConfiguration) {
        final DataPointConfiguration dataPointConfiguration = Instancio.of(DataPointConfiguration.class)
                .set(field(DataPointConfiguration::getSelector), selector)
                .set(field(DataPointConfiguration::getExtractionConfiguration), extractionConfiguration)
                .create();

        return Instancio.of(ScrapingConfiguration.class)
                .set(
                        field(ScrapingConfiguration::getDataPointConfigurations),
                        List.of(dataPointConfiguration)
                ).set(field(ScrapingConfiguration::getItemSelector), "div.main_element")
                .create();
    }

    @Test
    void shouldExtractDescriptionList() {
        mockDocumentResponse("src/test/resources/test-files/html/description_list_extraction.html");

        final DescriptionListExtractionConfiguration extractionConfiguration = new DescriptionListExtractionConfiguration();

        final DescriptionListExtractionDataPointConfiguration dataPointConfiguration1 = new DescriptionListExtractionDataPointConfiguration();
        dataPointConfiguration1.setDtValue("title1");
        dataPointConfiguration1.setField("value1");

        final DescriptionListExtractionDataPointConfiguration dataPointConfiguration2 = new DescriptionListExtractionDataPointConfiguration();
        dataPointConfiguration2.setDtValue("title3");
        dataPointConfiguration2.setField("value3");

        extractionConfiguration.setDataPoints(List.of(dataPointConfiguration1, dataPointConfiguration2));

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration("dl.description_list", extractionConfiguration);

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, scrapingConfiguration);

        assertEquals("data1", scrapingResult.getResult().getFirst().get("value1"));
        assertEquals("data3", scrapingResult.getResult().getFirst().get("value3"));
    }

    @Test
    void shouldExtractHtml() {
        mockDocumentResponse("src/test/resources/test-files/html/html_extraction.html");

        final HtmlExtractionConfiguration extractionConfiguration = new HtmlExtractionConfiguration();
        extractionConfiguration.setField("html");

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration("div.some_html", extractionConfiguration);

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, scrapingConfiguration);

        assertEquals("<p></p>", scrapingResult.getResult().getFirst().get("html"));
    }

    @Test
    void shouldExtractImage() {

    }

    @Test
    void shouldExtractText() {
        mockDocumentResponse("src/test/resources/test-files/html/text_extraction.html");

        final TextExtractionConfiguration extractionConfiguration = new TextExtractionConfiguration();
        extractionConfiguration.setField("text");

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration("p.some_text", extractionConfiguration);

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, scrapingConfiguration);

        assertEquals("text", scrapingResult.getResult().getFirst().get("text"));
    }
}