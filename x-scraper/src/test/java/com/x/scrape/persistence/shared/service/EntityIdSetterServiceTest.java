package com.x.scrape.persistence.shared.service;

import com.x.scrape.logging.ContextLogger;
import com.x.scrape.persistence.shared.model.HasId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntityIdSetterServiceTest {
	
	@Mock
	private ContextLogger logger;
	@Mock
	private PersistenceIdService persistenceIdService;
	
	@InjectMocks
	private EntityIdSetterService entityIdSetterService;
	
	@Test
	void shouldSetIdsAtRoot() {
		final SimpleEntity entity = new SimpleEntity();
		when(persistenceIdService.getNext()).thenReturn(0L);
		
		entityIdSetterService.setIds(entity);
		
		assertEquals(0L, entity.getId());
	}
	
	@Test
	void shouldSetNestedIds() {
		final NestedEntity entity = new NestedEntity();
		when(persistenceIdService.getNext()).thenReturn(0L, 1L);
		
		entityIdSetterService.setIds(entity);
		
		assertEquals(0L, entity.getId());
		assertEquals(1L, entity.getSimpleEntity().getId());
	}
	
	@Test
	void shouldSetIdsInHasIdIterable() {
		final IterableEntity entity = new IterableEntity();
		when(persistenceIdService.getNext()).thenReturn(0L, 1L, 2L);
		
		entityIdSetterService.setIds(entity);
		
		assertEquals(0L, entity.getId());
		assertEquals(1L, entity.getList().getFirst().getId());
		assertEquals(2L, entity.getList().getFirst().getSimpleEntity().getId());
	}
	
	static class SimpleEntity implements HasId {
		
		private Long id;
		
		@Override
		public Long getId() {
			return id;
		}
		
		@Override
		public void setId(final Long id) {
			this.id = id;
		}
	}
	
	static class NestedEntity implements HasId {
		
		private Long id;
		private SimpleEntity simpleEntity = new SimpleEntity();
		
		@Override
		public Long getId() {
			return id;
		}
		
		@Override
		public void setId(final Long id) {
			this.id = id;
		}
		
		public SimpleEntity getSimpleEntity() {
			return simpleEntity;
		}
		
		public void setSimpleEntity(final SimpleEntity simpleEntity) {
			this.simpleEntity = simpleEntity;
		}
	}
	
	static class IterableEntity implements HasId {
		
		private Long id;
		private List<NestedEntity> list = List.of(new NestedEntity());
		
		@Override
		public Long getId() {
			return id;
		}
		
		@Override
		public void setId(final Long id) {
			this.id = id;
		}
		
		public List<NestedEntity> getList() {
			return list;
		}
		
		public void setList(final List<NestedEntity> list) {
			this.list = list;
		}
	}
}