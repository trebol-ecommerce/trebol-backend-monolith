package org.trebol.jpa.services.conversion;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.ProductList;
import org.trebol.jpa.repositories.IProductListItemsJpaRepository;
import org.trebol.pojo.ProductListPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;
import static org.trebol.constant.TestConstants.ID_1L;

@ExtendWith(MockitoExtension.class)
class ProductListConverterJpaServiceImplTest {

    @InjectMocks
    private ProductListConverterJpaServiceImpl sut;

    @Mock
    private IProductListItemsJpaRepository productListItemRepository;

    private ProductList productList;
    private ProductListPojo productListPojo;

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
    @Test
    void testConvertToPojo() {
        Mockito.when(productListItemRepository.count(Mockito.any(Predicate.class))).thenReturn(ID_1L);
        ProductListPojo actual = sut.convertToPojo(productList);
        assertEquals(ANY, actual.getName());
    }

    @Test
    void testConvertToNewEntity() throws BadInputException {
        ProductList actual = sut.convertToNewEntity(productListPojo);
        assertEquals(ANY + " ", actual.getName());
    }
}
