package com.x.scrape.util;

import java.lang.reflect.Field;
import java.net.URL;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class ReflectionUtility {
	
	private static final Set<Class<?>> VALUE_HOLDER_CLASSES = Set.of(
			Byte.class, Short.class, Integer.class, Long.class,
			byte.class, short.class, int.class, long.class,
			Double.class, Float.class, double.class, float.class,
			UUID.class, String.class, Instant.class, URL.class,
			boolean.class, Boolean.class
	);
	
	/**
	 * Checks if an object is a value holder such as a primitive or boxed primitive.
	 */
	public static boolean isValueHolder(final Object object) {
		return VALUE_HOLDER_CLASSES.contains(object.getClass());
	}
	
	/**
	 * Returns the field value of an object.
	 *
	 * @param object the object the field belongs to.
	 * @param field  the field to retrieve the value from.
	 * @param <T>    the expected field type.
	 * @return field value.
	 */
	public static <T> T getFieldValue(final Object object,
	                                  final Field field) {
		try {
			field.setAccessible(true);
			return (T) field.get(object);
		} catch (final IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
	}
}
