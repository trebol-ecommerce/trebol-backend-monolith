package cl.blm.newmarketing.store.services.data.impl;

import java.util.Map;

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

import cl.blm.newmarketing.store.api.pojo.ProductPojo;
import cl.blm.newmarketing.store.jpa.entities.Product;
import cl.blm.newmarketing.store.jpa.entities.QProduct;
import cl.blm.newmarketing.store.jpa.repositories.ProductsRepository;
import cl.blm.newmarketing.store.services.data.GenericEntityDataService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductDataServiceImpl
    extends GenericEntityDataService<ProductPojo, Product, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ProductDataServiceImpl.class);

  private ProductsRepository repository;
  private ConversionService conversion;

  @Autowired
  public ProductDataServiceImpl(ProductsRepository repository, ConversionService conversion) {
    super(LOG, repository);
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
      return repository.deepReadAll(filters, paged);
    }
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
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
          predicate.and(qProduct.name.likeIgnoreCase("%" + stringValue + "%"));
          break;
        default:
          break;
        }
      } catch (NumberFormatException exc) {
        LOG.error("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }
}
