package org.trebol.jpa.services.crud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.jpa.repositories.IProductsJpaRepository;
import org.trebol.jpa.services.GenericCrudJpaService;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.pojo.ProductPojo;

import javax.validation.Validator;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductsJpaCrudServiceTest {

  @Mock IProductsJpaRepository productsRepositoryMock;
  @Mock ITwoWayConverterJpaService<ProductPojo, Product> productsConverterMock;
  @Mock IImagesJpaRepository imagesRepositoryMock;
  @Mock IProductImagesJpaRepository productImagesRepositoryMock;
  @Mock GenericCrudJpaService<ImagePojo, Image> imagesCrudServiceMock;
  @Mock GenericCrudJpaService<ProductCategoryPojo, ProductCategory> categoriesCrudServiceMock;
  @Mock ITwoWayConverterJpaService<ProductCategoryPojo, ProductCategory> categoriesConverterMock;
  @Mock ITwoWayConverterJpaService<ImagePojo, Image> imagesConverterMock;
  @Mock Validator validatorMock;

  private final Long productId = 1L;
  private final String productName = "test product name";
  private final String productBarcode = "TESTPROD1";
  private final String productDescription = "test product description";
  private final Integer productPrice = 100;
  private final Integer productStock = 10;
  private final Integer productStockCritical = 1;

  @Test
  public void sanity_check() {
    ProductsJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_barcode() throws BadInputException {
    ProductCategory productCategory = null;
    ProductPojo example = new ProductPojo(productBarcode);
    Product persistedEntity = new Product(productId, productName, productBarcode, productDescription, productPrice,
        productStock, productStockCritical, productCategory);
    when(productsRepositoryMock.findByBarcode(productBarcode)).thenReturn(Optional.of(persistedEntity));
    ProductsJpaCrudServiceImpl service = instantiate();

    Optional<Product> match = service.getExisting(example);

    assertTrue(match.isPresent());
    assertEquals(match.get().getId(), productId);
    assertEquals(match.get().getName(), productName);
    assertEquals(match.get().getBarcode(), productBarcode);
    assertEquals(match.get().getDescription(), productDescription);
    assertEquals(match.get().getPrice(), productPrice);
    assertEquals(match.get().getStockCurrent(), productStock);
    assertEquals(match.get().getStockCritical(), productStockCritical);
    assertEquals(match.get().getProductCategory(), productCategory);
  }

  @Test
  public void creates_product() throws BadInputException, EntityAlreadyExistsException {
    ProductCategoryPojo productCategoryPojo = null;
    Collection<ImagePojo> images = List.of();
    ProductPojo newPojo = new ProductPojo(productName, productBarcode, productPrice, productCategoryPojo,
        productDescription, productStock, productStockCritical, images);
    Product newEntity = new Product(productName, productBarcode, productDescription, productPrice,
        productStock, productStockCritical);
    Product persistedEntity = new Product(productId, productName, productBarcode, productDescription, productPrice,
        productStock, productStockCritical, null);
    ProductPojo persistedPojo = new ProductPojo(productId, productName, productBarcode, productPrice, productCategoryPojo,
        productDescription, productStock, productStockCritical, images);
    when(productsConverterMock.convertToNewEntity(newPojo)).thenReturn(newEntity);
    when(productsRepositoryMock.saveAndFlush(newEntity)).thenReturn(persistedEntity);
    when(productsConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(newPojo);

    assertNotNull(result);
    assertEquals(result.getBarcode(), productBarcode);
    assertEquals(result.getName(), productName);
    assertEquals(result.getPrice(), productPrice);
    assertEquals(result.getDescription(), productDescription);
    assertEquals(result.getCurrentStock(), productStock);
    assertEquals(result.getCriticalStock(), productStockCritical);
    assertNotNull(result.getImages());
    assertTrue(result.getImages().isEmpty());
    assertNull(result.getCategory());
    verify(productsConverterMock).convertToNewEntity(newPojo);
    verify(productsRepositoryMock).saveAndFlush(newEntity);
    verify(productsConverterMock).convertToPojo(persistedEntity);
  }

  @Test
  public void creates_product_with_image() throws BadInputException, EntityAlreadyExistsException {
    Long imageId = 1L;
    String imageCode = "test-img";
    String imageFilename = "testimg.jpg";
    String imageUrl = "http://example.com/img/testimg.jpg";
    ImagePojo newImagePojo = new ImagePojo(imageCode, imageFilename, imageUrl);
    Image persistedImageEntity = new Image(imageId, imageCode, imageFilename, imageUrl);
    ImagePojo persistedImagePojo = new ImagePojo(imageId, imageCode, imageFilename, imageUrl);
    Collection<ImagePojo> newImages = List.of(newImagePojo);
    ProductPojo newPojo = new ProductPojo(productName, productBarcode, productPrice, null,
        productDescription, productStock, productStockCritical, newImages);
    Product newEntity = new Product(productName, productBarcode, productDescription, productPrice,
        productStock, productStockCritical);
    Product persistedEntity = new Product(productId, productName, productBarcode, productDescription, productPrice,
        productStock, productStockCritical, null);
    ProductPojo persistedPojo = new ProductPojo(productId, productName, productBarcode, productPrice, null,
        productDescription, productStock, productStockCritical, List.of(persistedImagePojo));
    when(productsConverterMock.convertToNewEntity(newPojo)).thenReturn(newEntity);
    when(productsRepositoryMock.saveAndFlush(newEntity)).thenReturn(persistedEntity);
    when(productsConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);
    when(productsRepositoryMock.getOne(productId)).thenReturn(persistedEntity);
    when(validatorMock.validate(newImagePojo)).thenReturn(Set.of());
    when(imagesCrudServiceMock.create(newImagePojo)).thenReturn(persistedImagePojo);
    when(imagesRepositoryMock.getOne(imageId)).thenReturn(persistedImageEntity);
    when(productImagesRepositoryMock.saveAll(any())).thenReturn(List.of()); // this callback value is not used
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(newPojo);

    assertNotNull(result);
    assertNotNull(result.getImages());
    assertFalse(result.getImages().isEmpty());
    ImagePojo resultImagePojo = result.getImages().iterator().next();
    assertEquals(resultImagePojo.getId(), imageId);
    assertEquals(resultImagePojo.getCode(), imageCode);
    assertEquals(resultImagePojo.getFilename(), imageFilename);
    assertEquals(resultImagePojo.getUrl(), imageUrl);
    verify(productsConverterMock).convertToNewEntity(newPojo);
    verify(productsRepositoryMock).saveAndFlush(newEntity);
    verify(productsConverterMock).convertToPojo(persistedEntity);
    verify(productsRepositoryMock).getOne(productId);
    verify(validatorMock).validate(newImagePojo);
    verify(imagesCrudServiceMock).create(newImagePojo);
    verify(imagesRepositoryMock).getOne(imageId);
  }

  @Test
  public void creates_product_with_category() throws BadInputException, EntityAlreadyExistsException {
    Long categoryId = 1L;
    String categoryCode = "TESTCAT";
    String categoryName = "test category name";
    ProductCategoryPojo newCategoryPojo = new ProductCategoryPojo(categoryCode, categoryName, null);
    ProductCategory newCategoryEntity = new ProductCategory(categoryCode, categoryName, null);
    ProductCategoryPojo persistedCategoryPojo = new ProductCategoryPojo(categoryId, categoryCode, categoryName, null);
    Collection<ImagePojo> images = List.of();
    ProductPojo newPojo = new ProductPojo(productName, productBarcode, productPrice, newCategoryPojo,
        productDescription, productStock, productStockCritical, images);
    Product newEntity = new Product(productName, productBarcode, productDescription, productPrice,
        productStock, productStockCritical);
    Product persistedEntity = new Product(productId, productName, productBarcode, productDescription, productPrice,
        productStock, productStockCritical, newCategoryEntity);
    ProductPojo persistedPojo = new ProductPojo(productId, productName, productBarcode, productPrice, newCategoryPojo,
        productDescription, productStock, productStockCritical, images);
    when(productsConverterMock.convertToNewEntity(newPojo)).thenReturn(newEntity);
    when(productsRepositoryMock.saveAndFlush(newEntity)).thenReturn(persistedEntity);
    when(productsConverterMock.convertToPojo(persistedEntity)).thenReturn(persistedPojo);
    when(validatorMock.validate(newCategoryPojo)).thenReturn(Set.of());
    when(categoriesCrudServiceMock.create(newCategoryPojo)).thenReturn(persistedCategoryPojo);
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(newPojo);

    assertNotNull(result);
    assertNotNull(result.getCategory());
    assertEquals(result.getCategory().getId(), categoryId);
    assertEquals(result.getCategory().getCode(), categoryCode);
    assertEquals(result.getCategory().getName(), categoryName);
    assertNull(result.getCategory().getParent());
    verify(productsConverterMock).convertToNewEntity(newPojo);
    verify(productsRepositoryMock).saveAndFlush(newEntity);
    verify(productsConverterMock).convertToPojo(persistedEntity);
    verify(validatorMock).validate(newCategoryPojo);
    verify(categoriesCrudServiceMock).create(newCategoryPojo);
    verify(productsRepositoryMock).setProductCategoryById(productId, categoryId);
  }

  private ProductsJpaCrudServiceImpl instantiate() {
    return new ProductsJpaCrudServiceImpl(
        productsRepositoryMock,
        productsConverterMock,
        imagesRepositoryMock,
        productImagesRepositoryMock,
        imagesCrudServiceMock,
        categoriesCrudServiceMock,
        categoriesConverterMock,
        imagesConverterMock,
        validatorMock
    );
  }

}
