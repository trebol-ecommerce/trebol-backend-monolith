package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.ProductCategoryPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestContants.ANY;
import static org.trebol.constant.TestContants.ID_1L;

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

        productCategoryPojo = new ProductCategoryPojo();
        productCategoryPojo.setId(ID_1L);
        productCategoryPojo.setName(ANY);
        productCategoryPojo.setCode(ANY);
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

    @DisplayName("Will test parent which is a private method...")
    @Test
    void testApplyParent() throws BadInputException {
        final ProductCategoryPojo parentPojo = new ProductCategoryPojo();
//        parentPojo.setCode(ANY);
        final ProductCategory parent = new ProductCategory();
        parent.setName(ANY);
        productCategory.setParent(parent);
        productCategory.setName(ANY);
        productCategoryPojo.setParent(parentPojo);
        productCategory.setId(1L);
        when(categoriesRepository.save(any(ProductCategory.class))).thenReturn(parent);
        ProductCategory actual = sut.applyChangesToExistingEntity(productCategoryPojo, productCategory);

        assertEquals(ANY, actual.getParent().getName());
    }

    @Test
    void testApplyParent2() throws BadInputException {
        final ProductCategoryPojo parentPojo = new ProductCategoryPojo();
        parentPojo.setCode(ANY);
        final ProductCategory parent = new ProductCategory();
        parent.setName(ANY);
        parent.setCode("");
        productCategory.setParent(parent);
        productCategory.setName(ANY);
        productCategoryPojo.setParent(parentPojo);
        productCategory.setId(1L);
        when(categoriesRepository.save(any(ProductCategory.class))).thenReturn(parent);
        ProductCategory actual = sut.applyChangesToExistingEntity(productCategoryPojo, productCategory);

        assertEquals(ANY, actual.getParent().getName());
    }
}
