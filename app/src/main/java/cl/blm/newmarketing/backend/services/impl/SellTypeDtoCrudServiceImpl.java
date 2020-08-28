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

import cl.blm.newmarketing.backend.dtos.SellTypeDto;
import cl.blm.newmarketing.backend.model.entities.QSellType;
import cl.blm.newmarketing.backend.model.entities.SellType;
import cl.blm.newmarketing.backend.model.repositories.SellTypesRepository;
import cl.blm.newmarketing.backend.services.DtoCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellTypeDtoCrudServiceImpl
    implements DtoCrudService<SellTypeDto, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SellTypeDtoCrudServiceImpl.class);

  @Autowired
  SellTypesRepository sellTypes;
  @Autowired
  ConversionService conversion;

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QSellType qSellType = QSellType.sellType;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
        case "id":
          intValue = Integer.valueOf(stringValue);
          return predicate.and(qSellType.id.eq(intValue)); // match por id es único
        case "name":
          predicate.and(qSellType.name.likeIgnoreCase("%" + stringValue + "%"));
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
  public SellTypeDto create(SellTypeDto dto) {
    LOG.debug("create({})", dto);
    SellType newEntity = conversion.convert(dto, SellType.class);
    if (dto.getSellTypeId() != null && sellTypes.findById(dto.getSellTypeId()).isPresent()) {
      return null;
    } else {
      newEntity = sellTypes.saveAndFlush(newEntity);
      SellTypeDto newDto = conversion.convert(newEntity, SellTypeDto.class);
      return newDto;
    }
  }

  @Override
  public Collection<SellTypeDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort order = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, order);

    Iterable<SellType> iterable;
    if (filters == null) {
      iterable = sellTypes.findAll(paged);
    } else {
      iterable = sellTypes.findAll(filters, paged);
    }

    List<SellTypeDto> list = new ArrayList<>();
    for (SellType productType : iterable) {
      SellTypeDto dto = conversion.convert(productType, SellTypeDto.class);
      list.add(dto);
    }

    return list;
  }

  @Nullable
  @Override
  public SellTypeDto update(SellTypeDto dto) {
    LOG.debug("update({})", dto);
    Optional<SellType> queriedProduct = sellTypes.findById(dto.getSellTypeId());
    if (!queriedProduct.isPresent()) {
      return null;
    } else {
      SellType existingProduct = queriedProduct.get();
      SellType newProduct = conversion.convert(dto, SellType.class);
      if (newProduct.equals(existingProduct)) {
        return dto;
      } else {
        try {
          newProduct = sellTypes.saveAndFlush(newProduct);
          return conversion.convert(newProduct, SellTypeDto.class);
        } catch (Exception exc) {
          LOG.error("Product could not be saved");
          return null;
        }
      }
    }
  }

  @Nullable
  @Override
  public SellTypeDto update(SellTypeDto dto, Integer id) {
    LOG.debug("update({})", dto);
    Optional<SellType> queriedProduct = sellTypes.findById(id);
    if (!queriedProduct.isPresent()) {
      return null;
    } else {
      SellType existingProduct = queriedProduct.get();
      SellType newProduct = conversion.convert(dto, SellType.class);
      if (newProduct.equals(existingProduct)) {
        return dto;
      } else {
        try {
          newProduct = sellTypes.saveAndFlush(newProduct);
          return conversion.convert(newProduct, SellTypeDto.class);
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
      sellTypes.deleteById(id);
      sellTypes.flush();
      return !sellTypes.existsById(id);
    } catch (Exception exc) {
      LOG.error("Could not delete Product with id {}", id, exc);
      return false;
    }
  }

  @Nullable
  @Override
  public SellTypeDto find(Integer id) {
    LOG.debug("find({})", id);
    Optional<SellType> productById = sellTypes.findById(id);
    if (!productById.isPresent()) {
      return null;
    } else {
      return conversion.convert(productById.get(), SellTypeDto.class);
    }
  }
}
