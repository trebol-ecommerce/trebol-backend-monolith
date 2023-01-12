package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.pojo.ProductPojo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
public class ProductsConverterJpaServiceImplTest {
  @InjectMocks ProductsConverterJpaServiceImpl sut;
  @Mock IProductImagesJpaRepository productImagesRepository;
  @Mock IImagesConverterJpaService imagesConverterService;
  @Mock IProductCategoriesConverterJpaService productCategoriesConverterService;
  Product product;
  ProductPojo productPojo;

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

  @Test
  void testConvertToPojo() {
    final ProductCategory productCategory = new ProductCategory();
    final ProductImage productImage = new ProductImage();
    final Image image = new Image();
    productImage.setImage(image);
    product.setProductCategory(productCategory);
    when(productImagesRepository.deepFindProductImagesByProductId(anyLong())).thenReturn(List.of(productImage));
    when(imagesConverterService.convertToPojo(any(Image.class))).thenReturn(ImagePojo.builder().build());
    when(productCategoriesConverterService.convertToPojo(any(ProductCategory.class))).thenReturn(ProductCategoryPojo.builder().build());

    ProductPojo actual = sut.convertToPojo(product);

    assertEquals(product.getName(), actual.getName());
    assertEquals(product.getBarcode(), actual.getBarcode());
    assertNotNull(actual.getCategory());
    assertNotNull(actual.getImages());
    assertEquals(1, actual.getImages().size());
  }

  @Test
  void testConvertToNewEntity() {
    productPojo.setBarcode(ANY);
    productPojo.setPrice(1);

    Product actual = sut.convertToNewEntity(productPojo);

    assertEquals(productPojo.getName(), actual.getName());
    assertEquals(productPojo.getBarcode(), actual.getBarcode());
    assertEquals(productPojo.getPrice(), actual.getPrice());
  }
}
