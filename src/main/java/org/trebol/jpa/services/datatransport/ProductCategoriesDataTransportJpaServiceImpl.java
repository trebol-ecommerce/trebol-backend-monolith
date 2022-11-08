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

package org.trebol.jpa.services.datatransport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.jpa.services.IDataTransportJpaService;
import org.trebol.pojo.ProductCategoryPojo;

import java.util.Optional;

@Transactional
@Service
public class ProductCategoriesDataTransportJpaServiceImpl
  implements IDataTransportJpaService<ProductCategoryPojo, ProductCategory> {

  private final IProductsCategoriesJpaRepository categoriesRepository;

  @Autowired
  public ProductCategoriesDataTransportJpaServiceImpl(IProductsCategoriesJpaRepository repository) {
    this.categoriesRepository = repository;
  }

  @Override
  public ProductCategory applyChangesToExistingEntity(ProductCategoryPojo source, ProductCategory existing)
          throws BadInputException {
    ProductCategory target = new ProductCategory(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    this.applyParent(source, target);

    return target;
  }

  private void applyParent(ProductCategoryPojo source, ProductCategory target) {
    ProductCategoryPojo parent = source.getParent();
    if (parent != null) {
      String parentCode = parent.getCode();
      ProductCategory previousParent = target.getParent();
      if (parentCode == null) {
        this.applyNewParent(target, parent);
      } else if (previousParent == null || !previousParent.getCode().equals(parentCode)) {
        Optional<ProductCategory> parentMatch = categoriesRepository.findByCode(parentCode);
        if (parentMatch.isPresent()) {
          target.setParent(parentMatch.get());
        } else {
          this.applyNewParent(target, parent);
        }
      }
    }
  }

  // TODO remove this method and fix the signature of its calling method
  private void applyNewParent(ProductCategory target, ProductCategoryPojo parent) {
    // this method implies that a whole parent tree can be created from an update operation. that's bad design.
    // the `convertToNewEntity` method below, used to belong to the `ITwoWayConverterJpaService`
    ProductCategory newParentEntity = this.convertToNewEntity(parent);
    newParentEntity = categoriesRepository.save(newParentEntity);
    target.setParent(newParentEntity);
  }
}
