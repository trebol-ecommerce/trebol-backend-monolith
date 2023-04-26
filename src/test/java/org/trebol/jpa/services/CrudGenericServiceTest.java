/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.trebol.api.models.DataPagePojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.Repository;
import org.trebol.jpa.services.crud.CrudGenericService;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrudGenericServiceTest {
  static final String GENERIC_NAME = "test";
  @Mock Repository<GenericEntity> genericRepositoryMock;
  @Mock ConverterService<GenericPojo, GenericEntity> genericConverterMock;
  @Mock PatchService<GenericPojo, GenericEntity> genericPatchServiceMock;
  final GenericPojo newPojo = new GenericPojo(null, GENERIC_NAME);
  final GenericEntity newEntity = new GenericEntity(null, GENERIC_NAME);
  final GenericEntity persistedEntity = new GenericEntity(1L, GENERIC_NAME);
  final GenericPojo persistedPojo = new GenericPojo(1L, GENERIC_NAME);
  final List<GenericEntity> emptyEntityList = List.of();
  final List<GenericEntity> persistedEntityList = List.of(persistedEntity);
  final List<GenericPojo> persistedPojoList = List.of(persistedPojo);
  final PageRequest simplePageRequest = PageRequest.of(0, 10);

  @Test
  void sanity_checks() {
    CrudGenericService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    assertNotNull(service);

    CrudGenericService<GenericPojo, GenericEntity> service2 = this.instantiate_with_existing_entity();
    assertNotNull(service2);
  }

  @Test
  void creates_data()
    throws BadInputException, EntityExistsException {
    when(genericConverterMock.convertToNewEntity(newPojo)).thenReturn(newEntity);
    when(genericRepositoryMock.saveAndFlush(newEntity)).thenReturn(persistedEntity);
    when(genericConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);

    CrudGenericService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    GenericPojo result = service.create(newPojo);

    assertNotNull(result);
    assertEquals(persistedPojo, result);
    verify(genericConverterMock).convertToNewEntity(newPojo);
    verify(genericRepositoryMock).saveAndFlush(newEntity);
    verify(genericConverterMock).convertToPojo(persistedEntity);
  }

  @Test
  void reads_plural_data_without_items() {
    DataPagePojo<GenericPojo> expectedResult = new DataPagePojo<>();
    expectedResult.setPageSize(10);
    PageImpl<GenericEntity> emptyPage = new PageImpl<>(emptyEntityList);
    when(genericRepositoryMock.findAll(simplePageRequest)).thenReturn(emptyPage);

    CrudGenericService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    DataPagePojo<GenericPojo> result = service.readMany(0, 10, null, null);

    assertNotNull(result);
    assertEquals(expectedResult, result);
    verify(genericRepositoryMock).findAll(simplePageRequest);
  }

  @Test
  void reads_plural_data_with_items() {
    Predicate filters = new BooleanBuilder();
    DataPagePojo<GenericPojo> expectedResult = new DataPagePojo<>(persistedPojoList, 0, 1, 10);
    PageImpl<GenericEntity> singleItemPage = new PageImpl<>(persistedEntityList);

    when(genericRepositoryMock.count(filters)).thenReturn(1L);
    when(genericRepositoryMock.findAll(filters, simplePageRequest)).thenReturn(singleItemPage);
    when(genericConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);
    CrudGenericService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();

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
    CrudGenericService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();

    GenericPojo foundPojo = service.readOne(filters);

    assertEquals(persistedPojo, foundPojo);
    verify(genericRepositoryMock).findOne(filters);
    verify(genericConverterMock).convertToPojo(persistedEntity);
  }

  @Test
  void partially_updates_data()
    throws BadInputException, EntityNotFoundException {
    Long id = 1L;
    String name = "test2";
    Map<String, Object> changes = Map.of("name", name);
    GenericEntity updatedEntity = new GenericEntity(id, name);
    GenericPojo updatedPojo = new GenericPojo(id, name);
    when(genericPatchServiceMock.patchExistingEntity(anyMap(), persistedEntity)).thenReturn(updatedEntity);
    when(genericRepositoryMock.saveAndFlush(updatedEntity)).thenReturn(updatedEntity);
    when(genericConverterMock.convertToPojo(updatedEntity)).thenReturn(updatedPojo);
    CrudGenericService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();

    Optional<GenericPojo> result = service.partialUpdate(changes, id);

    assertTrue(result.isPresent());
    assertEquals(updatedPojo, result.get());
    verify(genericPatchServiceMock).patchExistingEntity(changes, persistedEntity);
    verify(genericRepositoryMock).saveAndFlush(updatedEntity);
    verify(genericConverterMock).convertToPojo(updatedEntity);
  }

  @Test
  void partially_updates_data_using_filters()
    throws BadInputException, EntityNotFoundException {
    Long id = 1L;
    String name = "test2";
    Predicate filters = new BooleanBuilder();
    Map<String, Object> changes = Map.of("name", name);
    GenericEntity updatedEntity = new GenericEntity(1L, name);
    GenericPojo updatedPojo = new GenericPojo(id, name);
    when(genericRepositoryMock.findOne(filters)).thenReturn(Optional.of(persistedEntity));
    when(genericPatchServiceMock.patchExistingEntity(anyMap(), persistedEntity)).thenReturn(updatedEntity);
    when(genericRepositoryMock.saveAndFlush(updatedEntity)).thenReturn(updatedEntity);
    when(genericConverterMock.convertToPojo(updatedEntity)).thenReturn(updatedPojo);

    CrudGenericService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();
    Optional<GenericPojo> result = service.partialUpdate(changes, filters);

    assertTrue(result.isPresent());
    assertEquals(updatedPojo, result.get());
    verify(genericRepositoryMock).findOne(filters);
    verify(genericPatchServiceMock).patchExistingEntity(changes, persistedEntity);
    verify(genericRepositoryMock).saveAndFlush(updatedEntity);
    verify(genericConverterMock).convertToPojo(updatedEntity);
  }

  @Test
  void deletes_data()
    throws EntityNotFoundException {
    PageImpl<GenericEntity> persistedEntityPage = new PageImpl<>(persistedEntityList);
    Predicate filters = new BooleanBuilder();
    when(genericRepositoryMock.count(filters)).thenReturn(1L);
    when(genericRepositoryMock.findAll(filters)).thenReturn(persistedEntityPage);

    CrudGenericService<GenericPojo, GenericEntity> service = this.instantiate_with_existing_entity();
    service.delete(filters);

    verify(genericRepositoryMock).findAll(filters);
    verify(genericRepositoryMock).deleteAll(persistedEntityPage);
  }

  @Test
  void errors_when_reads_singular_data_but_is_unable_to_find_it() {
    Predicate filters = new BooleanBuilder();
    Optional<GenericEntity> emptyResult = Optional.empty();
    when(genericRepositoryMock.findOne(filters)).thenReturn(emptyResult);

    CrudGenericService<GenericPojo, GenericEntity> service = this.instantiate_without_existing_entity();
    GenericPojo genericPojo = null;

    try {
      genericPojo = service.readOne(filters);
    } catch (EntityNotFoundException ex) {
      verify(genericRepositoryMock).findOne(filters);
    }

    assertNull(genericPojo);
  }

  private CrudGenericService<GenericPojo, GenericEntity> instantiate_without_existing_entity() {
    return new CrudGenericService<>(
      genericRepositoryMock,
      genericConverterMock,
      genericPatchServiceMock) {

      @Override
      public GenericPojo update(GenericPojo input) throws EntityNotFoundException, BadInputException {
        throw new UnsupportedOperationException("This method signature has been deprecated");
      }

      @Override
      protected Long extractIdFrom(GenericEntity source) {
        return source.id;
      }

      @Override
      protected void injectIdInto(Long id, GenericEntity target) {
        target.id = id;
      }

      @Override
      public Optional<GenericEntity> getExisting(GenericPojo example) {
        return Optional.empty();
      }
    };
  }

  private CrudGenericService<GenericPojo, GenericEntity> instantiate_with_existing_entity() {
    return new CrudGenericService<>(
      genericRepositoryMock,
      genericConverterMock,
      genericPatchServiceMock) {

      @Override
      public GenericPojo update(GenericPojo input) throws EntityNotFoundException, BadInputException {
        throw new UnsupportedOperationException("This method signature has been deprecated");
      }

      @Override
      protected Long extractIdFrom(GenericEntity source) {
        return source.id;
      }

      @Override
      protected void injectIdInto(Long id, GenericEntity target) {
        target.id = id;
      }

      @Override
      public Optional<GenericEntity> getExisting(GenericPojo example) {
        return Optional.of(persistedEntity);
      }
    };
  }

  @Data
  @AllArgsConstructor
  static class GenericPojo {
    Long id;
    String name;
  }

  @Data
  @AllArgsConstructor
  static class GenericEntity {
    Long id;
    String name;
  }
}
