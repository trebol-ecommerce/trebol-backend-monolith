package org.trebol.jpa.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.pojo.ImagePojo;
import org.trebol.pojo.ProductCategoryPojo;
import org.trebol.jpa.entities.QProduct;

import org.trebol.pojo.ProductPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.jpa.repositories.IProductsJpaRepository;

import javassist.NotFoundException;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductsJpaServiceImpl
  extends GenericJpaService<ProductPojo, Product> {

  private final IProductsJpaRepository productsRepository;
  private final IImagesJpaRepository imagesRepository;
  private final IProductImagesJpaRepository productImagesRepository;
  private final IProductsCategoriesJpaRepository categoriesRepository;
  private final GenericJpaService<ProductCategoryPojo, ProductCategory> categoriesService;
  private final ConversionService conversion;
  private final Validator validator;

  @Autowired
  public ProductsJpaServiceImpl(IProductsJpaRepository repository, IImagesJpaRepository imagesRepository,
                                IProductImagesJpaRepository productImagesRepository,
                                IProductsCategoriesJpaRepository categoriesRepository,
                                GenericJpaService<ProductCategoryPojo, ProductCategory> categoriesService,
                                ConversionService conversion,
                                Validator validator) {
    super(repository, LoggerFactory.getLogger(ProductsJpaServiceImpl.class));
    this.productsRepository = repository;
    this.imagesRepository = imagesRepository;
    this.productImagesRepository = productImagesRepository;
    this.categoriesRepository = categoriesRepository;
    this.categoriesService = categoriesService;
    this.conversion = conversion;
    this.validator = validator;
  }

  @Transactional
  @Override
  public ProductPojo create(ProductPojo inputPojo) throws BadInputException, EntityAlreadyExistsException {
    if (this.itemExists(inputPojo)) {
      throw new EntityAlreadyExistsException("The item to be created already exists");
    } else {
      Product input = this.convertToNewEntity(inputPojo);
      Product output = repository.saveAndFlush(input);
      ProductPojo result = this.convertToPojo(output);
      this.applyImages(inputPojo, output);
      return result;
    }
  }

  @Override
  public void delete(Long id) throws NotFoundException {
    if (!repository.existsById(id)) {
      throw new NotFoundException("The requested item does not exist");
    } else {
      productImagesRepository.deleteByProductId(id);
      repository.deleteById(id);
      repository.flush();
    }
  }

  @Override
  public ProductPojo convertToPojo(Product source) {
    ProductPojo target = conversion.convert(source, ProductPojo.class);
    if (target != null) {
      Long id = target.getId();
      Set<ImagePojo> images = new HashSet<>();
      for (ProductImage pi : productImagesRepository.deepFindProductImagesByProductId(id)) {
        ImagePojo targetImage = conversion.convert(pi.getImage(), ImagePojo.class);
        if (targetImage != null) {
          images.add(targetImage);
        }
      }
      target.setImages(images);

      ProductCategory category = source.getProductCategory();
      if (category != null) {
        ProductCategoryPojo categoryPojo = conversion.convert(category, ProductCategoryPojo.class);
        target.setCategory(categoryPojo);
      }
    }
    return target;
  }

  @Override
  public Product convertToNewEntity(ProductPojo source) throws BadInputException {
    Product target = conversion.convert(source, Product.class);
    this.applyCategory(source, target);
    return target;
  }

  @Override
  public Product applyChangesToExistingEntity(ProductPojo source, Product existing) throws BadInputException {
    Product target = new Product(existing);

    String barcode = source.getBarcode();
    if (barcode != null && !barcode.isBlank() && !target.getBarcode().equals(barcode)) {
      target.setBarcode(barcode);
    }

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    Integer price = source.getPrice();
    if (price != null) {
      target.setPrice(price);
    }

    String description = source.getDescription();
    if (description != null && !description.isBlank() && !target.getDescription().equals(description)) {
      target.setDescription(description);
    }


    Integer currentStock = source.getCurrentStock();
    if (currentStock != null) {
      target.setStockCurrent(currentStock);
    }

    this.applyCategory(source, target);
    this.applyImages(source, target);

    return target;
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QProduct qProduct = QProduct.product;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        switch (paramName) {
          case "id":
            return predicate.and(qProduct.id.eq(Long.valueOf(stringValue))); // match por id es único
          case "barcode":
            predicate.and(qProduct.barcode.eq(stringValue));
            break;
          case "name":
            predicate.and(qProduct.name.eq(stringValue));
            break;
          case "barcodeLike":
            predicate.and(qProduct.barcode.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "nameLike":
            predicate.and(qProduct.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "productCategory":
            predicate.and(qProduct.productCategory.name.eq(stringValue));
            break;
          case "productCategoryLike":
            predicate.and(qProduct.productCategory.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }

  @Override
  public Optional<Product> getExisting(ProductPojo input) throws BadInputException {
    String barcode = input.getBarcode();
    if (barcode == null || barcode.isEmpty()) {
      throw new BadInputException("Invalid product barcode");
    } else {
      return this.productsRepository.findByBarcode(barcode);
    }
  }

  private void applyCategory(ProductPojo source, Product target) throws BadInputException {
    ProductCategoryPojo category = source.getCategory();
    if (category != null) {
      String categoryCode = category.getCode();
      ProductCategory previousCategory = target.getProductCategory();
      if (categoryCode == null) {
        this.applyNewCategory(target, category);
      } else if (previousCategory == null || !previousCategory.getCode().equals(categoryCode)) {
        Optional<ProductCategory> categoryCodeMatch = categoriesRepository.findByCode(categoryCode);
        if (categoryCodeMatch.isPresent()) {
          target.setProductCategory(categoryCodeMatch.get());
        } else {
          this.applyNewCategory(target, category);
        }
      }
    }
  }

  private void applyNewCategory(Product target, ProductCategoryPojo category) throws BadInputException {
    ProductCategory newCategoryEntity = categoriesService.convertToNewEntity(category);
    newCategoryEntity = categoriesRepository.saveAndFlush(newCategoryEntity);
    target.setProductCategory(newCategoryEntity);
  }

  private void applyImages(ProductPojo source, Product target) {
    Collection<ImagePojo> images = source.getImages();
    if (images != null) {
      List<ProductImage> targetImages = new ArrayList<>();
      for (ImagePojo img : images) {
        if (validator.validate(img).isEmpty()) {
          String filename = img.getFilename();
          Image image;
          Optional<Image> filenameMatch = imagesRepository.findByFilename(filename);
          if (filenameMatch.isEmpty()) {
            image = conversion.convert(img, Image.class);
            if (image != null) {
              image = imagesRepository.saveAndFlush(image);
            }
          } else {
            image = filenameMatch.get();
          }
          ProductImage targetImage = new ProductImage();
          targetImage.setProduct(target);
          targetImage.setImage(image);
          targetImages.add(targetImage);
        }
      }

      if (target.getId() != null) {
        productImagesRepository.deleteByProductId(target.getId());
      }
      if (!targetImages.isEmpty()) {
        productImagesRepository.saveAll(targetImages);
      }
    }
  }
}
