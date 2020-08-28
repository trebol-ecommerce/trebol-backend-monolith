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

import cl.blm.newmarketing.backend.dtos.ProductFamilyDto;
import cl.blm.newmarketing.backend.model.entities.ProductFamily;
import cl.blm.newmarketing.backend.model.entities.QProductFamily;
import cl.blm.newmarketing.backend.model.repositories.ProductFamiliesRepository;
import cl.blm.newmarketing.backend.services.CrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class ProductFamilyCrudServiceImpl
    implements CrudService<ProductFamilyDto, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(ProductFamilyCrudServiceImpl.class);

  @Autowired
  ProductFamiliesRepository productTypes;
  @Autowired
  ConversionService conversion;

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QProductFamily qProductFamily = QProductFamily.productFamily;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
        case "id":
          intValue = Integer.valueOf(stringValue);
          return predicate.and(qProductFamily.id.eq(intValue)); // match por id es único
        case "name":
          predicate.and(qProductFamily.name.likeIgnoreCase("%" + stringValue + "%"));
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
  public ProductFamilyDto create(ProductFamilyDto dto) {
    LOG.debug("create({})", dto);
    ProductFamily newEntity = conversion.convert(dto, ProductFamily.class);
    if (dto.getProductFamilyId() != null && productTypes.findById(dto.getProductFamilyId()).isPresent()) {
      return null;
    } else {
      newEntity = productTypes.saveAndFlush(newEntity);
      ProductFamilyDto newDto = conversion.convert(newEntity, ProductFamilyDto.class);
      return newDto;
    }
  }

  @Override
  public Collection<ProductFamilyDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort order = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, order);

    Iterable<ProductFamily> iterable;
    if (filters == null) {
      iterable = productTypes.findAll(paged);
    } else {
      iterable = productTypes.findAll(filters, paged);
    }

    List<ProductFamilyDto> list = new ArrayList<>();
    for (ProductFamily ProductFamily : iterable) {
      ProductFamilyDto dto = conversion.convert(ProductFamily, ProductFamilyDto.class);
      list.add(dto);
    }

    return list;
  }

  @Nullable
  @Override
  public ProductFamilyDto update(ProductFamilyDto dto) {
    LOG.debug("update({})", dto);
    Optional<ProductFamily> queriedProduct = productTypes.findById(dto.getProductFamilyId());
    if (!queriedProduct.isPresent()) {
      return null;
    } else {
      ProductFamily existingProduct = queriedProduct.get();
      ProductFamily newProduct = conversion.convert(dto, ProductFamily.class);
      if (newProduct.equals(existingProduct)) {
        return dto;
      } else {
        try {
          newProduct = productTypes.saveAndFlush(newProduct);
          return conversion.convert(newProduct, ProductFamilyDto.class);
        } catch (Exception exc) {
          LOG.error("Product could not be saved");
          return null;
        }
      }
    }
  }

  @Nullable
  @Override
  public ProductFamilyDto update(ProductFamilyDto dto, Integer id) {
    LOG.debug("update({})", dto);
    Optional<ProductFamily> queriedProduct = productTypes.findById(id);
    if (!queriedProduct.isPresent()) {
      return null;
    } else {
      ProductFamily existingProduct = queriedProduct.get();
      ProductFamily newProduct = conversion.convert(dto, ProductFamily.class);
      if (newProduct.equals(existingProduct)) {
        return dto;
      } else {
        try {
          newProduct = productTypes.saveAndFlush(newProduct);
          return conversion.convert(newProduct, ProductFamilyDto.class);
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
  public ProductFamilyDto find(Integer id) {
    LOG.debug("find({})", id);
    Optional<ProductFamily> productById = productTypes.findById(id);
    if (!productById.isPresent()) {
      return null;
    } else {
      return conversion.convert(productById.get(), ProductFamilyDto.class);
    }
  }
}
