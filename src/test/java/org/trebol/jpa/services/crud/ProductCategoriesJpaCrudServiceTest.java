package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductCategoryPojo;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ProductCategoriesJpaCrudServiceTest {

  @Mock IProductsCategoriesJpaRepository categoriesRepositoryMock;
  @Mock ITwoWayConverterJpaService<ProductCategoryPojo, ProductCategory> categoriesConverterMock;

  @Test
  public void sanity_check() {
    ProductCategoriesJpaCrudServiceImpl service = new ProductCategoriesJpaCrudServiceImpl(
        categoriesRepositoryMock,
        categoriesConverterMock
    );
    assertNotNull(service);
  }

}
