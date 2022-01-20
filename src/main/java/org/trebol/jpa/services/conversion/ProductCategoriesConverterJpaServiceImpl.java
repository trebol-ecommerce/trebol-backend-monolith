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

package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductCategoryPojo;

import javax.annotation.Nullable;
import java.util.Optional;

@Transactional
@Service
public class ProductCategoriesConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<ProductCategoryPojo, ProductCategory> {

  private final IProductsCategoriesJpaRepository categoriesRepository;
  private final ConversionService conversion;

  @Autowired
  public ProductCategoriesConverterJpaServiceImpl(IProductsCategoriesJpaRepository repository,
                                                  ConversionService conversion) {
    this.categoriesRepository = repository;
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public ProductCategoryPojo convertToPojo(ProductCategory source) {
    return conversion.convert(source, ProductCategoryPojo.class);
  }

  @Override
  public ProductCategory convertToNewEntity(ProductCategoryPojo source) {
    ProductCategory target = new ProductCategory();
    target.setCode(source.getCode());
    target.setName(source.getName());
    this.applyParent(source, target);
    return target;
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

  private void applyNewParent(ProductCategory target, ProductCategoryPojo parent) {
    ProductCategory newParentEntity = this.convertToNewEntity(parent);
    newParentEntity = categoriesRepository.save(newParentEntity);
    target.setParent(newParentEntity);
  }
}
