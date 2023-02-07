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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ProductsConverterServiceImplTest {
  @InjectMocks ProductsConverterServiceImpl instance;
  @Mock ProductImagesRepository productImagesRepositoryMock;
  @Mock ImagesConverterService imagesConverterServiceMock;
  @Mock ProductCategoriesConverterService productCategoriesConverterServiceMock;
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
    when(productImagesRepositoryMock.deepFindProductImagesByProductId(anyLong())).thenReturn(List.of(productImage));
    when(imagesConverterServiceMock.convertToPojo(any(Image.class))).thenReturn(ImagePojo.builder().build());
    when(productCategoriesConverterServiceMock.convertToPojo(any(ProductCategory.class))).thenReturn(ProductCategoryPojo.builder().build());

    ProductPojo actual = instance.convertToPojo(product);

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

    Product actual = instance.convertToNewEntity(productPojo);

    assertEquals(productPojo.getName(), actual.getName());
    assertEquals(productPojo.getBarcode(), actual.getBarcode());
    assertEquals(productPojo.getPrice(), actual.getPrice());
  }
}
