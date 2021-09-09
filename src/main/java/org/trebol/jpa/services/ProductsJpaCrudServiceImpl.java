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

import org.trebol.api.pojo.ImagePojo;
import org.trebol.api.pojo.ProductCategoryPojo;
import org.trebol.jpa.entities.QProduct;

import org.trebol.api.pojo.ProductPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IImagesJpaRepository;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.jpa.repositories.IProductsJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductsJpaCrudServiceImpl
  extends GenericJpaCrudService<ProductPojo, Product> {

  private static final Logger logger = LoggerFactory.getLogger(ProductsJpaCrudServiceImpl.class);
  private final IProductsJpaRepository productsRepository;
  private final IImagesJpaRepository imagesRepository;
  private final IProductImagesJpaRepository productImagesRepository;
  private final IProductsCategoriesJpaRepository categoriesRepository;
  private final ConversionService conversion;
  private final Validator validator;

  @Autowired
  public ProductsJpaCrudServiceImpl(IProductsJpaRepository repository, IImagesJpaRepository imagesRepository,
    IProductImagesJpaRepository productImagesRepository,
    IProductsCategoriesJpaRepository categoriesRepository, ConversionService conversion,
    Validator validator) {
    super(repository);
    this.productsRepository = repository;
    this.imagesRepository = imagesRepository;
    this.productImagesRepository = productImagesRepository;
    this.categoriesRepository = categoriesRepository;
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
      Collection<ImagePojo> images = inputPojo.getImages();
      if (images != null && !images.isEmpty()) {
        this.saveAllImages(images, output);
      }
      return result;
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
    }
    return target;
  }

  @Override
  public Product convertToNewEntity(ProductPojo source) {
    return conversion.convert(source, Product.class);
  }

  @Override
  public void applyChangesToExistingEntity(ProductPojo source, Product target) throws BadInputException {
    String barcode = source.getBarcode();
    if (barcode != null && !barcode.isBlank() && !target.getBarcode().equals(barcode)) {
      target.setBarcode(barcode);
    }

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getBarcode().equals(name)) {
      target.setBarcode(name);
    }

    Integer price = source.getPrice();
    if (price != null) {
      target.setPrice(price);
    }

    String description = source.getDescription();
    if (description != null && !description.isBlank() && !target.getBarcode().equals(description)) {
      target.setBarcode(description);
    }

    ProductCategoryPojo category = source.getCategory();
    if (category != null) {
      String categoryName = category.getName();
      if (categoryName != null && !categoryName.isBlank() && !target.getProductCategory().getName().equals(categoryName)) {
        Optional<ProductCategory> categoryNameMatch = categoriesRepository.findByName(categoryName);
        if (categoryNameMatch.isPresent()) {
          target.setProductCategory(categoryNameMatch.get());
        }
      }
    }

    Integer currentStock = source.getCurrentStock();
    if (price != null) {
      target.setStockCurrent(currentStock);
    }

    Collection<ImagePojo> images = source.getImages();
    if (images != null) {
      List<ImagePojo> imagesToSave = new ArrayList<>();
      for (ImagePojo img : images) {
        if (validator.validate(img).isEmpty()) {
          imagesToSave.add(img);
        }
      }

      List<ProductImage> productImages = productImagesRepository.findByProductId(target.getId());
      productImagesRepository.deleteAll(productImages);
      this.saveAllImages(imagesToSave, target);
    }
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
  public boolean itemExists(ProductPojo input) throws BadInputException {
    String barcode = input.getBarcode();
    if (barcode == null || barcode.isEmpty()) {
      throw new BadInputException("Invalid product barcode");
    } else {
      return (this.productsRepository.findByBarcode(barcode).isPresent());
    }
  }

  private void saveAllImages(Collection<ImagePojo> images, Product output) {
    List<ProductImage> targetImages = new ArrayList<>();
    for (ImagePojo inputImage : images) {
      String filename = inputImage.getFilename();
      Image image;
      Optional<Image> filenameMatch = imagesRepository.findByFilename(filename);
      if (filenameMatch.isEmpty()) {
        image = conversion.convert(inputImage, Image.class);
        image = imagesRepository.saveAndFlush(image);
      } else {
        image = filenameMatch.get();
      }
      ProductImage targetImage = new ProductImage();
      targetImage.setProduct(output);
      targetImage.setImage(image);
      targetImages.add(targetImage);
    }
    productImagesRepository.saveAll(targetImages);
  }
}
