package com.x.scrape.persistence.shared.service;

import com.x.scrape.logging.ContextLogger;
import com.x.scrape.model.task.TaskDefinition;
import com.x.scrape.model.task.TaskExecution;
import com.x.scrape.persistence.shared.model.HasId;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import static com.x.scrape.util.ReflectionUtility.getFieldValue;
import static com.x.scrape.util.ReflectionUtility.isValueHolder;

/**
 * This service processes entities, and sets ids in case they are missing.
 */
@Service
public class EntityIdSetterService {
	
	/**
	 * This is necessary to prevent the cache interfering with complex nested objects.
	 */
	private static final Set<Class<?>> CACHE_COMPATIBLE_CLASSES = Set.of(
			TaskDefinition.class,
			TaskExecution.class
	);
	
	private static final int MAX_CACHE_SIZE = 50_000;
	
	private final ContextLogger logger;
	private final PersistenceIdService persistenceIdService;
	
	/**
	 * These serve as an optimization to prevent iterating objects that have been completely initialized.
	 */
	private final Set<Integer> cache = new HashSet<>();
	private final ConcurrentLinkedDeque<Integer> cacheEvictionQueue = new ConcurrentLinkedDeque<>();
	
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
		if (isIncompatible(entity) || cache.contains(entity.hashCode()) || processed.contains(entity)) {
			return;
		}
		
		processed.add(entity);
		updateCache(entity);
		
		if (entity instanceof HasId hasId && hasId.getId() == null) {
			hasId.setId(persistenceIdService.getNext());
		} else if (entity instanceof Iterable<?> iterable) {
			logger.trace("Processing iterable");
			final Iterator<?> iterator = iterable.iterator();
			while (iterator.hasNext()) {
				setIdsRecursive(iterator.next(), processed);
			}
			return;
		}
		
		for (final Field field : entity.getClass().getDeclaredFields()) {
			logger.trace("Setting ids for field " + field.getName() + " for class " + field.getDeclaringClass().getSimpleName() + ".");
			setIdsRecursive(getFieldValue(entity, field), processed);
		}
	}
	
	private boolean isIncompatible(final Object entity) {
		return entity == null ||
				entity.getClass().equals(Object.class) ||
				isValueHolder(entity) ||
				Enum.class.isAssignableFrom(entity.getClass());
	}
	
	/**
	 * Updates the cache, adds the entity if it not already in the cache. Also evicts the item that has been
	 * in the cache the longest.
	 *
	 * @param entity entity to update the cache with.
	 */
	private void updateCache(final Object entity) {
		final Integer objectHash = entity.hashCode();
		
		if (cache.contains(objectHash) || !isCacheCompatible(entity)) {
			return;
		}
		
		if (cache.size() >= MAX_CACHE_SIZE) {
			final Integer hashToEvict = cacheEvictionQueue.removeFirst();
			cache.remove(hashToEvict);
		}
		
		cache.add(objectHash);
		cacheEvictionQueue.offer(objectHash);
	}
	
	private boolean isCacheCompatible(final Object entity) {
		return CACHE_COMPATIBLE_CLASSES.contains(entity.getClass());
	}
}
