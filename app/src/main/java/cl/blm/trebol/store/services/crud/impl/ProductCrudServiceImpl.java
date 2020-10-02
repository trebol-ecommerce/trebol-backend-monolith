package cl.blm.trebol.store.services.crud.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.store.jpa.entities.QProduct;
import cl.blm.trebol.store.api.pojo.ProductPojo;
import cl.blm.trebol.store.jpa.entities.Product;
import cl.blm.trebol.store.jpa.repositories.ProductsRepository;
import cl.blm.trebol.store.services.crud.GenericEntityCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductCrudServiceImpl
    extends GenericEntityCrudService<ProductPojo, Product, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ProductCrudServiceImpl.class);

  private final ProductsRepository repository;
  private final ConversionService conversion;

  @Autowired
  public ProductCrudServiceImpl(ProductsRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  @Override
  public ProductPojo entity2Pojo(Product source) {
    return conversion.convert(source, ProductPojo.class);
  }

  @Override
  public Product pojo2Entity(ProductPojo source) {
    return conversion.convert(source, Product.class);
  }

  @Override
  public Page<Product> getAllEntities(Pageable paged, Predicate filters) {
    if (filters == null) {
      return repository.deepReadAll(paged);
    } else {
      return repository.findAll(filters, paged);
    }
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    QProduct qProduct = QProduct.product;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
          case "id":
            intValue = Integer.valueOf(stringValue);
            return predicate.and(qProduct.id.eq(intValue)); // match por id es único
          case "name":
            predicate = predicate.and(qProduct.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "productFamily":
            intValue = Integer.valueOf(stringValue);
            predicate.and(qProduct.productType.productFamily.id.eq(intValue));
            break;
        default:
          break;
        }
      } catch (NumberFormatException exc) {
        LOG.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }

  @Override
  public ProductPojo find(Integer id) {
    Optional<Product> productById = repository.findById(id);
    if (!productById.isPresent()) {
      return null;
    } else {
      Product found = productById.get();
      ProductPojo foundPojo = entity2Pojo(found);
      // TODO consider refactoring Product entity and pull this method out
      foundPojo.setImagesURL(new ArrayList<>());
      return foundPojo;
    }
  }
}
