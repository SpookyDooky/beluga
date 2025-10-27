package com.x.scrape.persistence.shared.service;

import com.x.scrape.logging.ContextLogger;
import com.x.scrape.persistence.shared.model.HasId;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

import static com.x.scrape.util.ReflectionUtility.getFieldValue;
import static com.x.scrape.util.ReflectionUtility.isValueHolder;

/**
 * This service processes entities, and sets ids in case they are missing.
 */
@Service
public class EntityIdSetterService {
	
	private final ContextLogger logger;
	private final PersistenceIdService persistenceIdService;
	
	public EntityIdSetterService(final ContextLogger logger,
	                             @Lazy final PersistenceIdService persistenceIdService) {
		this.logger = logger;
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
	@SuppressWarnings("PMD.LinguisticNaming")
	public <T> T setIds(final T entity) {
		setIdsRecursive(entity, new HashSet<>());
		return entity;
	}
	
	private void setIdsRecursive(final Object entity, final Set<Object> processed) {
		processed.add(entity);
		
		if (entity == null || entity.getClass().equals(Object.class) || isValueHolder(entity) || Enum.class.isAssignableFrom(entity.getClass())) {
			return;
		} else if (entity instanceof HasId hasId && hasId.getId() == null) {
			hasId.setId(persistenceIdService.getNext());
		} else if (entity instanceof Iterable<?> iterable) {
			logger.trace("Processing iterable");
			final Iterator<?> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				setIdsRecursive(iterator.next(), processed);
			}
			return;
		}
		
		final List<Field> fields = Arrays.stream(entity.getClass().getDeclaredFields()).toList();
		for (final Field field : fields) {
			logger.trace("Setting ids for field " + field.getName() + " for class " + field.getDeclaringClass().getSimpleName() + ".");
			setIdsRecursive(getFieldValue(entity, field), processed);
		}
	}
}
