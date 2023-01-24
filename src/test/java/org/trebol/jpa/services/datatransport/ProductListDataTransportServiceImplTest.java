package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductList;
import org.trebol.pojo.ProductListPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ProductListDataTransportServiceImplTest {
  @InjectMocks ProductListDataTransportServiceImpl sut;
  ProductList productList;
  ProductListPojo productListPojo;

  @BeforeEach
  void beforeEach() {
    productList = new ProductList();
    productList.setId(1L);
    productList.setName(ANY);
    productList.setName(ANY);
    productListPojo = ProductListPojo.builder()
      .id(1L)
      .name(ANY + " ")
      .code(ANY + " ")
      .build();
  }

  @AfterEach
  void afterEach() {
    productList = null;
    productListPojo = null;
  }

  @Test
  void testApplyChangesToExistingEntity() throws BadInputException {
    ProductList actual = sut.applyChangesToExistingEntity(productListPojo, productList);
    assertEquals(1L, actual.getId());
  }
}
