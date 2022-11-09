package org.trebol.jpa.services.datatransport;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.pojo.ProductCategoryPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
public class ProductCategoriesDataTransportJpaServiceImplTest {
    @InjectMocks
    private ProductCategoriesDataTransportJpaServiceImpl sut;

    @Mock
    private IProductsCategoriesJpaRepository categoriesRepository;

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

    @DisplayName("Will test parent which is a private method...")
    @Test
    void testApplyParent() throws BadInputException {
        final ProductCategory parent = new ProductCategory();
        parent.setName(ANY);
        productCategory.setParent(parent);
        productCategory.setName(ANY);
        productCategoryPojo.setParent(ProductCategoryPojo.builder().build());
        productCategory.setId(1L);
        when(categoriesRepository.save(any(ProductCategory.class))).thenReturn(parent);
        ProductCategory actual = sut.applyChangesToExistingEntity(productCategoryPojo, productCategory);

        assertEquals(ANY, actual.getParent().getName());
    }

    @Test
    void testApplyParent2() throws BadInputException {
        final ProductCategory parent = new ProductCategory();
        parent.setName(ANY);
        parent.setCode("");
        productCategory.setParent(parent);
        productCategory.setName(ANY);
        productCategoryPojo.setParent(ProductCategoryPojo.builder().code(ANY).build());
        productCategory.setId(1L);
        when(categoriesRepository.save(any(ProductCategory.class))).thenReturn(parent);
        ProductCategory actual = sut.applyChangesToExistingEntity(productCategoryPojo, productCategory);

        assertEquals(ANY, actual.getParent().getName());
    }
}
