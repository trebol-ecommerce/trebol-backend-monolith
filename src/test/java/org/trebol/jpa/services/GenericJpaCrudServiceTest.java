package org.trebol.jpa.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.IJpaRepository;
import org.trebol.pojo.DataPagePojo;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenericJpaCrudServiceTest {
  @Mock IJpaRepository<GenericEntity> genericRepositoryMock;
  @Mock ITwoWayConverterJpaService<GenericPojo, GenericEntity> genericConverterMock;
  @Mock IDataTransportJpaService<GenericPojo, GenericEntity> genericDataTransportServiceMock;
  final GenericPojo newPojo = new GenericPojo(null, "test");
  final GenericEntity newEntity = new GenericEntity(null, "test");
  final GenericEntity persistedEntity = new GenericEntity(1L, "test");
  final GenericPojo persistedPojo = new GenericPojo(1L, "test");
  final List<GenericEntity> emptyEntityList = List.of();
  final List<GenericEntity> persistedEntityList = List.of(persistedEntity);
  final List<GenericPojo> persistedPojoList = List.of(persistedPojo);
  final PageRequest simplePageRequest = PageRequest.of(0, 10);

  @Test
  void sanity_checks() {
    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    assertNotNull(service);

    GenericCrudJpaService<GenericPojo, GenericEntity> service2 = this.instantiate_with_existing_entity();
    assertNotNull(service2);
  }

  @Test
  void creates_data()
      throws BadInputException, EntityExistsException {
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
  void reads_plural_data_without_items() {
    DataPagePojo<GenericPojo> expectedResult = new DataPagePojo<>(0, 10);

    when(genericRepositoryMock.findAll(simplePageRequest)).thenReturn(new PageImpl<>(emptyEntityList));

    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    DataPagePojo<GenericPojo> result = service.readMany(0, 10, null, null);

    assertNotNull(result);
    assertEquals(expectedResult, result);
    verify(genericRepositoryMock).findAll(simplePageRequest);
  }

  @Test
  void reads_plural_data_with_items() {
    Predicate filters = new BooleanBuilder();
    DataPagePojo<GenericPojo> expectedResult = new DataPagePojo<>(persistedPojoList, 0, 1, 10);

    when(genericRepositoryMock.count(filters)).thenReturn(1L);
    when(genericRepositoryMock.findAll(filters, simplePageRequest)).thenReturn(new PageImpl<>(persistedEntityList));
    when(genericConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);
    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();

    DataPagePojo<GenericPojo> result = service.readMany(0, 10, null, filters);

    assertEquals(expectedResult, result);
    verify(genericRepositoryMock).count(filters);
    verify(genericRepositoryMock).findAll(filters, simplePageRequest);
    verify(genericConverterMock).convertToPojo(persistedEntity);
  }


  @Test
  void reads_singular_data()
      throws EntityNotFoundException {
    Predicate filters = new BooleanBuilder();
    Optional<GenericEntity> result = Optional.of(persistedEntity);
    when(genericRepositoryMock.findOne(filters)).thenReturn(result);
    when(genericConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);
    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();

    GenericPojo foundPojo = service.readOne(filters);

    assertEquals(persistedPojo, foundPojo);
    verify(genericRepositoryMock).findOne(filters);
    verify(genericConverterMock).convertToPojo(persistedEntity);
  }

  @Test
  void updates_data()
      throws BadInputException, EntityNotFoundException {
    GenericPojo updatingPojo = new GenericPojo(1L, "test2");
    GenericEntity updatedEntity = new GenericEntity(1L, "test2");
    when(genericDataTransportServiceMock.applyChangesToExistingEntity(updatingPojo, persistedEntity)).thenReturn(updatedEntity);
    when(genericRepositoryMock.saveAndFlush(updatedEntity)).thenReturn(updatedEntity);
    when(genericConverterMock.convertToPojo(updatedEntity)).thenReturn(updatingPojo);
    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();

    GenericPojo result = service.update(updatingPojo);

    assertNotNull(result);
    assertEquals(updatingPojo, result);
    verify(genericDataTransportServiceMock).applyChangesToExistingEntity(updatingPojo, persistedEntity);
    verify(genericRepositoryMock).saveAndFlush(updatedEntity);
    verify(genericConverterMock).convertToPojo(updatedEntity);
  }

  @Test
  void updates_data_using_filters()
      throws BadInputException, EntityNotFoundException {
    Predicate filters = new BooleanBuilder();
    GenericPojo updatingPojo = new GenericPojo(1L, "test2");
    GenericEntity updatedEntity = new GenericEntity(1L, "test2");
    when(genericRepositoryMock.findOne(filters)).thenReturn(Optional.of(persistedEntity));
    when(genericDataTransportServiceMock.applyChangesToExistingEntity(updatingPojo, persistedEntity)).thenReturn(updatedEntity);
    when(genericRepositoryMock.saveAndFlush(updatedEntity)).thenReturn(updatedEntity);
    when(genericConverterMock.convertToPojo(updatedEntity)).thenReturn(updatingPojo);

    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();
    GenericPojo result = service.update(updatingPojo, filters);

    assertEquals(result, updatingPojo);
    verify(genericRepositoryMock).findOne(filters);
    verify(genericDataTransportServiceMock).applyChangesToExistingEntity(updatingPojo, persistedEntity);
    verify(genericRepositoryMock).saveAndFlush(updatedEntity);
    verify(genericConverterMock).convertToPojo(updatedEntity);
  }

  @Test
  void deletes_data()
      throws EntityNotFoundException, BadInputException {
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
  void errors_when_reads_singular_data_but_is_unable_to_find_it() {
    Predicate filters = new BooleanBuilder();
    Optional<GenericEntity> emptyResult = Optional.empty();
    when(genericRepositoryMock.findOne(filters)).thenReturn(emptyResult);

    GenericCrudJpaService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    GenericPojo genericPojo = null;

    try {
      genericPojo = service.readOne(filters);
    } catch (EntityNotFoundException ex) {
      verify(genericRepositoryMock).findOne(filters);
    }

    assertNull(genericPojo);
  }

  private GenericCrudJpaService<GenericPojo, GenericEntity> instantiate_without_existing_entity() {
    return new GenericCrudJpaService<>(
        genericRepositoryMock,
        genericConverterMock,
        genericDataTransportServiceMock) {
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
        genericDataTransportServiceMock) {
      @Override
      public Optional<GenericEntity> getExisting(GenericPojo example) {
        return Optional.of(persistedEntity);
      }
    };
  }

}
