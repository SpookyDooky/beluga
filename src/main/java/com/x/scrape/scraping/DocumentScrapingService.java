package com.x.scrape.scraping;

import com.x.scrape.properties.scraping.DataPointProperties;
import com.x.scrape.properties.scraping.ScrapingProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DocumentScrapingService {
	
	private final Logger logger = LogManager.getLogger();
	
	public List<Map<String, Object>> scrapeDocument(final Document document,
	                                                final ScrapingProperties scrapingProperties) {
		final List<Element> elements = selectElements(document, scrapingProperties.getElementSelector());
		logger.info("Found " + elements.size() + " in document");
		
		return elements.stream()
				.map(element -> extractData(element, scrapingProperties.getDataPoints()))
				.toList();
	}
	
	private List<Element> selectElements(final Document document,
	                                     final String elementSelector) {
		return document.body()
				.select(elementSelector);
	}
	
	private Map<String, Object> extractData(final Element element,
	                                        final List<DataPointProperties> dataPointPropertiesList) {
		return dataPointPropertiesList.stream()
				.collect(Collectors.toMap(
						DataPointProperties::getPropertyName,
						dataPointProperties -> extractData(element, dataPointProperties)
				));
	}
	
	private Object extractData(final Element element,
	                           final DataPointProperties dataPointProperties) {
		final Elements selectedElement = element.select(dataPointProperties.getSelector());

		return switch(dataPointProperties.getValueSelector()) {
			case TEXT -> selectedElement.text();
			case HREF -> selectedElement.attr("href");
		};
	}
}
