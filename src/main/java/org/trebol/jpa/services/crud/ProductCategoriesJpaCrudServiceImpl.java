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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.conversion.IProductCategoriesConverterJpaService;
import org.trebol.jpa.services.datatransport.IProductCategoriesDataTransportJpaService;
import org.trebol.pojo.ProductCategoryPojo;

import java.util.Optional;

@Transactional
@Service
public class ProductCategoriesJpaCrudServiceImpl
  extends GenericCrudJpaService<ProductCategoryPojo, ProductCategory> {

  private final IProductsCategoriesJpaRepository categoriesRepository;

  @Autowired
  public ProductCategoriesJpaCrudServiceImpl(IProductsCategoriesJpaRepository repository,
                                             IProductCategoriesConverterJpaService converter,
                                             IProductCategoriesDataTransportJpaService dataTransportService) {
    super(repository,
          converter,
          dataTransportService);
    this.categoriesRepository = repository;
  }

  @Override
  public Optional<ProductCategory> getExisting(ProductCategoryPojo input) throws BadInputException {
    String code = input.getCode();
    if (code == null || code.isBlank()) {
      throw new BadInputException("Invalid category code");
    } else {
      return this.categoriesRepository.findByCode(code);
    }
  }

  @Override
  protected final ProductCategory prepareNewEntityFromInputPojo(ProductCategoryPojo inputPojo) throws BadInputException {
    ProductCategory target = super.prepareNewEntityFromInputPojo(inputPojo);
    if (inputPojo.getParent() != null) {
      this.passParentIfMatchingEntityExists(target, inputPojo.getParent());
    }
    return target;
  }

  @Override
  protected final ProductCategory prepareEntityWithUpdatesFromPojo(ProductCategoryPojo changes, ProductCategory existingEntity) throws BadInputException {
    ProductCategory preparedEntity = dataTransportService.applyChangesToExistingEntity(changes, existingEntity);
    this.passParentIfMatchingEntityExists(preparedEntity, changes.getParent());
    if (!existingEntity.equals(preparedEntity)) {
      return existingEntity;
    }
    return preparedEntity;
  }

  private void passParentIfMatchingEntityExists(ProductCategory target, ProductCategoryPojo sourceParent) {
    String sourceParentCode = sourceParent.getCode();
    ProductCategory previousExistingParent = target.getParent();
    if (sourceParentCode != null && (previousExistingParent == null || !previousExistingParent.getCode().equals(sourceParentCode))) {
      Optional<ProductCategory> parentMatch = categoriesRepository.findByCode(sourceParentCode);
      if (parentMatch.isPresent()) {
        target.setParent(parentMatch.get());
      }
    }
  }
}
