package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.pojo.ProductCategoryPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
public class ProductCategoriesConverterJpaServiceImplTest {
    @InjectMocks
    private ProductCategoriesConverterJpaServiceImpl sut;

    @Mock
    private ConversionService conversionService;

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
    void testConvertToPojo() {
        when(conversionService.convert(any(ProductCategory.class), eq(ProductCategoryPojo.class))).thenReturn(productCategoryPojo);
        ProductCategoryPojo actual = sut.convertToPojo(productCategory);
        verify(conversionService, times(1)).convert(any(ProductCategory.class), eq(ProductCategoryPojo.class));
    }

    @Test
    void testConvertToNewEntity() {
        ProductCategory actual = sut.convertToNewEntity(productCategoryPojo);
        assertEquals(ANY, actual.getCode());
        assertEquals(ANY, actual.getName());
    }
}
