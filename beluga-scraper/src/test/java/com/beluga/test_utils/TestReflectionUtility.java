package com.beluga.test_utils;

import org.opentest4j.AssertionFailedError;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This class is used to make it easier to test reflection related things on classes such as
 * testing if an annotation is present on a field/method/class. Besides that it also catches
 * all checked {@link Exception}'s related to reflection to make the developer experience nicer.
 */
public class TestReflectionUtility {
	
	/**
	 * Asserts if an annotation is present on a field for a class.
	 *
	 * @param fieldName       name of the field.
	 * @param clazz           the class the field is declared in.
	 * @param annotationClass annotation that should be on the field.
	 * @param <T>             annotation type.
	 * @return the {@link Annotation} found on the field if it is present.
	 * @throws AssertionFailedError if the annotation is not present on the field.
	 */
	public static <T extends Annotation> T assertAnnotationPresentOnField(final String fieldName,
	                                                                      final Class<?> clazz,
	                                                                      final Class<T> annotationClass) {
		final Field field = getField(fieldName, clazz);
		try {
			return field.getAnnotation(annotationClass);
		} catch (final NullPointerException e) {
			throw new AssertionFailedError("Annotation " + annotationClass.getSimpleName() + " not present on field " + fieldName + ".", e);
		}
	}
	
	/**
	 * Finds a {@link Field} with a name that is declared in a {@link Class}.
	 *
	 * @param fieldName name of the field.
	 * @param clazz     the class the field should be declared in.
	 * @return the field.
	 * @throws IllegalArgumentException if the field could not be found.
	 */
	public static Field getField(final String fieldName,
	                             final Class<?> clazz) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (final NoSuchFieldException e) {
			throw new IllegalArgumentException("Could not find field " + fieldName + " for class " + clazz.getSimpleName() + ".", e);
		}
	}
	
	/**
	 * Asserts that an annotation is present on a {@link Class}.
	 *
	 * @param clazz           the class the annotation should be present on.
	 * @param annotationClass the annotation to check for.
	 * @param <T>             type of {@link Annotation}
	 * @return the annotation.
	 * @throws AssertionFailedError if the annotation is not present on the class.
	 */
	public static <T extends Annotation> T assertAnnotationPresentOnClass(final Class<?> clazz,
	                                                                      final Class<T> annotationClass) {
		try {
			return clazz.getAnnotation(annotationClass);
		} catch (final NullPointerException e) {
			throw new AssertionFailedError("Annotation " + annotationClass.getSimpleName() + " not present on class " + clazz.getSimpleName() + ".", e);
		}
	}
	
	/**
	 * Asserts that an annotation is present on a {@link Method}.
	 *
	 * @param clazz            class the method is in.
	 * @param annotationClass  annotation class to look for.
	 * @param methodName       name of the method.
	 * @param methodParameters parameter types of the method
	 * @param <T>              return type.
	 * @return the annotation if it was found.
	 */
	public static <T extends Annotation> T assertAnnotationPresentOnMethod(final Class<?> clazz,
	                                                                       final Class<T> annotationClass,
	                                                                       final String methodName,
	                                                                       final Class<?>... methodParameters) {
		try {
			return getMethod(clazz, methodName, methodParameters).getAnnotation(annotationClass);
		} catch (final NullPointerException e) {
			throw new AssertionFailedError("Method " + methodName + " could not be found for class " + clazz.getSimpleName() + ".", e);
		}
	}
	
	/**
	 * Finds a method in class.
	 *
	 * @param clazz            the class to find the method in.
	 * @param methodName       the name of the method.
	 * @param methodParameters the parameters of the method.
	 * @return method if it is found.
	 * @throws IllegalArgumentException if method is not found.
	 */
	public static Method getMethod(final Class<?> clazz,
	                               final String methodName,
	                               final Class<?>... methodParameters) {
		try {
			return clazz.getMethod(methodName, methodParameters);
		} catch (final NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
