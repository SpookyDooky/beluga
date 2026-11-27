package com.beluga.scraping;

import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.attribute.AttributeExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.description_list.DescriptionListExtractionDataPointConfiguration;
import com.beluga.execution.model.task.extraction_configuration.html.HtmlExtractionConfiguration;
import com.beluga.execution.model.task.extraction_configuration.text.TextExtractionConfiguration;
import com.beluga.http.model.HttpResult;
import com.beluga.http.service.HttpService;
import com.beluga.scraping.model.ScrapingResult;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScrapingServiceTest {

    private static final String PAGE = "<html><body><div class=\"main_element\"><p class=\"some_text\">text</p></div></body></html>";

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
        final HttpResult httpResult = HttpResult.of(200, toUri(URL), "text/html; charset=UTF-8", "UTF-8", readFile(responseFile));

        when(httpService.get(URL)).thenReturn(Optional.of(httpResult));
    }

    byte[] readFile(final String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    URI toUri(final URL url) {
        try {
            return url.toURI();
        } catch (final URISyntaxException e) {
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

    @Test
    void shouldThrowWhenPageCouldNotBeFetched() {
        when(httpService.get(URL)).thenReturn(Optional.empty());

        final ScrapingConfiguration scrapingConfiguration = Instancio.create(ScrapingConfiguration.class);

        assertThrows(IllegalStateException.class, () -> scrapingService.scrape(URL, scrapingConfiguration));
    }

    @Test
    void shouldThrowWhenContentTypeIsNotSupported() {
        final HttpResult httpResult = HttpResult.of(200, toUri(URL), "application/json", null, "{}".getBytes(StandardCharsets.UTF_8));
        when(httpService.get(URL)).thenReturn(Optional.of(httpResult));

        final ScrapingConfiguration scrapingConfiguration = Instancio.create(ScrapingConfiguration.class);

        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> scrapingService.scrape(URL, scrapingConfiguration));
        assertTrue(exception.getMessage().contains("application/json"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "text/html",
            "TEXT/HTML; charset=UTF-8",
            "text/plain",
            "application/xhtml+xml",
            "application/xml"
    })
    void shouldScrapeSupportedContentType(final String contentType) {
        final HttpResult httpResult = HttpResult.of(200, toUri(URL), contentType, null, PAGE.getBytes(StandardCharsets.UTF_8));
        when(httpService.get(URL)).thenReturn(Optional.of(httpResult));

        final TextExtractionConfiguration extractionConfiguration = Instancio.of(TextExtractionConfiguration.class)
                .set(field(TextExtractionConfiguration::getSelector), "p.some_text")
                .set(field(TextExtractionConfiguration::getField), "text")
                .create();

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, createScrapingConfiguration(extractionConfiguration));

        assertEquals("text", scrapingResult.getResult().getFirst().get("text"));
    }

    @Test
    void shouldScrapeWhenContentTypeIsMissing() {
        final HttpResult httpResult = HttpResult.of(200, toUri(URL), null, null, PAGE.getBytes(StandardCharsets.UTF_8));
        when(httpService.get(URL)).thenReturn(Optional.of(httpResult));

        final TextExtractionConfiguration extractionConfiguration = Instancio.of(TextExtractionConfiguration.class)
                .set(field(TextExtractionConfiguration::getSelector), "p.some_text")
                .set(field(TextExtractionConfiguration::getField), "text")
                .create();

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, createScrapingConfiguration(extractionConfiguration));

        assertEquals("text", scrapingResult.getResult().getFirst().get("text"));
    }

    @Test
    void shouldUseCharsetOfHttpResult() {
        final String page = "<html><body><div class=\"main_element\"><p class=\"some_text\">café</p></div></body></html>";
        final HttpResult httpResult = HttpResult.of(200, toUri(URL), "text/html; charset=ISO-8859-1", "ISO-8859-1", page.getBytes(StandardCharsets.ISO_8859_1));
        when(httpService.get(URL)).thenReturn(Optional.of(httpResult));

        final TextExtractionConfiguration extractionConfiguration = Instancio.of(TextExtractionConfiguration.class)
                .set(field(TextExtractionConfiguration::getSelector), "p.some_text")
                .set(field(TextExtractionConfiguration::getField), "text")
                .create();

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, createScrapingConfiguration(extractionConfiguration));

        assertEquals("café", scrapingResult.getResult().getFirst().get("text"));
    }

    @Test
    void shouldDetectCharsetFromPageWhenHttpResultHasNoCharset() {
        final String page = "<html><head><meta charset=\"ISO-8859-1\"></head>"
                + "<body><div class=\"main_element\"><p class=\"some_text\">café</p></div></body></html>";
        final HttpResult httpResult = HttpResult.of(200, toUri(URL), "text/html", null, page.getBytes(StandardCharsets.ISO_8859_1));
        when(httpService.get(URL)).thenReturn(Optional.of(httpResult));

        final TextExtractionConfiguration extractionConfiguration = Instancio.of(TextExtractionConfiguration.class)
                .set(field(TextExtractionConfiguration::getSelector), "p.some_text")
                .set(field(TextExtractionConfiguration::getField), "text")
                .create();

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, createScrapingConfiguration(extractionConfiguration));

        assertEquals("café", scrapingResult.getResult().getFirst().get("text"));
    }

    @Test
    void shouldResolveRelativeUrlsAgainstFinalUri() {
        final String page = "<html><body><div class=\"main_element\"><a class=\"link\" href=\"image.png\">link</a></div></body></html>";
        final HttpResult httpResult = HttpResult.of(200, URI.create("https://example.com/final/page"), "text/html", null, page.getBytes(StandardCharsets.UTF_8));
        when(httpService.get(URL)).thenReturn(Optional.of(httpResult));

        final AttributeExtractionConfiguration extractionConfiguration = Instancio.of(AttributeExtractionConfiguration.class)
                .set(field(AttributeExtractionConfiguration::getSelector), "a.link")
                .set(field(AttributeExtractionConfiguration::getField), "link")
                .set(field(AttributeExtractionConfiguration::getAttribute), "abs:href")
                .create();

        final ScrapingResult scrapingResult = scrapingService.scrape(URL, createScrapingConfiguration(extractionConfiguration));

        assertEquals("https://example.com/final/image.png", scrapingResult.getResult().getFirst().get("link"));
    }
}
