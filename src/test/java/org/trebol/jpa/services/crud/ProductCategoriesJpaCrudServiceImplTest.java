package org.trebol.jpa.services.crud;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.pojo.ProductCategoryPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.ProductCategoriesTestHelper.*;

@ExtendWith(MockitoExtension.class)
class ProductCategoriesJpaCrudServiceImplTest {
  @InjectMocks ProductCategoriesJpaCrudServiceImpl instance;
  @Mock IProductsCategoriesJpaRepository categoriesRepositoryMock;

  @BeforeEach
  void beforeEach() {
    resetProductCategories();
  }

  @Test
  void finds_by_code() throws BadInputException {
    ProductCategoryPojo input = productCategoryPojoForFetch();
    ProductCategory expectedResult = productCategoryEntityAfterCreation();
    when(categoriesRepositoryMock.findByCode(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<ProductCategory> match = instance.getExisting(input);

    verify(categoriesRepositoryMock).findByCode(input.getCode());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }
}
