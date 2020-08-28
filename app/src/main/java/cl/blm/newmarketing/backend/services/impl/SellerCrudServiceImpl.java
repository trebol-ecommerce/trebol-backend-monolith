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

import cl.blm.newmarketing.backend.dtos.SellerDto;
import cl.blm.newmarketing.backend.model.entities.QSeller;
import cl.blm.newmarketing.backend.model.entities.Seller;
import cl.blm.newmarketing.backend.model.repositories.SellersRepository;
import cl.blm.newmarketing.backend.services.CrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellerCrudServiceImpl
    implements CrudService<SellerDto, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SellerCrudServiceImpl.class);

  @Autowired
  SellersRepository clients;
  @Autowired
  ConversionService conversion;

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QSeller qSeller = QSeller.seller;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
        case "id":
          intValue = Integer.valueOf(stringValue);
          return predicate.and(qSeller.id.eq(intValue)); // id matching is final
        case "name":
          predicate.and(qSeller.person.name.likeIgnoreCase("%" + stringValue + "%"));
          break;
        case "idnumber":
          predicate.and(qSeller.person.idCard.likeIgnoreCase("%" + stringValue + "%"));
          break;
        case "email":
          predicate.and(qSeller.person.email.likeIgnoreCase("%" + stringValue + "%"));
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
  public SellerDto create(SellerDto dto) {
    LOG.debug("create({})", dto);
    Seller newEntity = conversion.convert(dto, Seller.class);
    if (dto.getSellerId() != null && clients.findById(dto.getSellerId()).isPresent()) {
      return null;
    } else {
      newEntity = clients.saveAndFlush(newEntity);
      SellerDto newDto = conversion.convert(newEntity, SellerDto.class);
      return newDto;
    }
  }

  @Override
  public Collection<SellerDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    Sort orden = Sort.by("id").ascending();
    Pageable paged = PageRequest.of(pageIndex, pageSize, orden);

    Iterable<Seller> iterable;
    if (filters == null) {
      iterable = clients.findAll(paged);
    } else {
      iterable = clients.findAll(filters, paged);
    }

    List<SellerDto> list = new ArrayList<>();
    for (Seller client : iterable) {
      SellerDto dto = conversion.convert(client, SellerDto.class);
      list.add(dto);
    }

    return list;
  }

  @Nullable
  @Override
  public SellerDto update(SellerDto dto) {
    LOG.debug("update({})", dto);
    Optional<Seller> queriedSeller = clients.findById(dto.getSellerId());
    if (!queriedSeller.isPresent()) {
      return null;
    } else {
      Seller existingSeller = queriedSeller.get();
      Seller newPerson = conversion.convert(dto, Seller.class);
      if (newPerson.equals(existingSeller)) {
        return dto;
      } else {
        try {
          newPerson = clients.saveAndFlush(newPerson);
          return conversion.convert(newPerson, SellerDto.class);
        } catch (Exception exc) {
          LOG.error("Seller could not be saved");
          return null;
        }
      }
    }
  }

  @Nullable
  @Override
  public SellerDto update(SellerDto dto, Integer id) {
    LOG.debug("update({}, {})", dto, id);
    Optional<Seller> queriedSeller = clients.findById(id);
    if (!queriedSeller.isPresent()) {
      return null;
    } else {
      Seller existingSeller = queriedSeller.get();
      Seller newSeller = conversion.convert(dto, Seller.class);
      if (newSeller.equals(existingSeller)) {
        return dto;
      } else {
        try {
          newSeller = clients.saveAndFlush(newSeller);
          return conversion.convert(newSeller, SellerDto.class);
        } catch (Exception exc) {
          LOG.error("Seller could not be saved");
          return null;
        }
      }
    }
  }

  @Override
  public boolean delete(Integer id) {
    LOG.debug("delete({})", id);
    try {
      clients.deleteById(id);
      clients.flush();
      return !clients.existsById(id);
    } catch (Exception exc) {
      LOG.error("Could not delete Seller with id {}", id, exc);
      return false;
    }
  }

  @Nullable
  @Override
  public SellerDto find(Integer id) {
    LOG.debug("find({})", id);
    Optional<Seller> clientById = clients.findById(id);
    if (!clientById.isPresent()) {
      return null;
    } else {
      Seller entity = clientById.get();
      return conversion.convert(entity, SellerDto.class);
    }
  }
}
