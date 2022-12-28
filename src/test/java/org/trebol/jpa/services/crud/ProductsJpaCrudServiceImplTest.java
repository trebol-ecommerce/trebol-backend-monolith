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

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.ImagesTestHelper.*;
import static org.trebol.testhelpers.ProductCategoriesTestHelper.*;
import static org.trebol.testhelpers.ProductsTestHelper.*;

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

  @BeforeEach
  void beforeEach() {
    resetProducts();
    resetProductCategories();
    resetImages();
  }


  @Test
  void finds_by_barcode()
      throws BadInputException {
    ProductPojo input = productPojoForFetch();
    Product expectedResult = productEntityAfterCreation();
    when(productsRepositoryMock.findByBarcode(anyString())).thenReturn(Optional.of(expectedResult));

    Optional<Product> match = instance.getExisting(input);

    verify(productsRepositoryMock).findByBarcode(input.getBarcode());
    assertTrue(match.isPresent());
    assertEquals(expectedResult, match.get());
  }

  @Test
  void creates_product()
      throws BadInputException, EntityExistsException {
    ProductPojo input = productPojoBeforeCreation();
    ProductPojo expectedResult = productPojoAfterCreation();
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(productsRepositoryMock.getById(anyLong())).thenReturn(productEntityAfterCreation());

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productEntityBeforeCreation());
    assertNotNull(result);
    assertEquals(expectedResult, result);
    assertNull(result.getImages());
    assertNull(result.getCategory());
  }

  @Test
  void creates_product_with_nonexistent_image()
      throws BadInputException {
    ProductPojo input = productPojoBeforeCreation();
    input.setImages(List.of(imagePojoBeforeCreation()));
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(productPojoAfterCreation());
    when(productsRepositoryMock.getById(anyLong())).thenReturn(productEntityAfterCreation());
    when(imagesCrudServiceMock.getExisting(any(ImagePojo.class))).thenReturn(Optional.empty());
    when(productImagesRepositoryMock.saveAll(anyCollection())).thenReturn(List.of()); // unused value, stubbed for safety

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productEntityBeforeCreation());
    assertNotNull(result);
    assertNotNull(result.getImages());
    assertTrue(result.getImages().isEmpty());
  }

  @Test
  void creates_product_with_existing_image()
      throws BadInputException {
    ProductPojo input = productPojoBeforeCreation();
    input.setImages(List.of(imagePojoBeforeCreation()));
    ProductPojo expectedResult = productPojoAfterCreation();
    ImagePojo expectedResultImage = imagePojoAfterCreation();
    expectedResult.setImages(List.of(expectedResultImage));
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(productsRepositoryMock.getById(anyLong())).thenReturn(productEntityAfterCreation());
    when(imagesCrudServiceMock.getExisting(any(ImagePojo.class))).thenReturn(Optional.of(imageEntityAfterCreation()));
    when(productImagesRepositoryMock.saveAll(anyCollection())).thenReturn(List.of()); // unused value, stubbed for safety
    when(imagesConverterMock.convertToPojo(any(Image.class))).thenReturn(expectedResultImage);

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productEntityBeforeCreation());
    assertNotNull(result);
    assertFalse(result.getImages().isEmpty());
    ImagePojo resultImage = result.getImages().iterator().next();
    assertEquals(expectedResultImage, resultImage);
    assertEquals(expectedResult, result);
  }

  @Test
  void creates_product_with_nonexistent_category()
      throws BadInputException {
    ProductPojo input = productPojoBeforeCreation();
    input.setCategory(productCategoryPojoBeforeCreation());
    ProductPojo expectedResult = productPojoAfterCreation();
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(productsRepositoryMock.getById(any(Product.class).getId())).thenReturn(productEntityAfterCreation());
    when(categoriesCrudServiceMock.getExisting(any(ProductCategoryPojo.class))).thenReturn(Optional.empty());

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productEntityBeforeCreation());
    assertNotNull(result);
    assertNull(result.getCategory());
    assertEquals(expectedResult, result);
  }

  @Test
  void creates_product_with_existing_category()
      throws BadInputException {
    ProductPojo input = productPojoBeforeCreation();
    input.setCategory(productCategoryPojoBeforeCreation());
    ProductPojo expectedResult = productPojoAfterCreation();
    ProductCategoryPojo expectedResultCategory = productCategoryPojoAfterCreation();
    expectedResult.setCategory(expectedResultCategory);
    when(productsConverterMock.convertToNewEntity(any(ProductPojo.class))).thenReturn(productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(any(Product.class))).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(any(Product.class))).thenReturn(expectedResult);
    when(productsRepositoryMock.getById(any(Product.class).getId())).thenReturn(productEntityAfterCreation());
    when(categoriesCrudServiceMock.getExisting(any(ProductCategoryPojo.class))).thenReturn(Optional.of(productCategoryEntityAfterCreation()));
    when(categoriesConverterMock.convertToPojo(any(ProductCategory.class))).thenReturn(expectedResultCategory);

    ProductPojo result = instance.create(input);

    verify(productsRepositoryMock).saveAndFlush(productEntityBeforeCreation());
    assertNotNull(result);
    ProductCategoryPojo resultCategory = result.getCategory();
    assertNotNull(resultCategory);
    assertEquals(expectedResultCategory, resultCategory);
    assertEquals(expectedResult, result);
  }
}
