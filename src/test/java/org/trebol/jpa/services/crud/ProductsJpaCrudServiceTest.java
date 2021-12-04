package org.trebol.jpa.services.crud;


import org.junit.Test;
import org.mockito.Mock;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;


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
    ProductsJpaCrudServiceImpl service = new ProductsJpaCrudServiceImpl(
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
    assertNotNull(service);
  }

}
