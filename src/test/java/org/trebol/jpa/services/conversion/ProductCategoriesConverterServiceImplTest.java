package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.pojo.ProductCategoryPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
public class ProductCategoriesConverterServiceImplTest {
  @InjectMocks ProductCategoriesConverterServiceImpl sut;
  ProductCategory productCategory;
  ProductCategoryPojo productCategoryPojo;

  @BeforeEach
  void beforeEach() {
    productCategory = new ProductCategory();
    productCategory.setId(ID_1L);
    productCategoryPojo = ProductCategoryPojo.builder()
      .id(ID_1L)
      .name(ANY)
      .code(ANY)
      .build();
  }

  @AfterEach
  void afterEach() {
    productCategory = null;
    productCategoryPojo = null;
  }

  @Test
  void testConvertToPojo() {
    ProductCategoryPojo actual = sut.convertToPojo(productCategory);
    assertEquals(productCategory.getName(), actual.getName());
    assertEquals(productCategory.getCode(), actual.getCode());
  }

  @Test
  void testConvertToNewEntity() {
    ProductCategory actual = sut.convertToNewEntity(productCategoryPojo);
    assertEquals(productCategoryPojo.getName(), actual.getName());
    assertEquals(productCategoryPojo.getCode(), actual.getCode());
  }
}
