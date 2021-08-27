package org.trebol.jpa.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.api.pojo.ImagePojo;
import org.trebol.jpa.entities.QProduct;

import org.trebol.api.pojo.ProductPojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.ProductImage;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.IProductImagesJpaRepository;
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
  private final IProductImagesJpaRepository imagesRepository;
  private final ConversionService conversion;

  @Autowired
  public ProductsJpaCrudServiceImpl(IProductsJpaRepository repository, IProductImagesJpaRepository imagesRepository,
    ConversionService conversion) {
    super(repository);
    this.imagesRepository = imagesRepository;
    this.conversion = conversion;
  }

  @Nullable
  @Override
  public ProductPojo entity2Pojo(Product source) {
    ProductPojo target = conversion.convert(source, ProductPojo.class);
    if (target != null) {
      Long id = target.getId();
      Set<ImagePojo> images = new HashSet<>();
      for (ProductImage pi : imagesRepository.deepFindProductImagesByProductId(id)) {
        ImagePojo targetImage = conversion.convert(pi.getImage(), ImagePojo.class);
        if (targetImage != null) {
          images.add(targetImage);
        }
      }
      target.setImages(images);
    }
    return target;
  }

  @Nullable
  @Override
  public Product pojo2Entity(ProductPojo source) {
    return conversion.convert(source, Product.class);
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QProduct qProduct = QProduct.product;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Long longValue = Long.valueOf(stringValue);
        switch (paramName) {
          case "id":
            return predicate.and(qProduct.id.eq(longValue)); // match por id es único
          case "name":
            predicate.and(qProduct.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "productCategory":
            predicate.and(qProduct.productCategory.id.eq(longValue));
            break;
          case "productCategoryName":
            predicate.and(qProduct.productCategory.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }

  @Override
  public boolean itemExists(ProductPojo input) throws BadInputException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
