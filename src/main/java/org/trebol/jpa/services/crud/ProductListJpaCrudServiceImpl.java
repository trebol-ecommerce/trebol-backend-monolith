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
import org.trebol.jpa.repositories.IProductListItemsJpaRepository;
import org.trebol.jpa.repositories.IProductListsJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.conversion.IProductListsConverterJpaService;
import org.trebol.jpa.services.datatransport.IProductListsDataTransportJpaService;
import org.trebol.pojo.ProductListPojo;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Transactional
@Service
public class ProductListJpaCrudServiceImpl
  extends GenericCrudJpaService<ProductListPojo, ProductList> {

  private final IProductListsJpaRepository productListRepository;
  private final IProductListItemsJpaRepository productListItemRepository;

  @Autowired
  public ProductListJpaCrudServiceImpl(IProductListsJpaRepository productListRepository,
                                       IProductListItemsJpaRepository productListItemRepository,
                                       IProductListsConverterJpaService converterService,
                                       IProductListsDataTransportJpaService dataTransportService) {
    super(productListRepository,
           converterService,
           dataTransportService);
    this.productListRepository = productListRepository;
    this.productListItemRepository = productListItemRepository;
  }

  @Override
  public void delete(Predicate filters)
      throws EntityNotFoundException {
    long count = productListRepository.count(filters);
    if (count == 0) {
      throw new EntityNotFoundException(ITEM_NOT_FOUND);
    } else {
      for (ProductList list : productListRepository.findAll(filters)) {
        productListItemRepository.deleteByListId(list.getId());
      }

      productListRepository.deleteAll(productListRepository.findAll(filters));
    }
  }

  @Override
  public Optional<ProductList> getExisting(ProductListPojo input) {
    Long id = input.getId();
    String name = input.getName();
    if (id == null && name == null) {
      return Optional.empty();
    } else if (id != null) {
      return productListRepository.findById(id);
    } else {
      return productListRepository.findByName(name);
    }
  }


}
