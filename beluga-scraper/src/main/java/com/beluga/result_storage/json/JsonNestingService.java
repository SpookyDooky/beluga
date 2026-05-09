package com.x.scrape.result_storage.json;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JsonNestingService {
	
	public Map<String, Object> createJsonNesting(final Map<String, Object> unnestedObject) {
		final Set<String> nestedPrefixes = findNestedProperties(unnestedObject.keySet());
		final Map<String, Object> result = createNestedMap(nestedPrefixes);
		
		for (final String propertyName : unnestedObject.keySet()) {
			addProperty(result, propertyName, unnestedObject.get(propertyName));
		}
		
		return result;
	}
	
	private Set<String> findNestedProperties(final Set<String> propertyNames) {
		final Set<String> prefixes = new HashSet<>();
		
		for (final String propertyName : propertyNames) {
			if (propertyName.contains(".")) {
				prefixes.add(createNestedPropertyPrefix(propertyName));
			}
		}
		
		return prefixes;
	}
	
	private String createNestedPropertyPrefix(final String propertyName) {
		final String[] propertyNameParts = propertyName.split("\\.");
		
		final StringBuilder prefix = new StringBuilder();
		for (int i = 0; i < propertyNameParts.length - 1; i++) {
			prefix.append(propertyNameParts[i]);
			
			if (i + 1 != propertyNameParts.length - 1) {
				prefix.append(".");
			}
		}
		
		return prefix.toString();
	}
	
	private Map<String, Object> createNestedMap(final Set<String> nestedPrefixes) {
		final Map<String, Object> nestedMap = new HashMap<>();
		
		for (final String nestedPropertyPrefix : nestedPrefixes) {
			final List<String> nestedObjects = getNestedObjects(nestedPropertyPrefix);
			
			Map<String, Object> nestedObject = null;
			for (int i = 0; i < nestedObjects.size(); i++) {
				final String objectName = nestedObjects.get(i);
				
				// Some part is already defined
				if (nestedMap.containsKey(objectName)) {
					nestedObject = (Map<String, Object>) nestedMap.get(objectName);
					continue;
				} else if (nestedObject != null && nestedObject.containsKey(objectName)) {
					nestedObject = (Map<String, Object>) nestedObject.get(objectName);
					continue;
				}
				
				final Map<String, Object> newObject = new HashMap<>();
				if (nestedObject == null) {
					nestedMap.put(objectName, newObject);
					nestedObject = newObject;
				} else {
					nestedObject.put(objectName, newObject);
					nestedObject = newObject;
				}
			}
			
//			Map<String, Object> lastObject = null;
//			for (int i = 0; i < nestedObjects.size(); i++) {
//				final String objectName = nestedObjects.get(i);
//				final Map<String, Object> nestedObject = new HashMap<>();
//
//				if (i >= 1) {
//					lastObject.put(objectName, nestedObject);
//					lastObject = nestedObject;
//				} else {
//					nestedMap.put(objectName, nestedObject);
//					lastObject = nestedObject;
//				}
//			}
		}
		
		return nestedMap;
	}
	
	private List<String> getNestedObjects(final String nestedPrefix) {
		return Arrays.stream(nestedPrefix.split("\\."))
				.toList();
	}
	
	private void addProperty(final Map<String, Object> result,
	                         final String propertyName,
	                         final Object propertyValue) {
		if (propertyName.contains(".")) {
			final String[] propertyNameParts = propertyName.split("\\.");
			
			Map<String, Object> nestedObject = (Map<String, Object>) result.get(propertyNameParts[0]);
			for (int i = 1; i < propertyNameParts.length - 1; i++ ) {
				nestedObject = (Map<String, Object>) nestedObject.get(propertyNameParts[i]);
			}
			nestedObject.put(propertyNameParts[propertyNameParts.length - 1], propertyValue);
		} else {
			result.put(propertyName, propertyValue);
		}
	}
}
