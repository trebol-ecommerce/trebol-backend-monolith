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
import static org.mockito.Mockito.when;

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
    Long categoryId = 1L;
    String categoryCode = "test-one";
    String categoryName = "test-one.jpg";
    ProductCategory parentEntity = null;
    ProductCategoryPojo example = new ProductCategoryPojo(categoryCode);
    ProductCategory persistedEntity = new ProductCategory(categoryId, categoryCode, categoryName, parentEntity);
    when(categoriesRepositoryMock.findByCode(categoryCode)).thenReturn(Optional.of(persistedEntity));
    ProductCategoriesJpaCrudServiceImpl service = instantiate();

    Optional<ProductCategory> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), categoryId);
    assertEquals(match.get().getCode(), categoryCode);
    assertEquals(match.get().getName(), categoryName);
    assertEquals(match.get().getParent(), parentEntity);
  }

  private ProductCategoriesJpaCrudServiceImpl instantiate() {
    return new ProductCategoriesJpaCrudServiceImpl(
        categoriesRepositoryMock,
        categoriesConverterMock
    );
  }

}
