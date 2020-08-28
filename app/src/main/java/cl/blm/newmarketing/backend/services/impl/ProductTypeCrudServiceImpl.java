package cl.blm.newmarketing.backend.services.impl;

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

import cl.blm.newmarketing.backend.dtos.ProductTypeDto;
import cl.blm.newmarketing.backend.model.entities.ProductType;
import cl.blm.newmarketing.backend.model.entities.QProductType;
import cl.blm.newmarketing.backend.model.repositories.ProductTypesRepository;
import cl.blm.newmarketing.backend.services.DtoCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductTypeCrudServiceImpl
    implements DtoCrudService<ProductTypeDto, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ProductTypeCrudServiceImpl.class);

  @Autowired
  ProductTypesRepository productTypes;
  @Autowired
  ConversionService conversion;

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QProductType qProductType = QProductType.productType;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
        case "id":
          intValue = Integer.valueOf(stringValue);
          return predicate.and(qProductType.id.eq(intValue)); // match por id es único
        case "name":
          predicate.and(qProductType.name.likeIgnoreCase("%" + stringValue + "%"));
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
  public ProductTypeDto create(ProductTypeDto dto) {
    LOG.debug("create({})", dto);
    ProductType newEntity = conversion.convert(dto, ProductType.class);
    if (dto.getProductTypeId() != null && productTypes.findById(dto.getProductTypeId()).isPresent()) {
      return null;
    } else {
      newEntity = productTypes.saveAndFlush(newEntity);
      ProductTypeDto newDto = conversion.convert(newEntity, ProductTypeDto.class);
      return newDto;
    }
  }

  @Override
  public Collection<ProductTypeDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort order = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, order);

    Iterable<ProductType> iterable;
    if (filters == null) {
      iterable = productTypes.findAll(paged);
    } else {
      iterable = productTypes.findAll(filters, paged);
    }

    List<ProductTypeDto> list = new ArrayList<>();
    for (ProductType productType : iterable) {
      ProductTypeDto dto = conversion.convert(productType, ProductTypeDto.class);
      list.add(dto);
    }

    return list;
  }

  @Nullable
  @Override
  public ProductTypeDto update(ProductTypeDto dto) {
    LOG.debug("update({})", dto);
    Optional<ProductType> queriedProduct = productTypes.findById(dto.getProductTypeId());
    if (!queriedProduct.isPresent()) {
      return null;
    } else {
      ProductType existingProduct = queriedProduct.get();
      ProductType newProduct = conversion.convert(dto, ProductType.class);
      if (newProduct.equals(existingProduct)) {
        return dto;
      } else {
        try {
          newProduct = productTypes.saveAndFlush(newProduct);
          return conversion.convert(newProduct, ProductTypeDto.class);
        } catch (Exception exc) {
          LOG.error("Product could not be saved");
          return null;
        }
      }
    }
  }

  @Nullable
  @Override
  public ProductTypeDto update(ProductTypeDto dto, Integer id) {
    LOG.debug("update({})", dto);
    Optional<ProductType> queriedProduct = productTypes.findById(id);
    if (!queriedProduct.isPresent()) {
      return null;
    } else {
      ProductType existingProduct = queriedProduct.get();
      ProductType newProduct = conversion.convert(dto, ProductType.class);
      if (newProduct.equals(existingProduct)) {
        return dto;
      } else {
        try {
          newProduct = productTypes.saveAndFlush(newProduct);
          return conversion.convert(newProduct, ProductTypeDto.class);
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
      productTypes.deleteById(id);
      productTypes.flush();
      return !productTypes.existsById(id);
    } catch (Exception exc) {
      LOG.error("Could not delete Product with id {}", id, exc);
      return false;
    }
  }

  @Nullable
  @Override
  public ProductTypeDto find(Integer id) {
    LOG.debug("find({})", id);
    Optional<ProductType> productById = productTypes.findById(id);
    if (!productById.isPresent()) {
      return null;
    } else {
      return conversion.convert(productById.get(), ProductTypeDto.class);
    }
  }
}
