package org.trebol.jpa.services.crud;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductCategoryPojo;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductCategoriesJpaCrudServiceImpl
  extends GenericCrudJpaService<ProductCategoryPojo, ProductCategory> {

  private final IProductsCategoriesJpaRepository categoriesRepository;

  @Autowired
  public ProductCategoriesJpaCrudServiceImpl(IProductsCategoriesJpaRepository repository,
                                             ITwoWayConverterJpaService<ProductCategoryPojo, ProductCategory> converter) {
    super(repository,
          converter,
          LoggerFactory.getLogger(ProductCategoriesJpaCrudServiceImpl.class));
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
}
