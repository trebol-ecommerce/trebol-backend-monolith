/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services.conversion.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ImagePojo;
import org.trebol.api.models.ProductCategoryPojo;
import org.trebol.api.models.ProductPojo;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.repositories.ProductImagesRepository;
import org.trebol.jpa.services.conversion.ImagesConverterService;
import org.trebol.jpa.services.conversion.ProductCategoriesConverterService;
import org.trebol.testing.ProductsTestHelper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductsConverterServiceImplTest {
  @InjectMocks ProductsConverterServiceImpl instance;
  @Mock ProductImagesRepository productImagesRepositoryMock;
  @Mock ImagesConverterService imagesConverterServiceMock;
  @Mock ProductCategoriesConverterService productCategoriesConverterServiceMock;
  final ProductsTestHelper productsTestHelper = new ProductsTestHelper();

  @BeforeEach
  void beforeEach() {
    productsTestHelper.resetProducts();
  }

  @Test
  void convers_to_pojo() {
    Product input = productsTestHelper.productEntityAfterCreationWithoutCategory();
    input.setProductCategory(ProductCategory.builder().build());
    ProductCategoryPojo expectedProductCategory = ProductCategoryPojo.builder().build();
    List<ProductImage> existingImages = List.of(
      ProductImage.builder()
        .image(Image.builder().build())
        .product(input)
        .build()
    );
    ImagePojo expectedImagePojo = ImagePojo.builder().build();
    when(productImagesRepositoryMock.deepFindProductImagesByProductId(anyLong())).thenReturn(existingImages);
    when(imagesConverterServiceMock.convertToPojo(any(Image.class))).thenReturn(expectedImagePojo);
    when(productCategoriesConverterServiceMock.convertToPojo(any(ProductCategory.class))).thenReturn(expectedProductCategory);
    ProductPojo result = instance.convertToPojo(input);
    assertEquals(input.getName(), result.getName());
    assertEquals(input.getBarcode(), result.getBarcode());
    assertEquals(input.getPrice(), result.getPrice());
    assertEquals(input.getDescription(), result.getDescription());
    assertEquals(input.getStockCurrent(), result.getCurrentStock());
    assertEquals(input.getStockCritical(), result.getCriticalStock());
    assertNotNull(result.getCategory());
    assertEquals(expectedProductCategory, result.getCategory());
    assertNotNull(result.getImages());
    assertFalse(result.getImages().isEmpty());
    assertEquals(1, result.getImages().size());
    assertEquals(expectedImagePojo, result.getImages().iterator().next());
  }

  @Test
  void converts_to_new_entity() {
    ProductPojo input = productsTestHelper.productPojoBeforeCreationWithoutCategory();
    Product result = instance.convertToNewEntity(input);
    assertEquals(input.getName(), result.getName());
    assertEquals(input.getBarcode(), result.getBarcode());
    assertEquals(input.getPrice(), result.getPrice());
    assertEquals(input.getDescription(), result.getDescription());
    assertEquals(input.getCurrentStock(), result.getStockCurrent());
    assertEquals(input.getCriticalStock(), result.getStockCritical());
    assertNull(result.getProductCategory());
  }
}
