package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.pojo.ProductCategoryPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
public class ProductCategoriesDataTransportJpaServiceImplTest {
    @InjectMocks ProductCategoriesDataTransportJpaServiceImpl sut;
    private ProductCategory productCategory;
    private ProductCategoryPojo productCategoryPojo;

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
    void testApplyChangesToExistingEntity() throws BadInputException {
        productCategory.setName(ANY + " ");
        ProductCategory actual = sut.applyChangesToExistingEntity(productCategoryPojo, productCategory);
        assertEquals(ANY, actual.getName());
    }
}
