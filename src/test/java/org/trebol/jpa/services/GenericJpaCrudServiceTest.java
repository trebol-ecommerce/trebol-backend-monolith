package org.trebol.jpa.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import javassist.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.IJpaRepository;
import org.trebol.pojo.DataPagePojo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GenericJpaCrudServiceTest {

  @Mock
  IJpaRepository<GenericEntity> genericRepositoryMock;

  @Mock
  ITwoWayConverterJpaService<GenericPojo, GenericEntity> genericConverterMock;

  private final GenericPojo newPojo = new GenericPojo(null, "test");
  private final GenericEntity newEntity = new GenericEntity(null, "test");
  private final GenericEntity persistedEntity = new GenericEntity(1L, "test");
  private final GenericPojo persistedPojo = new GenericPojo(1L, "test");
  private final List<GenericEntity> emptyEntityList = List.of();
  private final List<GenericEntity> persistedEntityList = List.of(persistedEntity);
  private final List<GenericPojo> persistedPojoList = List.of(persistedPojo);
  private final PageRequest simplePageRequest = PageRequest.of(0, 10);

  @Test
  public void creates_data() throws BadInputException, EntityAlreadyExistsException {
    when(genericConverterMock.convertToNewEntity(newPojo)).thenReturn(newEntity);
    when(genericRepositoryMock.saveAndFlush(newEntity)).thenReturn(persistedEntity);
    when(genericConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);

    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    GenericPojo result = service.create(newPojo);

    assertNotNull(result);
    assertEquals(result, persistedPojo);
    verify(genericConverterMock).convertToNewEntity(newPojo);
    verify(genericRepositoryMock).saveAndFlush(newEntity);
    verify(genericConverterMock).convertToPojo(persistedEntity);
  }

  @Test
  public void reads_plural_data_without_items() {
    DataPagePojo<GenericPojo> expectedResult = new DataPagePojo<>(0, 10);

    when(genericRepositoryMock.findAll(simplePageRequest)).thenReturn(new PageImpl<>(emptyEntityList));

    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    DataPagePojo<GenericPojo> result = service.readMany(10, 0, null);

    assertNotNull(result);
    assertEquals(expectedResult, result);
    verify(genericRepositoryMock).findAll(simplePageRequest);
  }

  @Test
  public void reads_plural_data_with_items() {
    Predicate filters = new BooleanBuilder();
    DataPagePojo<GenericPojo> expectedResult = new DataPagePojo<>(persistedPojoList, 0, 1, 10);

    when(genericRepositoryMock.count(filters)).thenReturn(1L);
    when(genericRepositoryMock.findAll(simplePageRequest)).thenReturn(new PageImpl<>(persistedEntityList));
    when(genericConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);
    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();

    DataPagePojo<GenericPojo> result = service.readMany(10, 0, filters);

    assertEquals(expectedResult, result);
    verify(genericRepositoryMock).count(filters);
    verify(genericRepositoryMock).findAll(simplePageRequest);
    verify(genericConverterMock).convertToPojo(persistedEntity);
  }


  @Test
  public void reads_singular_data() throws NotFoundException {
    Predicate filters = new BooleanBuilder();
    Optional<GenericEntity> result = Optional.of(persistedEntity);
    when(genericRepositoryMock.findOne(filters)).thenReturn(result);
    when(genericConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);
    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();

    GenericPojo foundPojo = service.readOne(null);

    assertEquals(persistedPojo, foundPojo);
    verify(genericRepositoryMock).findOne(filters);
    verify(genericConverterMock).convertToPojo(persistedEntity);
  }

  @Test
  public void updates_data() throws BadInputException, NotFoundException {
    GenericPojo updatingPojo = new GenericPojo(1L, "test2");
    GenericEntity updatedEntity = new GenericEntity(1L, "test2");
    when(genericConverterMock.applyChangesToExistingEntity(updatingPojo, persistedEntity)).thenReturn(updatedEntity);
    when(genericRepositoryMock.saveAndFlush(updatedEntity)).thenReturn(updatedEntity);
    when(genericConverterMock.convertToPojo(updatedEntity)).thenReturn(updatingPojo);
    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();

    GenericPojo result = service.update(updatingPojo);

    assertNotNull(result);
    assertEquals(updatingPojo, result);
    verify(genericConverterMock).applyChangesToExistingEntity(updatingPojo, persistedEntity);
    verify(genericRepositoryMock).saveAndFlush(updatedEntity);
    verify(genericConverterMock).convertToPojo(updatedEntity);
  }

  @Test
  public void updates_data_using_filters() throws BadInputException, NotFoundException {
    Predicate filters = new BooleanBuilder();
    GenericPojo updatingPojo = new GenericPojo(1L, "test2");
    GenericEntity updatedEntity = new GenericEntity(1L, "test2");
    when(genericRepositoryMock.findOne(filters)).thenReturn(Optional.of(persistedEntity));
    when(genericConverterMock.applyChangesToExistingEntity(updatingPojo, persistedEntity)).thenReturn(updatedEntity);
    when(genericRepositoryMock.saveAndFlush(updatedEntity)).thenReturn(updatedEntity);
    when(genericConverterMock.convertToPojo(updatedEntity)).thenReturn(updatingPojo);

    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();
    GenericPojo result = service.update(updatingPojo, filters);

    assertEquals(result, updatingPojo);
    verify(genericRepositoryMock).findOne(filters);
    verify(genericConverterMock).applyChangesToExistingEntity(updatingPojo, persistedEntity);
    verify(genericRepositoryMock).saveAndFlush(updatedEntity);
    verify(genericConverterMock).convertToPojo(updatedEntity);
  }

  @Test
  public void deletes_data() throws NotFoundException {
    PageImpl<GenericEntity> persistedEntityPage = new PageImpl<>(persistedEntityList);
    Predicate filters = new BooleanBuilder();
    when(genericRepositoryMock.count(filters)).thenReturn(1L);
    when(genericRepositoryMock.findAll(filters)).thenReturn(persistedEntityPage);

    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();
    service.delete(filters);

    verify(genericRepositoryMock).findAll(filters);
    verify(genericRepositoryMock).deleteAll(persistedEntityPage);
  }

  @Test
  public void errors_when_reads_singular_data_but_is_unable_to_find_it() {
    Predicate filters = new BooleanBuilder();
    Optional<GenericEntity> emptyResult = Optional.empty();
    when(genericRepositoryMock.findOne(filters)).thenReturn(emptyResult);

    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    GenericPojo genericPojo = null;

    try {
      genericPojo = service.readOne(filters);
    } catch (NotFoundException ex) {
      assertNull(genericPojo);
      verify(genericRepositoryMock).findOne(filters);
    }
  }

  private GenericCrudJpaService<GenericPojo, GenericEntity> instantiate_without_existing_entity() {
    return new GenericCrudJpaService<>(
        genericRepositoryMock,
        genericConverterMock,
        LoggerFactory.getLogger(GenericCrudJpaService.class)) {
      @Override
      public Optional<GenericEntity> getExisting(GenericPojo example) {
        return Optional.empty();
      }
    };
  }

  private GenericCrudJpaService<GenericPojo, GenericEntity> instantiate_with_existing_entity() {
    return new GenericCrudJpaService<>(
        genericRepositoryMock,
        genericConverterMock,
        LoggerFactory.getLogger(GenericCrudJpaService.class)) {
      @Override
      public Optional<GenericEntity> getExisting(GenericPojo example) {
        return Optional.of(persistedEntity);
      }
    };
  }

}
