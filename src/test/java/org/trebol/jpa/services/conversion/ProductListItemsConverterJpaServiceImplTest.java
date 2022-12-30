package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.entities.ProductListItem;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductPojo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ProductListItemsConverterJpaServiceImplTest {
    @InjectMocks ProductListItemsConverterJpaServiceImpl sut;

    @Mock
    private ConversionService conversionService;

    @Mock
    private IProductImagesJpaRepository iProductImagesJpaRepository;

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
        final ProductImage productImage = new ProductImage();
        productImage.setImage(new Image());
        when(conversionService.convert(any(Product.class), eq(ProductPojo.class))).thenReturn(ProductPojo.builder().id(1L).build());
        when(iProductImagesJpaRepository.deepFindProductImagesByProductId(anyLong())).thenReturn(List.of(productImage));
        when(conversionService.convert(any(Image.class), eq(ImagePojo.class))).thenReturn(ImagePojo.builder().filename(ANY).build());

        ProductPojo actual = sut.convertToPojo(productListItem);

        assertNotNull(actual.getImages());
    }
}
