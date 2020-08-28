package cl.blm.newmarketing.backend.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import cl.blm.newmarketing.backend.dtos.SellDto;
import cl.blm.newmarketing.backend.model.entities.QSell;
import cl.blm.newmarketing.backend.model.entities.Sell;
import cl.blm.newmarketing.backend.model.repositories.SalesRepository;
import cl.blm.newmarketing.backend.services.DtoCrudService;
import cl.blm.newmarketing.backend.services.UtilityService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellDtoCrudServiceImpl
    implements DtoCrudService<SellDto, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SellDtoCrudServiceImpl.class);

  @Autowired
  SalesRepository sales;
  @Autowired
  ConversionService conversion;
  @Autowired
  UtilityService util;

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QSell qSell = QSell.sell;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        Date dateValue;
        switch (paramName) {
        case "id":
          intValue = Integer.valueOf(stringValue);
          return predicate.and(qSell.id.eq(intValue)); // match por id es único
        case "date":
          dateValue = DateFormat.getInstance().parse(stringValue);
          predicate.and(qSell.date.eq(dateValue));
          break;
        // TODO add more filters
        default:
          break;
        }
      } catch (NumberFormatException exc) {
        LOG.error("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      } catch (ParseException exc) {
        LOG.error("Param '{}' couldn't be parsed as date (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }

  @Nullable
  @Override
  public SellDto create(SellDto dto) {
    LOG.debug("create({})", dto);
    Sell newEntity = conversion.convert(dto, Sell.class);
    if (dto.getSellId() != null && sales.findById(dto.getSellId()).isPresent()) {
      return null;
    } else {
      newEntity = sales.saveAndFlush(newEntity);
      SellDto newDto = conversion.convert(newEntity, SellDto.class);
      return newDto;
    }
  }

  @Override
  public Collection<SellDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort order = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, order);

    Iterable<Sell> iterable;
    if (filters == null) {
      iterable = sales.findAll(paged);
    } else {
      iterable = sales.findAll(filters, paged);
    }

    List<SellDto> list = new ArrayList<>();
    for (Sell Sell : iterable) {
      SellDto dto = conversion.convert(Sell, SellDto.class);
      list.add(dto);
    }

    return list;
  }

  @Nullable
  @Override
  public SellDto update(SellDto dto) {
    LOG.debug("update({})", dto);
    Optional<Sell> queriedSell = sales.findById(dto.getSellId());
    if (!queriedSell.isPresent()) {
      return null;
    } else {
      Sell existingSell = queriedSell.get();
      Sell newSell = conversion.convert(dto, Sell.class);
      if (newSell.equals(existingSell)) {
        return dto;
      } else {
        try {
          newSell = sales.saveAndFlush(newSell);
          return conversion.convert(newSell, SellDto.class);
        } catch (Exception exc) {
          LOG.error("Sell could not be saved");
          return null;
        }
      }
    }
  }

  @Nullable
  @Override
  public SellDto update(SellDto dto, Integer id) {
    LOG.debug("update({})", dto);
    Optional<Sell> queriedSell = sales.findById(id);
    if (!queriedSell.isPresent()) {
      return null;
    } else {
      Sell existingSell = queriedSell.get();
      Sell newSell = conversion.convert(dto, Sell.class);
      if (newSell.equals(existingSell)) {
        return dto;
      } else {
        try {
          newSell = sales.saveAndFlush(newSell);
          return conversion.convert(newSell, SellDto.class);
        } catch (Exception exc) {
          LOG.error("Sell could not be saved");
          return null;
        }
      }
    }
  }

  @Override
  public boolean delete(Integer id) {
    LOG.debug("delete({})", id);
    try {
      sales.deleteById(id);
      sales.flush();
      return !sales.existsById(id);
    } catch (Exception exc) {
      LOG.error("Could not delete Sell with id {}", id, exc);
      return false;
    }
  }

  @Nullable
  @Override
  public SellDto find(Integer id) {
    LOG.debug("find({})", id);
    Optional<Sell> productById = sales.findById(id);
    if (!productById.isPresent()) {
      return null;
    } else {
      return conversion.convert(productById.get(), SellDto.class);
    }
  }
}
