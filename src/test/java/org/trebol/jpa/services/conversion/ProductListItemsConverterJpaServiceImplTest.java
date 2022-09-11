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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestContants.ANY;

@ExtendWith(MockitoExtension.class)
class ProductListItemsConverterJpaServiceImplTest {

    @InjectMocks
    private ProductListItemsConverterJpaServiceImpl sut;

    @Mock
    private ConversionService conversionService;

    @Mock
    private IProductImagesJpaRepository iProductImagesJpaRepository;

    @Test
    void testApplyChangesToExistingEntity() {
        UnsupportedOperationException unsupportedOperationException = assertThrows(UnsupportedOperationException.class,
                () -> sut.convertToNewEntity(new ProductPojo()));
        assertEquals("Not implemented", unsupportedOperationException.getMessage());
    }
    @Test
    void testConvertToPojo() {
        final ProductListItem productListItem = new ProductListItem();
        final Product product = new Product();
        productListItem.setProduct(product);
        final ProductPojo productPojo = new ProductPojo();
        productPojo.setId(1L);
        final ProductImage productImage = new ProductImage();
        productImage.setImage(new Image());
        final ImagePojo imagePojo = new ImagePojo(ANY);
        when(conversionService.convert(any(Product.class), eq(ProductPojo.class))).thenReturn(productPojo);
        when(iProductImagesJpaRepository.deepFindProductImagesByProductId(anyLong())).thenReturn(List.of(productImage));
        when(conversionService.convert(any(Image.class), eq(ImagePojo.class))).thenReturn(imagePojo);

        ProductPojo actual = sut.convertToPojo(productListItem);

        assertNotNull(actual.getImages());
    }

    @Test
    void testConvertToNewEntity() {
        UnsupportedOperationException unsupportedOperationException = assertThrows(UnsupportedOperationException.class,
                () -> sut.applyChangesToExistingEntity(new ProductPojo(), new ProductListItem()));
        assertEquals("Not implemented", unsupportedOperationException.getMessage());
    }
}
