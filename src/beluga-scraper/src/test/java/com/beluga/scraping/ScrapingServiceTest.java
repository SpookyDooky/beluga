package com.beluga.scraping;

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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

        final AttributeExtractionConfiguration extractionConfiguration = Instancio.of(AttributeExtractionConfiguration.class)
                .set(field(AttributeExtractionConfiguration::getSelector), "a.some_attribute")
                .set(field(AttributeExtractionConfiguration::getField), "attribute")
                .set(field(AttributeExtractionConfiguration::getAttribute), "href")
                .create();

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration(extractionConfiguration);

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

    ScrapingConfiguration createScrapingConfiguration(final ExtractionConfiguration extractionConfiguration) {
        return Instancio.of(ScrapingConfiguration.class)
                .set(
                        field(ScrapingConfiguration::getExtractionConfigurations),
                        List.of(extractionConfiguration)
                ).set(field(ScrapingConfiguration::getItemSelector), "div.main_element")
                .create();
    }

    @Test
    void shouldExtractDescriptionList() {
        mockDocumentResponse("src/test/resources/test-files/html/description_list_extraction.html");

        final DescriptionListExtractionConfiguration extractionConfiguration = Instancio.of(DescriptionListExtractionConfiguration.class)
                .set(field(DescriptionListExtractionConfiguration::getSelector), "dl.description_list")
                .set(field(DescriptionListExtractionConfiguration::getDataPoints),
                        List.of(
                                Instancio.of(DescriptionListExtractionDataPointConfiguration.class)
                                        .set(field(DescriptionListExtractionDataPointConfiguration::getDtValue), "title1")
                                        .set(field(DescriptionListExtractionDataPointConfiguration::getField), "value1")
                                        .create(),
                                Instancio.of(DescriptionListExtractionDataPointConfiguration.class)
                                        .set(field(DescriptionListExtractionDataPointConfiguration::getDtValue), "title3")
                                        .set(field(DescriptionListExtractionDataPointConfiguration::getField), "value3")
                                        .create()
                        ))
                .create();

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration(extractionConfiguration);

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, scrapingConfiguration);

        assertEquals("data1", scrapingResult.getResult().getFirst().get("value1"));
        assertEquals("data3", scrapingResult.getResult().getFirst().get("value3"));
    }

    @Test
    void shouldNotAddFieldsWhenDescriptionListIsNotFound() {
        mockDocumentResponse("src/test/resources/test-files/html/description_list_extraction.html");

        final DescriptionListExtractionConfiguration extractionConfiguration = Instancio.of(DescriptionListExtractionConfiguration.class)
                .set(field(DescriptionListExtractionConfiguration::getSelector), "dl.does_not_exist")
                .set(field(DescriptionListExtractionConfiguration::getDataPoints), List.of(
                        Instancio.of(DescriptionListExtractionDataPointConfiguration.class)
                                .set(field(DescriptionListExtractionDataPointConfiguration::getDtValue), "title1")
                                .set(field(DescriptionListExtractionDataPointConfiguration::getField), "value1")
                                .create()
                ))
                .create();

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration(extractionConfiguration);

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, scrapingConfiguration);

        assertFalse(scrapingResult.getResult().getFirst().containsKey("value1"));
    }

    @Test
    void shouldSetFieldToNullWhenDtIsNotFound() {
        mockDocumentResponse("src/test/resources/test-files/html/description_list_extraction.html");

        final DescriptionListExtractionConfiguration extractionConfiguration = Instancio.of(DescriptionListExtractionConfiguration.class)
                .set(field(DescriptionListExtractionConfiguration::getSelector), "dl.description_list")
                .set(field(DescriptionListExtractionConfiguration::getDataPoints), List.of(
                        Instancio.of(DescriptionListExtractionDataPointConfiguration.class)
                                .set(field(DescriptionListExtractionDataPointConfiguration::getDtValue), "title1")
                                .set(field(DescriptionListExtractionDataPointConfiguration::getField), "found")
                                .create(),
                        Instancio.of(DescriptionListExtractionDataPointConfiguration.class)
                                .set(field(DescriptionListExtractionDataPointConfiguration::getDtValue), "does_not_exist")
                                .set(field(DescriptionListExtractionDataPointConfiguration::getField), "missing")
                                .create()
                ))
                .create();

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration(extractionConfiguration);

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, scrapingConfiguration);

        assertEquals("data1", scrapingResult.getResult().getFirst().get("found"));
        assertTrue(scrapingResult.getResult().getFirst().containsKey("missing"));
        assertNull(scrapingResult.getResult().getFirst().get("missing"));
    }

    @Test
    void shouldExtractHtml() {
        mockDocumentResponse("src/test/resources/test-files/html/html_extraction.html");

        final HtmlExtractionConfiguration extractionConfiguration = Instancio.of(HtmlExtractionConfiguration.class)
                .set(field(HtmlExtractionConfiguration::getSelector), "div.some_html")
                .set(field(HtmlExtractionConfiguration::getField), "html")
                .create();

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration(extractionConfiguration);

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, scrapingConfiguration);

        assertEquals("<p></p>", scrapingResult.getResult().getFirst().get("html"));
    }

    @Test
    void shouldExtractImage() {

    }

    @Test
    void shouldExtractText() {
        mockDocumentResponse("src/test/resources/test-files/html/text_extraction.html");

        final TextExtractionConfiguration extractionConfiguration = Instancio.of(TextExtractionConfiguration.class)
                .set(field(TextExtractionConfiguration::getSelector), "p.some_text")
                .set(field(TextExtractionConfiguration::getField), "text")
                .create();

        final ScrapingConfiguration scrapingConfiguration = createScrapingConfiguration(extractionConfiguration);

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, scrapingConfiguration);

        assertEquals("text", scrapingResult.getResult().getFirst().get("text"));
    }
}