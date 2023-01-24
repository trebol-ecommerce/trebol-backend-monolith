/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.repositories.ProductListItemsJpaRepository;
import org.trebol.jpa.repositories.ProductListsJpaRepository;
import org.trebol.jpa.services.CrudGenericService;
import org.trebol.jpa.services.conversion.ProductListsConverterService;
import org.trebol.jpa.services.datatransport.ProductListsDataTransportService;
import org.trebol.pojo.ProductListPojo;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Transactional
@Service
public class ProductListsCrudServiceImpl
  extends CrudGenericService<ProductListPojo, ProductList>
  implements ProductListCrudService {
  private final ProductListsJpaRepository listsRepository;
  private final ProductListItemsJpaRepository listItemsRepository;

  @Autowired
  public ProductListsCrudServiceImpl(
    ProductListsJpaRepository listsRepository,
    ProductListItemsJpaRepository listItemsRepository,
    ProductListsConverterService listsConverterService,
    ProductListsDataTransportService listsDataTransportService
  ) {
    super(listsRepository, listsConverterService, listsDataTransportService);
    this.listsRepository = listsRepository;
    this.listItemsRepository = listItemsRepository;
  }

  @Override
  public void delete(Predicate filters)
    throws EntityNotFoundException {
    long count = listsRepository.count(filters);
    if (count == 0) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    } else {
      for (ProductList list : listsRepository.findAll(filters)) {
        listItemsRepository.deleteByListId(list.getId());
      }

      listsRepository.deleteAll(listsRepository.findAll(filters));
    }
  }

  @Override
  public Optional<ProductList> getExisting(ProductListPojo input) {
    Long id = input.getId();
    String name = input.getName();
    if (id == null && name == null) {
      return Optional.empty();
    } else if (id != null) {
      return listsRepository.findById(id);
    } else {
      return listsRepository.findByName(name);
    }
  }


}