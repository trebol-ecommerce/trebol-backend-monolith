package org.trebol.jpa.services.crud;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.trebol.exceptions.BadInputException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

  @Test
  public void sanity_check() {
    ProductsJpaCrudServiceImpl service = instantiate();
    assertNotNull(service);
  }

  @Test
  public void finds_by_barcode() throws BadInputException {
    Long productId = 1L;
    String productName = "test-one.jpg";
    String productBarcode = "test-one";
    String productDescription = "test last name";
    Integer productPrice = 100;
    Integer productStock = 10;
    Integer productStockCritical = 1;
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
