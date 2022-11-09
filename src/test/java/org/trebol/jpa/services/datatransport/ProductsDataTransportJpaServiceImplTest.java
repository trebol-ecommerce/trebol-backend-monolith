package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.pojo.ProductPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
public class ProductsDataTransportJpaServiceImplTest {

    @InjectMocks
    private ProductsDataTransportJpaServiceImpl sut;

    private Product product;
    private ProductPojo productPojo;

    @BeforeEach
    void beforeEach() {
        product = new Product();
        product.setName(ANY);
        product.setId(1L);
        productPojo = ProductPojo.builder()
          .id(1L)
          .name(ANY)
          .build();
    }

    @AfterEach
    void afterEach() {
        product = null;
        productPojo = null;
    }

    @Test
    void testApplyChangesToExistingEntity() throws BadInputException {
        productPojo.setBarcode(ANY);
        productPojo.setName("Bear Brand");
        productPojo.setPrice(1);
        productPojo.setDescription(ANY);
        productPojo.setCurrentStock(1);

        product.setBarcode(ANY + " ");
        product.setName("Bear Brand  ");
        product.setPrice(2);
        product.setDescription(ANY + " ");
        Product actual = sut.applyChangesToExistingEntity(productPojo, product);
        assertEquals(ANY, actual.getBarcode());
    }
}
