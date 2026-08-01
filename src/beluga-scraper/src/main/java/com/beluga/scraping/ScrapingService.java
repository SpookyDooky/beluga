package com.beluga.scraping;

import com.beluga.execution.model.task.DataPointConfiguration;
import com.beluga.execution.model.task.ScrapingConfiguration;
import com.beluga.http.HttpService;
import com.beluga.scraping.model.ScrapingResult;
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

import static com.beluga.model.job_definition.configuration.scraping_configuration.DataPointType.HTML;

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
		
		final List<Map<String, Object>> result =  elements.stream()
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
		return dataPointConfigurations.stream()
				.collect(Collectors.toMap(
						DataPointConfiguration::getField,
						dataPointConfiguration -> extractData(element, dataPointConfiguration)
				));
	}
	
	private Object extractData(final Element element,
	                           final DataPointConfiguration dataPointDefinition) {
		final Elements selectedElement = element.select(dataPointDefinition.getSelector());

		if (dataPointDefinition.getAttribute() != null) {
			return selectedElement.attr(dataPointDefinition.getAttribute());
		}

		if (dataPointDefinition.getType() == HTML) {
			return selectedElement.html();
		}
		
		return selectedElement.text();
	}
}
