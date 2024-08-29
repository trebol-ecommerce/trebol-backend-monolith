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

package org.trebol.jpa.services.crud;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.api.models.DataPagePojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.DBEntity;
import org.trebol.jpa.Repository;
import org.trebol.jpa.services.ConverterService;
import org.trebol.jpa.services.CrudService;
import org.trebol.jpa.services.PatchService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstraction that supports all four CRUD operations.<br/>
 * Communicates with other services mostly using model classes.<br/>
 * <p>
 * Whenever possible, its public abstract API should not be overriden, but its protected methods instead.
 *
 * @param <M> The models class
 * @param <E> The entity class
 */
@Transactional
public abstract class CrudGenericService<M, E extends DBEntity>
    implements CrudService<M, E> {
    protected static final String ITEM_NOT_FOUND = "Requested item(s) not found";
    protected static final String ITEM_ALREADY_EXISTS = "The item already exists";
    private final Repository<E> repository;
    private final ConverterService<M, E> converter;
    private final PatchService<M, E> patchService;

    protected CrudGenericService(
        Repository<E> repository,
        ConverterService<M, E> converter,
        PatchService<M, E> patchService
    ) {
        this.repository = repository;
        this.converter = converter;
        this.patchService = patchService;
    }

    /**
     * Converts a model class instance to an entity instance, saves it and returns it back as a model copy of the persisted entity.
     *
     * @param input model Pojo instance to be converted and inserted.
     * @throws BadInputException     When the data required from the input object is invalid or insufficient to build a proper entity.
     * @throws EntityExistsException When the data is a duplicate of an existing entity.
     */
    @Override
    public M create(M input) throws BadInputException, EntityExistsException {
        this.validateInputPojoBeforeCreation(input);
        E preparedEntity = converter.convertToNewEntity(input);
        E result = repository.saveAndFlush(preparedEntity);
        return converter.convertToPojo(result);
    }

    /**
     * Read data from repository, convert each entity to its equivalent model class and
     * return the collected data in a {@link org.trebol.api.models.DataPagePojo}.
     */
    @Override
    public DataPagePojo<M> readMany(int pageIndex, int pageSize, @Nullable Sort order, @Nullable Predicate filters) {
        Pageable pagination = ((order==null) ?
            PageRequest.of(pageIndex, pageSize):
            PageRequest.of(pageIndex, pageSize, order));
        long totalCount = ((filters==null) ?
            repository.count():
            repository.count(filters));
        Page<E> iterable = ((filters==null) ?
            repository.findAll(pagination):
            repository.findAll(filters, pagination));
        List<M> pojoList = new ArrayList<>();
        for (E item : iterable) {
            M outputItem = converter.convertToPojo(item);
            pojoList.add(outputItem);
        }
        return new DataPagePojo<>(pojoList, pageIndex, totalCount, pageSize);
    }

    @Override
    public M update(M input) throws EntityNotFoundException, BadInputException {
        throw new UnsupportedOperationException("This method signature has been deprecated");
    }

    /**
     * @throws EntityNotFoundException When no entity matches the given id.
     * @throws BadInputException       When the data in the input object is not valid.<br/>
     *                                 It is expected that some portions data may be null, because it may not have
     *                                 been included during serialization. Such cases are <i>not</i> meant to cause
     *                                 a BadInputException.
     */
    @Override
    public Optional<M> update(M input, Long id) throws EntityNotFoundException, BadInputException {
        E inputEntity = converter.convertToNewEntity(input);
        inputEntity.setId(id);
        E resultEntity = repository.saveAndFlush(inputEntity);
        M output = converter.convertToPojo(resultEntity);
        return Optional.of(output);
    }

    /**
     * @throws BadInputException When the data in the input object is not valid.
     */
    @Override
    public Optional<M> update(M input, Predicate filters) throws BadInputException {
        long count = repository.count(filters);
        if (count > 1) {
            throw new RuntimeException("Cannot update more than one item at a time");
        }
        Optional<E> firstMatch = repository.findOne(filters);
        if (firstMatch.isEmpty()) {
            return Optional.empty();
        }
        Long id = firstMatch.get().getId();
        return this.update(input, id);
    }

    @Override
    public Optional<M> partialUpdate(Map<String, Object> changes, Long id) {
        return repository.findById(id)
            .map(existingEntity -> {
                try {
                    E result = this.flushPartialChanges(changes, existingEntity);
                    return converter.convertToPojo(result);
                } catch (BadInputException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public Optional<M> partialUpdate(Map<String, Object> changes, Predicate filters) {
        long count = repository.count(filters);
        if (count > 1) {
            throw new RuntimeException("Cannot update more than one item at a time");
        }
        return repository.findOne(filters)
            .map(existingEntity -> {
                try {
                    E result = this.flushPartialChanges(changes, existingEntity);
                    return converter.convertToPojo(result);
                } catch (BadInputException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * @throws EntityNotFoundException When no entity matches the given filtering conditions.
     */
    @Override
    public void delete(Predicate filters)
        throws EntityNotFoundException {
        long count = repository.count(filters);
        if (count==0) {
            throw new EntityNotFoundException(ITEM_NOT_FOUND);
        }
        repository.deleteAll(repository.findAll(filters));
    }

    /**
     * @throws EntityNotFoundException When no entity matches the given filtering conditions.
     */
    @Override
    public M readOne(Predicate filters)
        throws EntityNotFoundException {
        Optional<E> entity = repository.findOne(filters);
        if (entity.isEmpty()) {
            throw new EntityNotFoundException(ITEM_NOT_FOUND);
        }
        E found = entity.get();
        return converter.convertToPojo(found);
    }

    protected E flushPartialChanges(Map<String, Object> changes, E existingEntity) throws BadInputException {
        E updatedEntity = patchService.patchExistingEntity(changes, existingEntity);
        if (existingEntity.equals(updatedEntity)) {
            return existingEntity;
        }
        return repository.saveAndFlush(updatedEntity);
    }

    /**
     * Generic validation routine. Should be called at the beginning of the create() method.
     *
     * @param inputPojo A model to validate
     * @throws BadInputException If the model does not have a valid identifying property
     * @throws BadInputException If the model does not have a valid identifying property
     */
    protected void validateInputPojoBeforeCreation(M inputPojo) throws BadInputException {
        if (this.getExisting(inputPojo).isPresent()) {
            throw new EntityExistsException(ITEM_ALREADY_EXISTS);
        }
    }
}
