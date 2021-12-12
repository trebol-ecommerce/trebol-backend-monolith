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
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.jpa.testhelpers.ImagesJpaCrudServiceTestHelper.*;
import static org.trebol.jpa.testhelpers.ProductCategoriesJpaCrudServiceTestHelper.productCategoryPojoAfterCreation;
import static org.trebol.jpa.testhelpers.ProductCategoriesJpaCrudServiceTestHelper.productCategoryPojoBeforeCreation;
import static org.trebol.jpa.testhelpers.ProductsJpaCrudServiceTestHelper.*;

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

  @Test
  public void sanity_check() {
    ProductsJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_barcode() throws BadInputException {
    resetProducts();
    when(productsRepositoryMock.findByBarcode(productPojoForFetch().getBarcode())).thenReturn(Optional.of(productEntityAfterCreation()));
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
  public void creates_product() throws BadInputException, EntityAlreadyExistsException {
    resetProducts();
    when(productsConverterMock.convertToNewEntity(productPojoBeforeCreation())).thenReturn(productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(productEntityBeforeCreation())).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(productPojoBeforeCreation());

    assertNotNull(result);
    verify(productsConverterMock).convertToNewEntity(productPojoBeforeCreation());
    verify(productsRepositoryMock).saveAndFlush(productEntityBeforeCreation());
    verify(productsConverterMock).convertToPojo(productEntityAfterCreation());
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
  public void creates_product_with_image() throws BadInputException, EntityAlreadyExistsException {
    resetProducts();
    productPojoBeforeCreation().setImages(List.of(imagePojoBeforeCreation()));
    productPojoAfterCreation().setImages(List.of(imagePojoAfterCreation()));
    when(productsConverterMock.convertToNewEntity(productPojoBeforeCreation())).thenReturn(productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(productEntityBeforeCreation())).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());
    when(productsRepositoryMock.getOne(productPojoAfterCreation().getId())).thenReturn(productEntityAfterCreation());
    when(validatorMock.validate(imagePojoBeforeCreation())).thenReturn(Set.of());
    when(imagesCrudServiceMock.create(imagePojoBeforeCreation())).thenReturn(imagePojoAfterCreation());
    when(imagesRepositoryMock.getOne(imagePojoAfterCreation().getId())).thenReturn(imageEntityAfterCreation());
    when(productImagesRepositoryMock.saveAll(any())).thenReturn(List.of()); // the resulting value is not used
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(productPojoBeforeCreation());

    assertNotNull(result);
    verify(validatorMock).validate(imagePojoBeforeCreation());
    verify(imagesCrudServiceMock).create(imagePojoBeforeCreation());
    verify(imagesRepositoryMock).getOne(imagePojoAfterCreation().getId());
    assertNotNull(result.getImages());
    assertFalse(result.getImages().isEmpty());
    ImagePojo resultImagePojo = result.getImages().iterator().next();
    assertEquals(resultImagePojo.getId(), imagePojoAfterCreation().getId());
    assertEquals(resultImagePojo.getCode(), imagePojoAfterCreation().getCode());
    assertEquals(resultImagePojo.getFilename(), imagePojoAfterCreation().getFilename());
    assertEquals(resultImagePojo.getUrl(), imagePojoAfterCreation().getUrl());
  }

  @Test
  public void creates_product_with_category() throws BadInputException, EntityAlreadyExistsException {
    resetProducts();
    productPojoBeforeCreation().setCategory(productCategoryPojoBeforeCreation());
    productPojoAfterCreation().setCategory(productCategoryPojoAfterCreation());
    when(productsConverterMock.convertToNewEntity(productPojoBeforeCreation())).thenReturn(productEntityBeforeCreation());
    when(productsRepositoryMock.saveAndFlush(productEntityBeforeCreation())).thenReturn(productEntityAfterCreation());
    when(productsConverterMock.convertToPojo(productEntityAfterCreation())).thenReturn(productPojoAfterCreation());
    when(validatorMock.validate(productCategoryPojoBeforeCreation())).thenReturn(Set.of());
    when(categoriesCrudServiceMock.create(productCategoryPojoBeforeCreation())).thenReturn(
        productCategoryPojoAfterCreation());
    ProductsJpaCrudServiceImpl service = this.instantiate();

    ProductPojo result = service.create(productPojoBeforeCreation());

    assertNotNull(result);
    verify(validatorMock).validate(productCategoryPojoBeforeCreation());
    verify(categoriesCrudServiceMock).create(productCategoryPojoBeforeCreation());
    verify(productsRepositoryMock).setProductCategoryById(productPojoAfterCreation().getId(),
                                                          productCategoryPojoAfterCreation().getId());
    assertNotNull(result.getCategory());
    assertEquals(result.getCategory().getId(), productCategoryPojoAfterCreation().getId());
    assertEquals(result.getCategory().getCode(), productCategoryPojoAfterCreation().getCode());
    assertEquals(result.getCategory().getName(), productCategoryPojoAfterCreation().getName());
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
