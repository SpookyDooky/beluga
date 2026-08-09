package com.beluga.scraping;

import com.beluga.execution.model.task.DataPointConfiguration;
import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.execution.model.task.extraction_configuration.ExtractionConfiguration;
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

    }

    @Test
    void shouldExtractHtml() {

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