package com.x.scrape.persistence.shared.service;

import com.x.scrape.persistence.shared.model.HasId;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.x.scrape.util.ReflectionUtility.getFieldValue;
import static com.x.scrape.util.ReflectionUtility.isValueHolder;

/**
 * This service processes entities, and sets ids in case they are missing.
 */
public class EntityIdSetterService {
	
	private final PersistenceIdService persistenceIdService;
	
	public EntityIdSetterService(final PersistenceIdService persistenceIdService) {
		this.persistenceIdService = persistenceIdService;
	}
	
	/**
	 * Sets all missing ids recursively through the entire object.
	 * Modifies the passed object by reference. Returns the same object for convenience.
	 * <p>
	 * Skips nulls, primitives, and value-holder types.
	 *
	 * @param entity the entity for which ids need to be set.
	 * @param <T>    return type.
	 * @return entity with set ids.
	 */
	public <T> T setIds(final T entity) {
		setIdsRecursive(entity);
		return entity;
	}
	
	private void setIdsRecursive(final Object entity) {
		if (entity == null || entity.getClass().equals(Object.class) || isValueHolder(entity)) {
			return;
		} else if (entity instanceof HasId hasId && hasId.getId() == null) {
			hasId.setId(persistenceIdService.getNext());
		} else if (entity instanceof Iterable<?> iterable) {
			final Iterator<?> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				setIdsRecursive(iterator.next());
			}
		}
		
		final List<Field> fields = Arrays.stream(entity.getClass().getDeclaredFields()).toList();
		for (final Field field : fields) {
			setIdsRecursive(getFieldValue(entity, field));
		}
	}
}
