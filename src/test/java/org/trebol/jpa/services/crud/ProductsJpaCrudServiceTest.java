package org.trebol.jpa.services.crud;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testhelpers.ImagesTestHelper.*;
import static org.trebol.testhelpers.ProductCategoriesTestHelper.*;
import static org.trebol.testhelpers.ProductsTestHelper.*;

@ExtendWith(MockitoExtension.class)
public class ProductsJpaCrudServiceTest {

  @Mock
  IProductsJpaRepository productsRepositoryMock;
  @Mock
  ITwoWayConverterJpaService<ProductPojo, Product> productsConverterMock;
  @Mock
  IProductImagesJpaRepository productImagesRepositoryMock;
  @Mock
  GenericCrudJpaService<ImagePojo, Image> imagesCrudServiceMock;
  @Mock
  GenericCrudJpaService<ProductCategoryPojo, ProductCategory> categoriesCrudServiceMock;
  @Mock
  ITwoWayConverterJpaService<ProductCategoryPojo, ProductCategory> categoriesConverterMock;
  @Mock
  ITwoWayConverterJpaService<ImagePojo, Image> imagesConverterMock;

  @Test
  public void sanity_check() {
    ProductsJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_barcode()
      throws BadInputException {
    resetProducts();
    when(productsRepositoryMock.findByBarcode(productPojoForFetch().getBarcode())).thenReturn(
        Optional.of(productEntityAfterCreation()));
    ProductsJpaCrudServiceImpl service = instantiate();

    Optional<Product> match = service.getExisting(productPojoForFetch());

    assertTrue(match.isPresent());
    verify(productsRepositoryMock).findByBarcode(productPojoForFetch().getBarcode());
    assertEquals(match.get().getId(), productEntityAfterCreation().getId());
    assertEquals(match.get().getName(), productEntityAfterCreation().getName());
    assertEquals(match.get().getBarcode(), productEntityAfterCreation().getBarcode());
    assertEquals(match.get().getDescription(), productEntityAfterCreation().getDescription());
    assertEquals(match.get().getPrice(), productEntityAfterCreation().getPrice());
    assertEquals(match.get().getStockCurrent(), productEntityAfterCreation().getStockCurrent());
    assertEquals(match.get().getStockCritical(), productEntityAfterCreation().getStockCritical());
    assertEquals(match.get().getProductCategory(), productEntityAfterCreation().getProductCategory());
  }

  @Test
  public void creates_product()
      throws BadInputException, EntityExistsException {
    resetProducts();

    when(productsConverterMock.convertToNewEntity(productPojoBeforeCreation())).thenReturn(
        productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(productEntityBeforeCreation())).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());
    when(productsRepositoryMock.getById(productEntityAfterCreation().getId())).thenReturn(productEntityAfterCreation());
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(productPojoBeforeCreation());

    assertNotNull(result);
    verify(productsConverterMock).convertToNewEntity(productPojoBeforeCreation());
    verify(productsRepositoryMock).saveAndFlush(productEntityBeforeCreation());
    verify(productsConverterMock).convertToPojo(productEntityAfterCreation());
    verify(productsRepositoryMock).getById(productEntityAfterCreation().getId());
    assertEquals(result.getBarcode(), productPojoAfterCreation().getBarcode());
    assertEquals(result.getName(), productPojoAfterCreation().getName());
    assertEquals(result.getPrice(), productPojoAfterCreation().getPrice());
    assertEquals(result.getDescription(), productPojoAfterCreation().getDescription());
    assertEquals(result.getCurrentStock(), productPojoAfterCreation().getCurrentStock());
    assertEquals(result.getCriticalStock(), productPojoAfterCreation().getCriticalStock());
    assertNotNull(result.getImages());
    assertTrue(result.getImages().isEmpty());
    assertNull(result.getCategory());
  }

  @Test
  public void creates_product_with_nonexistent_image()
      throws BadInputException {
    resetProducts();
    resetImages();

    productPojoBeforeCreation().setImages(List.of(imagePojoBeforeCreation()));
    when(productsConverterMock.convertToNewEntity(productPojoBeforeCreation())).thenReturn(
        productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(productEntityBeforeCreation())).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());
    when(productsRepositoryMock.getById(productPojoAfterCreation().getId())).thenReturn(productEntityAfterCreation());
    when(imagesCrudServiceMock.getExisting(imagePojoBeforeCreation())).thenReturn(Optional.empty());
    when(productImagesRepositoryMock.saveAll(any())).thenReturn(List.of()); // unused value, stubbed for safety
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(productPojoBeforeCreation());

