package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.pojo.ProductPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductListItemsConverterServiceImplTest {
  @InjectMocks ProductListItemsConverterServiceImpl sut;
  @Mock IProductsConverterService productsConverterService;

  @Test
  void testApplyChangesToExistingEntity() {
    UnsupportedOperationException unsupportedOperationException = assertThrows(UnsupportedOperationException.class,
      () -> sut.convertToNewEntity(ProductPojo.builder().build()));
    assertEquals("Not implemented", unsupportedOperationException.getMessage());
  }

  @Test
  void testConvertToPojo() {
    final ProductListItem productListItem = new ProductListItem();
    final Product product = new Product();
    productListItem.setProduct(product);
    ProductPojo expectedResult = ProductPojo.builder().id(1L).build();
    when(productsConverterService.convertToPojo(any(Product.class))).thenReturn(expectedResult);

    ProductPojo actual = sut.convertToPojo(productListItem);

    assertEquals(expectedResult, actual);
  }
}
