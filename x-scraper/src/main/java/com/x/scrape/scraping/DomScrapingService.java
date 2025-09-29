package com.x.scrape.scraping;

import com.x.scrape.http.HttpService;
import com.x.scrape.model.job.scraping_configuration.DataPointConfiguration;
import com.x.scrape.model.job.scraping_configuration.ScrapingConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DomScrapingService {
	
	private final Logger logger = LogManager.getLogger();
	
	private final HttpService httpService;
	
	public DomScrapingService(final HttpService httpService) {
		this.httpService = httpService;
	}
	
	public List<Map<String, Object>> scrape(final URL url,
	                                        final ScrapingConfiguration scrapingProperties) {
		final Document document = httpService.retrievePageAsDocument(url)
				.orElseThrow(() -> new IllegalStateException("Failed to retrieve page " + url.toString() + "."));
		
		final List<Element> elements = selectElements(document, scrapingProperties.getElementSelector());
		logger.info("Found " + elements.size() + " in document");
		
		return elements.stream()
				.map(element -> extractData(element, scrapingProperties.getDataPointConfigurations()))
				.toList();
	}
	
	private List<Element> selectElements(final Document document,
	                                     final String elementSelector) {
		return document.body()
				.select(elementSelector);
	}
	
	private Map<String, Object> extractData(final Element element,
	                                        final List<DataPointConfiguration> dataPointConfigurations) {
		return dataPointConfigurations.stream()
				.collect(Collectors.toMap(
						DataPointConfiguration::getPropertyName,
						dataPointConfiguration -> extractData(element, dataPointConfiguration)
				));
	}
	
	private Object extractData(final Element element,
	                           final DataPointConfiguration dataPointConfiguration) {
		final Elements selectedElement = element.select(dataPointConfiguration.getSelector());

		if (dataPointConfiguration.getAttribute() != null) {
			return selectedElement.attr(dataPointConfiguration.getAttribute());
		}
		
		return selectedElement.text();
	}
}
