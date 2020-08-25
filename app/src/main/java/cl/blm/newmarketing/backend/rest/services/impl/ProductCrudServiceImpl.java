package cl.blm.newmarketing.backend.rest.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.model.entities.Product;
import cl.blm.newmarketing.backend.model.entities.QProduct;
import cl.blm.newmarketing.backend.model.repositories.ProductsRepository;
import cl.blm.newmarketing.backend.rest.dtos.ProductDto;
import cl.blm.newmarketing.backend.rest.services.CrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductCrudServiceImpl
    implements CrudService<ProductDto, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ProductCrudServiceImpl.class);

  @Autowired
  ProductsRepository products;
  @Autowired
  ConversionService conversion;

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

  @Nullable
  @Override
  public ProductDto create(ProductDto dto) {
    LOG.debug("create({})", dto);
    Product newEntity = conversion.convert(dto, Product.class);
    if (dto.getProductId() != null && products.findById(dto.getProductId()).isPresent()) {
      return null;
    } else {
      newEntity = products.saveAndFlush(newEntity);
      ProductDto newDto = conversion.convert(newEntity, ProductDto.class);
      return newDto;
    }
  }

  @Override
  public Collection<ProductDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort order = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, order);

    Iterable<Product> iterable;
    if (filters == null) {
      iterable = products.findAll(paged);
    } else {
      iterable = products.findAll(filters, paged);
    }

    List<ProductDto> list = new ArrayList<>();
    for (Product Product : iterable) {
      ProductDto dto = conversion.convert(Product, ProductDto.class);
      list.add(dto);
    }

    return list;
  }

  @Nullable
  @Override
  public ProductDto update(ProductDto dto) {
    LOG.debug("update({})", dto);
    Optional<Product> queriedProduct = products.findById(dto.getProductId());
    if (!queriedProduct.isPresent()) {
      return null;
    } else {
      Product existingProduct = queriedProduct.get();
      Product newProduct = conversion.convert(dto, Product.class);
      if (newProduct.equals(existingProduct)) {
        return dto;
      } else {
        try {
          newProduct = products.saveAndFlush(newProduct);
          return conversion.convert(newProduct, ProductDto.class);
        } catch (Exception exc) {
          LOG.error("Product could not be saved");
          return null;
        }
      }
    }
  }

  @Nullable
  @Override
  public ProductDto update(ProductDto dto, Integer id) {
    LOG.debug("update({})", dto);
    Optional<Product> queriedProduct = products.findById(id);
    if (!queriedProduct.isPresent()) {
      return null;
    } else {
      Product existingProduct = queriedProduct.get();
      Product newProduct = conversion.convert(dto, Product.class);
      if (newProduct.equals(existingProduct)) {
        return dto;
      } else {
        try {
          newProduct = products.saveAndFlush(newProduct);
          return conversion.convert(newProduct, ProductDto.class);
        } catch (Exception exc) {
          LOG.error("Product could not be saved");
          return null;
        }
      }
    }
  }

  @Override
  public boolean delete(Integer id) {
    LOG.debug("delete({})", id);
    try {
      products.deleteById(id);
      products.flush();
      return !products.existsById(id);
    } catch (Exception exc) {
      LOG.error("Could not delete Product with id {}", id, exc);
      return false;
    }
  }

  @Nullable
  @Override
  public ProductDto find(Integer id) {
    LOG.debug("find({})", id);
    Optional<Product> productById = products.findById(id);
    if (productById.isPresent()) {
      return null;
    } else {
      return conversion.convert(productById.get(), ProductDto.class);
    }
  }
}
