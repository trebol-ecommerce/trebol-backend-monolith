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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.trebol.api.models.DataPagePojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.DBEntity;
import org.trebol.jpa.Repository;
import org.trebol.jpa.services.crud.CrudGenericService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class CrudGenericServiceTest {
    static final String GENERIC_NAME = "test";
    @Mock Repository<GenericEntity> genericRepositoryMock;
    @Mock ConverterService<GenericPojo, GenericEntity> genericConverterMock;
    @Mock PatchService<GenericPojo, GenericEntity> genericPatchServiceMock;
    static final GenericPojo NEW_POJO = new GenericPojo(null, GENERIC_NAME);
    static final GenericEntity NEW_ENTITY = new GenericEntity(null, GENERIC_NAME);
    static final GenericEntity PERSISTED_ENTITY = new GenericEntity(1L, GENERIC_NAME);
    static final GenericPojo PERSISTED_POJO = new GenericPojo(1L, GENERIC_NAME);
    static final List<GenericPojo> EMPTY_POJO_LIST = List.of();
    static final Page<GenericEntity> EMPTY_ENTITY_PAGE = new PageImpl<>(List.of());
    static final List<GenericEntity> PERSISTED_ENTITY_LIST = List.of(PERSISTED_ENTITY);
    static final List<GenericPojo> PERSISTED_POJO_LIST = List.of(PERSISTED_POJO);
    static final PageRequest SIMPLE_PAGE_REQUEST = PageRequest.of(0, 10);

    @Test
    void sanity_checks() {
        List.of(
            new MockServiceWithoutExistingEntity(this),
            new MockServiceWithExistingEntity(this)
        ).forEach(service -> {
            assertNotNull(service);
            UnsupportedOperationException oldUpdateMethodException = assertThrows(UnsupportedOperationException.class, () -> service.update(NEW_POJO));
            assertEquals("This method signature has been deprecated", oldUpdateMethodException.getMessage());
        });
    }

    @Test
    void creates_data() throws BadInputException, EntityExistsException {
        when(genericConverterMock.convertToNewEntity(any(GenericPojo.class))).thenReturn(NEW_ENTITY);
        when(genericRepositoryMock.saveAndFlush(any(GenericEntity.class))).thenReturn(PERSISTED_ENTITY);
        when(genericConverterMock.convertToPojo(any(GenericEntity.class))).thenReturn(PERSISTED_POJO);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithoutExistingEntity(this);

        GenericPojo result = service.create(NEW_POJO);

        assertNotNull(result);
        assertEquals(PERSISTED_POJO, result);
        verify(genericConverterMock).convertToNewEntity(NEW_POJO);
        verify(genericRepositoryMock).saveAndFlush(NEW_ENTITY);
        verify(genericConverterMock).convertToPojo(PERSISTED_ENTITY);
    }

    @Test
    void parses_sorting_order() {
        Sort sortOrder = Sort.by(ANY);
        Pageable expectedPagination = PageRequest.of(0, 10, sortOrder);
        when(genericRepositoryMock.findAll(any(PageRequest.class))).thenReturn(EMPTY_ENTITY_PAGE);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithoutExistingEntity(this);

        DataPagePojo<GenericPojo> result = service.readMany(0, 10, sortOrder, null);

        assertNotNull(result);
        verify(genericRepositoryMock).findAll(expectedPagination);
    }

    @Test
    void reads_many_items() {
        PageImpl<GenericEntity> singleItemPage = new PageImpl<>(PERSISTED_ENTITY_LIST);
        when(genericRepositoryMock.count()).thenReturn(1L);
        when(genericRepositoryMock.findAll(any(PageRequest.class))).thenReturn(singleItemPage);
        when(genericConverterMock.convertToPojo(any(GenericEntity.class))).thenReturn(PERSISTED_POJO);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        DataPagePojo<GenericPojo> result = service.readMany(0, 10, null, null);

        assertEquals(PERSISTED_POJO_LIST, result.getItems());
        verify(genericRepositoryMock).findAll(SIMPLE_PAGE_REQUEST);
        verify(genericConverterMock).convertToPojo(PERSISTED_ENTITY);
    }

    @Test
    void will_not_error_when_a_read_query_for_many_items_returns_zero_items() {
        when(genericRepositoryMock.findAll(any(PageRequest.class))).thenReturn(EMPTY_ENTITY_PAGE);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithoutExistingEntity(this);

        DataPagePojo<GenericPojo> result = service.readMany(0, 10, null, null);

        assertNotNull(result);
        assertEquals(EMPTY_POJO_LIST, result.getItems());
        verify(genericRepositoryMock).findAll(SIMPLE_PAGE_REQUEST);
    }

    @Test
    void reads_a_single_item() throws EntityNotFoundException {
        Predicate filters = new BooleanBuilder();
        Optional<GenericEntity> result = Optional.of(PERSISTED_ENTITY);
        when(genericRepositoryMock.findOne(any(Predicate.class))).thenReturn(result);
        when(genericConverterMock.convertToPojo(any(GenericEntity.class))).thenReturn(PERSISTED_POJO);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        GenericPojo foundPojo = service.readOne(filters);

        assertEquals(PERSISTED_POJO, foundPojo);
        verify(genericRepositoryMock).findOne(filters);
        verify(genericConverterMock).convertToPojo(PERSISTED_ENTITY);
    }

    @Test
    void will_error_out_when_a_read_query_for_a_single_item_finds_nothing() {
        when(genericRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.empty());
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithoutExistingEntity(this);

        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> service.readOne(null));

        assertEquals("Requested item(s) not found", result.getMessage());
        verify(genericRepositoryMock).findOne((Predicate) isNull());
    }

    @Test
    void updates_a_single_item_using_id() throws BadInputException, EntityNotFoundException {
        Long id = 1L;
        String name = "test2";
        GenericPojo changes = new GenericPojo(null, name);
        GenericEntity changesEntity = new GenericEntity(null, name);
        GenericPojo updatedPojo = new GenericPojo(id, name);
        when(genericConverterMock.convertToNewEntity(any(GenericPojo.class))).thenReturn(changesEntity);
        when(genericConverterMock.convertToPojo(nullable(GenericEntity.class))).thenReturn(updatedPojo);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        Optional<GenericPojo> result = service.update(changes, id);

        assertTrue(result.isPresent());
        assertEquals(updatedPojo, result.get());
        verify(genericConverterMock).convertToNewEntity(changes);
        verify(genericRepositoryMock).saveAndFlush(changesEntity);
        verify(genericConverterMock).convertToPojo(null);
    }

    @Test
    void updates_a_single_item_using_filters() throws BadInputException, EntityNotFoundException {
        GenericPojo changes = new GenericPojo(null, ANY);
        Predicate filters = new BooleanBuilder();
        GenericEntity changesEntity = new GenericEntity(null, ANY);
        GenericPojo updatedPojo = new GenericPojo(1L, ANY);
        when(genericRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(PERSISTED_ENTITY));
        when(genericConverterMock.convertToNewEntity(any(GenericPojo.class))).thenReturn(changesEntity);
        when(genericConverterMock.convertToPojo(nullable(GenericEntity.class))).thenReturn(updatedPojo);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        Optional<GenericPojo> result = service.update(changes, filters);

        assertTrue(result.isPresent());
        assertEquals(updatedPojo, result.get());
        verify(genericRepositoryMock).findOne(filters);
        verify(genericConverterMock).convertToNewEntity(changes);
        verify(genericRepositoryMock).saveAndFlush(changesEntity);
        verify(genericConverterMock).convertToPojo(null);
    }

    @Test
    void will_error_out_when_an_update_query_with_filters_finds_too_many_items() throws EntityNotFoundException {
        long tooMany = 2L;
        GenericPojo changes = new GenericPojo(null, ANY);
        when(genericRepositoryMock.count(nullable(Predicate.class))).thenReturn(tooMany);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        RuntimeException result = assertThrows(RuntimeException.class, () -> service.update(changes, (Predicate) null));

        assertEquals("Cannot update more than one item at a time", result.getMessage());
    }

    @Test
    void will_not_error_when_an_update_query_with_filters_finds_nothing() throws BadInputException, EntityNotFoundException {
        GenericPojo changes = new GenericPojo(null, ANY);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        Optional<GenericPojo> result = service.update(changes, (Predicate) null);

        assertTrue(result.isEmpty());
    }

    @Test
    void partially_updates_a_single_item_using_id() throws BadInputException, EntityNotFoundException {
        Long id = 1L;
        String name = "test2";
        Map<String, Object> changes = Map.of("name", name);
        GenericEntity updatedEntity = new GenericEntity(id, name);
        GenericPojo updatedPojo = new GenericPojo(id, name);
        when(genericRepositoryMock.findById(anyLong())).thenReturn(Optional.of(PERSISTED_ENTITY));
        when(genericPatchServiceMock.patchExistingEntity(anyMap(), any(GenericEntity.class))).thenReturn(updatedEntity);
        when(genericRepositoryMock.saveAndFlush(any(GenericEntity.class))).thenReturn(updatedEntity);
        when(genericConverterMock.convertToPojo(any(GenericEntity.class))).thenReturn(updatedPojo);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        Optional<GenericPojo> result = service.partialUpdate(changes, id);

        assertTrue(result.isPresent());
        assertEquals(updatedPojo, result.get());
        verify(genericPatchServiceMock).patchExistingEntity(changes, PERSISTED_ENTITY);
        verify(genericRepositoryMock).saveAndFlush(updatedEntity);
        verify(genericConverterMock).convertToPojo(updatedEntity);
    }

    @Test
    void will_return_early_if_changes_made_by_a_partial_update_query_with_id_amounted_to_zero() throws BadInputException, EntityNotFoundException {
        Map<String, Object> changes = Map.of("name", ANY);
        when(genericRepositoryMock.findById(anyLong())).thenReturn(Optional.of(PERSISTED_ENTITY));
        when(genericPatchServiceMock.patchExistingEntity(anyMap(), any(GenericEntity.class))).thenReturn(PERSISTED_ENTITY);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        service.partialUpdate(changes, 0L);

        verify(genericRepositoryMock, times(0)).saveAndFlush(any(GenericEntity.class));
    }

    @Test
    void will_cascade_errors_from_patch_service_during_partial_updates_with_id() throws BadInputException, EntityNotFoundException {
        BadInputException expectedCause = new BadInputException(ANY);
        Map<String, Object> changes = Map.of("name", ANY);
        when(genericRepositoryMock.findById(anyLong())).thenReturn(Optional.of(PERSISTED_ENTITY));
        when(genericPatchServiceMock.patchExistingEntity(anyMap(), any(GenericEntity.class))).thenThrow(expectedCause);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        RuntimeException result = assertThrows(RuntimeException.class, () -> service.partialUpdate(changes, 0L));

        assertEquals(expectedCause, result.getCause());
        verify(genericPatchServiceMock).patchExistingEntity(changes, PERSISTED_ENTITY);
    }

    @Test
    void partially_updates_a_single_item_using_filters() throws BadInputException, EntityNotFoundException {
        Predicate filters = new BooleanBuilder();
        Map<String, Object> changes = Map.of("name", ANY);
        GenericEntity updatedEntity = new GenericEntity(1L, ANY);
        GenericPojo updatedPojo = new GenericPojo(1L, ANY);
        when(genericRepositoryMock.findOne(any(Predicate.class))).thenReturn(Optional.of(PERSISTED_ENTITY));
        when(genericPatchServiceMock.patchExistingEntity(anyMap(), any(GenericEntity.class))).thenReturn(updatedEntity);
        when(genericRepositoryMock.saveAndFlush(any(GenericEntity.class))).thenReturn(updatedEntity);
        when(genericConverterMock.convertToPojo(any(GenericEntity.class))).thenReturn(updatedPojo);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        Optional<GenericPojo> result = service.partialUpdate(changes, filters);

        assertTrue(result.isPresent());
        assertEquals(updatedPojo, result.get());
        verify(genericRepositoryMock).findOne(filters);
        verify(genericPatchServiceMock).patchExistingEntity(changes, PERSISTED_ENTITY);
        verify(genericRepositoryMock).saveAndFlush(updatedEntity);
        verify(genericConverterMock).convertToPojo(updatedEntity);
    }

    @Test
    void will_return_early_if_changes_made_by_a_partial_update_query_with_filters_amounted_to_zero() throws BadInputException, EntityNotFoundException {
        Map<String, Object> changes = Map.of("name", ANY);
        when(genericRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.of(PERSISTED_ENTITY));
        when(genericPatchServiceMock.patchExistingEntity(anyMap(), any(GenericEntity.class))).thenReturn(PERSISTED_ENTITY);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        service.partialUpdate(changes, (Predicate) null);

        verify(genericRepositoryMock, times(0)).saveAndFlush(any(GenericEntity.class));
    }

    @Test
    void will_error_out_when_a_partial_update_query_with_filters_finds_too_many_items() throws EntityNotFoundException {
        long tooMany = 2L;
        Map<String, Object> changes = Map.of("name", ANY);
        when(genericRepositoryMock.count(nullable(Predicate.class))).thenReturn(tooMany);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        RuntimeException result = assertThrows(RuntimeException.class, () -> service.partialUpdate(changes, (Predicate) null));

        assertEquals("Cannot update more than one item at a time", result.getMessage());
    }

    @Test
    void will_cascade_errors_from_patch_service_during_partial_updates_with_filters() throws BadInputException, EntityNotFoundException {
        BadInputException expectedCause = new BadInputException(ANY);
        Map<String, Object> changes = Map.of("name", ANY);
        when(genericRepositoryMock.findOne(nullable(Predicate.class))).thenReturn(Optional.of(PERSISTED_ENTITY));
        when(genericPatchServiceMock.patchExistingEntity(anyMap(), any(GenericEntity.class))).thenThrow(expectedCause);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        RuntimeException result = assertThrows(RuntimeException.class, () -> service.partialUpdate(changes, (Predicate) null));

        assertEquals(expectedCause, result.getCause());
        verify(genericPatchServiceMock).patchExistingEntity(changes, PERSISTED_ENTITY);
    }

    @Test
    void deletes_data() throws EntityNotFoundException {
        PageImpl<GenericEntity> persistedEntityPage = new PageImpl<>(PERSISTED_ENTITY_LIST);
        when(genericRepositoryMock.count(nullable(Predicate.class))).thenReturn(1L);
        when(genericRepositoryMock.findAll(nullable(Predicate.class))).thenReturn(persistedEntityPage);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        service.delete(null);

        verify(genericRepositoryMock).count((Predicate) isNull());
        verify(genericRepositoryMock).findAll((Predicate) isNull());
        verify(genericRepositoryMock).deleteAll(persistedEntityPage);
    }

    @Test
    void will_error_out_when_a_delete_query_affects_zero_items() throws EntityNotFoundException {
        when(genericRepositoryMock.count(nullable(Predicate.class))).thenReturn(0L);
        CrudGenericService<GenericPojo, GenericEntity> service = new MockServiceWithExistingEntity(this);

        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> service.delete(null));

        assertEquals("Requested item(s) not found", result.getMessage());
        verify(genericRepositoryMock).count((Predicate) isNull());
    }

    @Data
    @AllArgsConstructor
    private static class GenericPojo {
        Long id;
        String name;
    }

    @Data
    @AllArgsConstructor
    private static class GenericEntity implements DBEntity {
        Long id;
        String name;
    }

    private static class MockServiceWithoutExistingEntity extends CrudGenericService<GenericPojo, GenericEntity> {
        MockServiceWithoutExistingEntity(CrudGenericServiceTest testClass) {
            super(testClass.genericRepositoryMock, testClass.genericConverterMock, testClass.genericPatchServiceMock);
        }

        @Override
        public Optional<GenericEntity> getExisting(GenericPojo example) {
            return Optional.empty();
        }
    }

    private static class MockServiceWithExistingEntity extends CrudGenericService<GenericPojo, GenericEntity> {
        MockServiceWithExistingEntity(CrudGenericServiceTest testClass) {
            super(testClass.genericRepositoryMock, testClass.genericConverterMock, testClass.genericPatchServiceMock);
        }

        @Override
        public Optional<GenericEntity> getExisting(GenericPojo example) {
            return Optional.of(CrudGenericServiceTest.PERSISTED_ENTITY);
        }
    }
}
