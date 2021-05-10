package org.trebol.services.crud.impl;

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

import org.trebol.jpa.entities.QSalesperson;

import org.trebol.api.pojo.PersonPojo;
import org.trebol.api.pojo.SalespersonPojo;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.services.crud.GenericCrudService;
import org.trebol.jpa.repositories.SalespeopleRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SalespersonCrudServiceImpl
    extends GenericCrudService<SalespersonPojo, Salesperson, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SalespersonCrudServiceImpl.class);

  private final SalespeopleRepository repository;
  private final ConversionService conversion;

  @Autowired
  public SalespersonCrudServiceImpl(SalespeopleRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  @Override
  public SalespersonPojo entity2Pojo(Salesperson source) {
    SalespersonPojo target = conversion.convert(source, SalespersonPojo.class);
    PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
    target.setPerson(person);
    return target;
  }

  @Override
  public Salesperson pojo2Entity(SalespersonPojo source) {
    Salesperson target = conversion.convert(source, Salesperson.class);
    Person personTarget = conversion.convert(source.getPerson(), Person.class);
    target.setPerson(personTarget);
    return target;
  }

  @Override
  public Page<Salesperson> getAllEntities(Pageable paged, Predicate filters) {
    if (filters == null) {
      return repository.deepReadAll(paged);
    } else {
      return repository.findAll(filters, paged);
    }
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    QSalesperson qSalesperson = QSalesperson.salesperson;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
          case "id":
            intValue = Integer.valueOf(stringValue);
            return predicate.and(qSalesperson.id.eq(intValue)); // id matching is final
          case "name":
            predicate.and(qSalesperson.person.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idnumber":
            predicate.and(qSalesperson.person.idCard.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "email":
            predicate.and(qSalesperson.person.email.likeIgnoreCase("%" + stringValue + "%"));
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