    assertNotNull(result);
    assertNotNull(result.getImages());
    assertTrue(result.getImages().isEmpty());
    verify(imagesCrudServiceMock).getExisting(imagePojoBeforeCreation());
    verify(productImagesRepositoryMock).saveAll(any());
  }

  @Test
  public void creates_product_with_existing_image()
      throws BadInputException {
    resetProducts();
    resetImages();

    productPojoBeforeCreation().setImages(List.of(imagePojoBeforeCreation()));
    productPojoAfterCreation().setImages(List.of(imagePojoAfterCreation()));
    when(productsConverterMock.convertToNewEntity(productPojoBeforeCreation())).thenReturn(
        productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(productEntityBeforeCreation())).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());
    when(productsRepositoryMock.getById(productPojoAfterCreation().getId())).thenReturn(productEntityAfterCreation());
    when(imagesCrudServiceMock.getExisting(imagePojoBeforeCreation())).thenReturn(
        Optional.of(imageEntityAfterCreation()));
    when(productImagesRepositoryMock.saveAll(any())).thenReturn(List.of()); // unused value, stubbed for safety
    when(imagesConverterMock.convertToPojo(imageEntityAfterCreation())).thenReturn(imagePojoAfterCreation());
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(productPojoBeforeCreation());

    assertFalse(result.getImages().isEmpty());
    ImagePojo resultImage = result.getImages().iterator().next();
    assertEquals(resultImage.getFilename(), imagePojoAfterCreation().getFilename());
    assertEquals(resultImage.getUrl(), imagePojoAfterCreation().getUrl());
    verify(imagesCrudServiceMock).getExisting(imagePojoBeforeCreation());
    verify(productImagesRepositoryMock).saveAll(any());
  }

  @Test
  public void creates_product_with_nonexistent_category()
      throws BadInputException {
    resetProducts();
    resetProductCategories();

    productPojoBeforeCreation().setCategory(productCategoryPojoBeforeCreation());
    when(productsConverterMock.convertToNewEntity(productPojoBeforeCreation())).thenReturn(
        productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(productEntityBeforeCreation())).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());
    when(productsRepositoryMock.getById(productEntityAfterCreation().getId())).thenReturn(productEntityAfterCreation());
    when(categoriesCrudServiceMock.getExisting(productCategoryPojoBeforeCreation())).thenReturn(Optional.empty());
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(productPojoBeforeCreation());

    assertNotNull(result);
    assertNull(result.getCategory());
    verify(categoriesCrudServiceMock).getExisting(productCategoryPojoBeforeCreation());
  }

  @Test
  public void creates_product_with_existing_category()
      throws BadInputException {
    resetProducts();
    resetProductCategories();

    productPojoBeforeCreation().setCategory(productCategoryPojoBeforeCreation());
    productPojoAfterCreation().setCategory(productCategoryPojoAfterCreation());
    when(productsConverterMock.convertToNewEntity(productPojoBeforeCreation())).thenReturn(
        productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(productEntityBeforeCreation())).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());
    when(productsRepositoryMock.getById(productEntityAfterCreation().getId())).thenReturn(productEntityAfterCreation());
    when(categoriesCrudServiceMock.getExisting(productCategoryPojoBeforeCreation())).thenReturn(
        Optional.of(productCategoryEntityAfterCreation()));
    when(categoriesConverterMock.convertToPojo(productCategoryEntityAfterCreation())).thenReturn(
        productCategoryPojoAfterCreation());
    ProductsJpaCrudServiceImpl service = this.instantiate();


    ProductPojo result = service.create(productPojoBeforeCreation());

    ProductCategoryPojo resultCategory = result.getCategory();
    assertNotNull(resultCategory);
    assertEquals(resultCategory.getId(), productCategoryPojoAfterCreation().getId());
    assertEquals(resultCategory.getCode(), productCategoryPojoAfterCreation().getCode());
    assertEquals(resultCategory.getName(), productCategoryPojoAfterCreation().getName());
    verify(categoriesCrudServiceMock).getExisting(productCategoryPojoBeforeCreation());
  }

  private ProductsJpaCrudServiceImpl instantiate() {
    return new ProductsJpaCrudServiceImpl(
        productsRepositoryMock,
        productsConverterMock,
        productImagesRepositoryMock,
        imagesCrudServiceMock,
        categoriesCrudServiceMock,
        categoriesConverterMock,
        imagesConverterMock
    );
  }

}
