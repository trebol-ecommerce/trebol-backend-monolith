package org.trebol.jpa.services.crud;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.jpa.repositories.IProductsJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.pojo.ProductPojo;
import org.trebol.testhelpers.ProductsTestHelper;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.ImagesTestHelper.*;
import static org.trebol.testhelpers.ProductCategoriesTestHelper.*;

@ExtendWith(MockitoExtension.class)
class ProductsJpaCrudServiceImplTest {
  @InjectMocks ProductsJpaCrudServiceImpl instance;
  @Mock IProductsJpaRepository productsRepositoryMock;
  @Mock ITwoWayConverterJpaService<ProductPojo, Product> productsConverterMock;
  @Mock IProductImagesJpaRepository productImagesRepositoryMock;
  @Mock GenericCrudJpaService<ImagePojo, Image> imagesCrudServiceMock;
  @Mock GenericCrudJpaService<ProductCategoryPojo, ProductCategory> categoriesCrudServiceMock;
  @Mock ITwoWayConverterJpaService<ProductCategoryPojo, ProductCategory> categoriesConverterMock;
  @Mock ITwoWayConverterJpaService<ImagePojo, Image> imagesConverterMock;
  ProductsTestHelper productsHelper = new ProductsTestHelper();

  @BeforeEach
  void beforeEach() {
    productsHelper.resetProducts();
    resetProductCategories();
    resetImages();
  }


  @Test
  void finds_by_barcode()
      throws BadInputException {
    ProductPojo input = productsHelper.productPojoForFetch();
    Product expectedResult = productsHelper.productEntityAfterCreation();
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<Product> match = instance.getExisting(input);

    verify(productsRepositoryMock).findByBarcode(input.getBarcode());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }

  @Test
  void creates_product()
      throws BadInputException, EntityExistsException {
    ProductPojo input = productsHelper.productPojoBeforeCreation();
    ProductPojo expectedResult = productsHelper.productPojoAfterCreation();
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productsHelper.productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productsHelper.productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(productsRepositoryMock.getById(anyLong())).thenReturn(productsHelper.productEntityAfterCreation());

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productsHelper.productEntityBeforeCreation());
    assertNotNull(result);
    assertEquals(expectedResult, result);
    assertNull(result.getImages());
    assertNull(result.getCategory());
  }

  @Test
  void creates_product_with_nonexistent_image()
      throws BadInputException {
    ProductPojo input = productsHelper.productPojoBeforeCreation();
    input.setImages(List.of(imagePojoBeforeCreation()));
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productsHelper.productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productsHelper.productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(productsHelper.productPojoAfterCreation());
    when(productsRepositoryMock.getById(anyLong())).thenReturn(productsHelper.productEntityAfterCreation());
    when(imagesCrudServiceMock.getExisting(any(ImagePojo.class))).thenReturn(Optional.empty());
    when(productImagesRepositoryMock.saveAll(anyCollection())).thenReturn(List.of()); // unused value, stubbed for safety

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productsHelper.productEntityBeforeCreation());
    assertNotNull(result);
    assertNotNull(result.getImages());
    assertTrue(result.getImages().isEmpty());
  }

  @Test
  void creates_product_with_existing_image()
      throws BadInputException {
    ProductPojo input = productsHelper.productPojoBeforeCreation();
    input.setImages(List.of(imagePojoBeforeCreation()));
    ProductPojo expectedResult = productsHelper.productPojoAfterCreation();
    ImagePojo expectedResultImage = imagePojoAfterCreation();
    expectedResult.setImages(List.of(expectedResultImage));
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productsHelper.productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productsHelper.productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(productsRepositoryMock.getById(anyLong())).thenReturn(productsHelper.productEntityAfterCreation());
    when(imagesCrudServiceMock.getExisting(any(ImagePojo.class))).thenReturn(Optional.of(imageEntityAfterCreation()));
    when(productImagesRepositoryMock.saveAll(anyCollection())).thenReturn(List.of()); // unused value, stubbed for safety
    when(imagesConverterMock.convertToPojo(any(Image.class))).thenReturn(expectedResultImage);

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productsHelper.productEntityBeforeCreation());
    assertNotNull(result);
    assertFalse(result.getImages().isEmpty());
    ImagePojo resultImage = result.getImages().iterator().next();
    assertEquals(expectedResultImage, resultImage);
    assertEquals(expectedResult, result);
  }

  @Test
  void creates_product_with_nonexistent_category()
      throws BadInputException {
    ProductPojo input = productsHelper.productPojoBeforeCreation();
    input.setCategory(productCategoryPojoBeforeCreation());
    ProductPojo expectedResult = productsHelper.productPojoAfterCreation();
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productsHelper.productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productsHelper.productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(productsRepositoryMock.getById(any(Product.class).getId())).thenReturn(productsHelper.productEntityAfterCreation());
    when(categoriesCrudServiceMock.getExisting(any(ProductCategoryPojo.class))).thenReturn(Optional.empty());

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productsHelper.productEntityBeforeCreation());
    assertNotNull(result);
    assertNull(result.getCategory());
    assertEquals(expectedResult, result);
  }

  @Test
  void creates_product_with_existing_category()
      throws BadInputException {
    ProductPojo input = productsHelper.productPojoBeforeCreation();
    input.setCategory(productCategoryPojoBeforeCreation());
    ProductPojo expectedResult = productsHelper.productPojoAfterCreation();
    ProductCategoryPojo expectedResultCategory = productCategoryPojoAfterCreation();
    expectedResult.setCategory(expectedResultCategory);
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productsHelper.productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productsHelper.productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(productsRepositoryMock.getById(any(Product.class).getId())).thenReturn(productsHelper.productEntityAfterCreation());
    when(categoriesCrudServiceMock.getExisting(any(ProductCategoryPojo.class))).thenReturn(Optional.of(productCategoryEntityAfterCreation()));
    when(categoriesConverterMock.convertToPojo(any(ProductCategory.class))).thenReturn(expectedResultCategory);

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productsHelper.productEntityBeforeCreation());
    assertNotNull(result);
    ProductCategoryPojo resultCategory = result.getCategory();
    assertNotNull(resultCategory);
    assertEquals(expectedResultCategory, resultCategory);
    assertEquals(expectedResult, result);
  }
}
