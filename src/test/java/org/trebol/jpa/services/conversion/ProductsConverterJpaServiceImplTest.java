package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.exceptions.BadInputException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
public class ProductsConverterJpaServiceImplTest {

  @InjectMocks
  private ProductsConverterJpaServiceImpl sut;

  @Mock
  private ConversionService conversionService;

  @Mock
  private IProductImagesJpaRepository productImagesRepository;

  private Product product;
  private ProductPojo productPojo;

  @BeforeEach
  void beforeEach() {
    product = new Product();
    product.setName(ANY);
    product.setId(1L);
    productPojo = new ProductPojo();
    productPojo.setId(1L);
    productPojo.setName(ANY);
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

  @Test
  void testConvertToPojo() {
    when(conversionService.convert(any(Product.class), eq(ProductPojo.class))).thenReturn(productPojo);
    final ProductImage productImage = new ProductImage();
    final ImagePojo imagePojo = new ImagePojo();
    final Image image = new Image();
    productImage.setImage(image);
    when(productImagesRepository.deepFindProductImagesByProductId(anyLong())).thenReturn(List.of(productImage));
    when(conversionService.convert(any(Image.class), eq(ImagePojo.class))).thenReturn(imagePojo);
    final ProductCategory productCategory = new ProductCategory();
    product.setProductCategory(productCategory);
    when(conversionService.convert(any(ProductCategory.class), eq(ProductCategoryPojo.class))).thenReturn(new ProductCategoryPojo());

    ProductPojo actual = sut.convertToPojo(product);


    assertEquals(ANY, actual.getName());
    verify(conversionService, times(1)).convert(any(Product.class), eq(ProductPojo.class));
  }

  @Test
  void testConvertToNewEntity() throws BadInputException {
    when(conversionService.convert(any(ProductPojo.class), eq(Product.class))).thenReturn(product);
    Product actual = sut.convertToNewEntity(productPojo);
    assertEquals(ANY, actual.getName());
    verify(conversionService, times(1)).convert(any(ProductPojo.class), eq(Product.class));
  }
}
