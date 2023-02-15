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

package org.trebol.jpa.services.crud.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ImagePojo;
import org.trebol.api.models.ProductCategoryPojo;
import org.trebol.api.models.ProductPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.ProductImagesRepository;
import org.trebol.jpa.repositories.ProductsRepository;
import org.trebol.jpa.services.conversion.ImagesConverterService;
import org.trebol.jpa.services.conversion.ProductCategoriesConverterService;
import org.trebol.jpa.services.conversion.ProductsConverterService;
import org.trebol.jpa.services.crud.ImagesCrudService;
import org.trebol.jpa.services.crud.ProductCategoriesCrudService;
import org.trebol.testing.ImagesTestHelper;
import org.trebol.testing.ProductCategoriesTestHelper;
import org.trebol.testing.ProductsTestHelper;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductsCrudServiceImplTest {
  @InjectMocks ProductsCrudServiceImpl instance;
  @Mock ProductsRepository productsRepositoryMock;
  @Mock ProductsConverterService productsConverterMock;
  @Mock ProductImagesRepository productImagesRepositoryMock;
  @Mock ImagesCrudService imagesCrudServiceMock;
  @Mock ProductCategoriesCrudService categoriesCrudServiceMock;
  @Mock ProductCategoriesConverterService categoriesConverterMock;
  @Mock ImagesConverterService imagesConverterMock;
  final ProductsTestHelper productsHelper = new ProductsTestHelper();
  final ProductCategoriesTestHelper categoriesHelper = new ProductCategoriesTestHelper();
  final ImagesTestHelper imagesHelper = new ImagesTestHelper();

  @BeforeEach
  void beforeEach() {
    productsHelper.resetProducts();
    categoriesHelper.resetProductCategories();
    imagesHelper.resetImages();
  }

  @Test
  void finds_by_barcode()
    throws BadInputException {
    ProductPojo input = productsHelper.productPojoForFetch();
    Product expectedResult = productsHelper.productEntityAfterCreationWithoutCategory();
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<Product> match = instance.getExisting(input);

    verify(productsRepositoryMock).findByBarcode(input.getBarcode());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }

  @Test
  void creates_product()
    throws BadInputException, EntityExistsException {
    ProductPojo input = productsHelper.productPojoBeforeCreationWithoutCategory();
    ProductPojo expectedResult = productsHelper.productPojoAfterCreationWithoutCategory();
    Product inputEntity = productsHelper.productEntityBeforeCreationWithoutCategory();
    Product resultEntity = productsHelper.productEntityAfterCreationWithoutCategory();
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(inputEntity);
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(resultEntity);
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(inputEntity);
    assertNotNull(result);
    assertEquals(expectedResult, result);
    assertNull(result.getImages());
    assertNull(result.getCategory());
  }

  @Test
  void creates_product_with_nonexistent_image()
    throws BadInputException {
    ProductPojo input = productsHelper.productPojoBeforeCreationWithoutCategory();
    input.setImages(List.of(imagesHelper.imagePojoBeforeCreation()));
    Product inputEntity = productsHelper.productEntityBeforeCreationWithoutCategory();
    Product resultEntity = productsHelper.productEntityAfterCreationWithoutCategory();
    ProductPojo expectedResult = productsHelper.productPojoAfterCreationWithoutCategory();
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(inputEntity);
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(resultEntity);
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(imagesCrudServiceMock.getExisting(any(ImagePojo.class))).thenReturn(Optional.empty());
    when(productImagesRepositoryMock.saveAll(anyCollection())).thenReturn(List.of()); // unused value, stubbed for safety

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(inputEntity);
    assertNotNull(result);
    assertNotNull(result.getImages());
    assertTrue(result.getImages().isEmpty());
  }

  @Test
  void creates_product_with_existing_image()
    throws BadInputException {
    ProductPojo input = productsHelper.productPojoBeforeCreationWithoutCategory();
    input.setImages(List.of(imagesHelper.imagePojoBeforeCreation()));
    ProductPojo expectedResult = productsHelper.productPojoAfterCreationWithoutCategory();
    ImagePojo expectedResultImage = imagesHelper.imagePojoAfterCreation();
    expectedResult.setImages(List.of(expectedResultImage));
    Product inputEntity = productsHelper.productEntityBeforeCreationWithoutCategory();
    Product resultEntity = productsHelper.productEntityAfterCreationWithoutCategory();
    Image imageEntity = imagesHelper.imageEntityAfterCreation();
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(inputEntity);
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(resultEntity);
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(imagesCrudServiceMock.getExisting(any(ImagePojo.class))).thenReturn(Optional.of(imageEntity));
    when(productImagesRepositoryMock.saveAll(anyCollection())).thenReturn(List.of()); // unused value, stubbed for safety
    when(imagesConverterMock.convertToPojo(any(Image.class))).thenReturn(expectedResultImage);

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(inputEntity);
    assertNotNull(result);
    assertFalse(result.getImages().isEmpty());
    ImagePojo resultImage = result.getImages().iterator().next();
    assertEquals(expectedResultImage, resultImage);
    assertEquals(expectedResult, result);
  }

  @Test
  void creates_product_with_nonexistent_category()
    throws BadInputException {
    ProductPojo input = productsHelper.productPojoBeforeCreationWithoutCategory();
    input.setCategory(categoriesHelper.productCategoryPojoBeforeCreation());
    ProductPojo expectedResult = productsHelper.productPojoAfterCreationWithoutCategory();
    Product inputEntity = productsHelper.productEntityBeforeCreationWithoutCategory();
    Product resultEntity = productsHelper.productEntityAfterCreationWithoutCategory();
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(inputEntity);
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(resultEntity);
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(categoriesCrudServiceMock.getExisting(any(ProductCategoryPojo.class))).thenReturn(Optional.empty());

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(inputEntity);
    verify(categoriesCrudServiceMock).getExisting(input.getCategory());
    assertNotNull(result);
    assertNull(result.getCategory());
    assertEquals(expectedResult, result);
  }

  @Test
  void creates_product_with_existing_category()
    throws BadInputException {
    ProductPojo input = productsHelper.productPojoBeforeCreationWithoutCategory();
    input.setCategory(categoriesHelper.productCategoryPojoBeforeCreation());
    ProductPojo expectedResult = productsHelper.productPojoAfterCreationWithoutCategory();
    ProductCategoryPojo expectedResultCategory = categoriesHelper.productCategoryPojoAfterCreation();
    expectedResult.setCategory(expectedResultCategory);
    Product inputEntity = productsHelper.productEntityBeforeCreationWithoutCategory();
    Product resultEntity = productsHelper.productEntityAfterCreationWithoutCategory();
    ProductCategory categoryEntity = categoriesHelper.productCategoryEntityAfterCreation();
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(inputEntity);
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(resultEntity);
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(categoriesCrudServiceMock.getExisting(any(ProductCategoryPojo.class))).thenReturn(Optional.of(categoryEntity));
    when(categoriesConverterMock.convertToPojo(any(ProductCategory.class))).thenReturn(expectedResultCategory);

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(inputEntity);
    verify(categoriesCrudServiceMock).getExisting(input.getCategory());
    assertNotNull(result);
    assertNotNull(result.getCategory());
    assertEquals(expectedResultCategory, result.getCategory());
    assertEquals(expectedResult, result);
  }
}
