package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ProductCategoryPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.jpa.testhelpers.ProductCategoriesJpaCrudServiceTestHelper.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductCategoriesJpaCrudServiceTest {

  @Mock IProductsCategoriesJpaRepository categoriesRepositoryMock;
  @Mock ITwoWayConverterJpaService<ProductCategoryPojo, ProductCategory> categoriesConverterMock;

  @Test
  public void sanity_check() {
    ProductCategoriesJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_code() throws BadInputException {
    resetProductCategories();
    when(categoriesRepositoryMock.findByCode(
        productCategoryPojoForFetch().getCode())).thenReturn(Optional.of(productCategoryEntityAfterCreation()));
    ProductCategoriesJpaCrudServiceImpl service = instantiate();

    Optional<ProductCategory> match = service.getExisting(productCategoryPojoForFetch());

    assertTrue(match.isPresent());
    verify(categoriesRepositoryMock).findByCode(productCategoryPojoForFetch().getCode());
    assertEquals(match.get().getId(), productCategoryEntityAfterCreation().getId());
    assertEquals(match.get().getCode(), productCategoryEntityAfterCreation().getCode());
    assertEquals(match.get().getName(), productCategoryEntityAfterCreation().getName());
  }

  private ProductCategoriesJpaCrudServiceImpl instantiate() {
    return new ProductCategoriesJpaCrudServiceImpl(
        categoriesRepositoryMock,
        categoriesConverterMock
    );
  }

}
