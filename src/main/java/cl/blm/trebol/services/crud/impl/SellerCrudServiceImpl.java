package cl.blm.trebol.services.crud.impl;

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

import cl.blm.trebol.jpa.entities.QSeller;
import cl.blm.trebol.api.pojo.PersonPojo;
import cl.blm.trebol.api.pojo.SellerPojo;
import cl.blm.trebol.jpa.entities.Person;
import cl.blm.trebol.jpa.entities.Seller;
import cl.blm.trebol.jpa.repositories.SellersRepository;
import cl.blm.trebol.services.crud.GenericCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellerCrudServiceImpl
    extends GenericCrudService<SellerPojo, Seller, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SellerCrudServiceImpl.class);

  private final SellersRepository repository;
  private final ConversionService conversion;

  @Autowired
  public SellerCrudServiceImpl(SellersRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  @Override
  public SellerPojo entity2Pojo(Seller source) {
    SellerPojo target = conversion.convert(source, SellerPojo.class);
    PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
    target.setPerson(person);
    return target;
  }

  @Override
  public Seller pojo2Entity(SellerPojo source) {
    Seller target = conversion.convert(source, Seller.class);
    Person personTarget = conversion.convert(source.getPerson(), Person.class);
    target.setPerson(personTarget);
    return target;
  }

  @Override
  public Page<Seller> getAllEntities(Pageable paged, Predicate filters) {
    if (filters == null) {
      return repository.deepReadAll(paged);
    } else {
      return repository.findAll(filters, paged);
    }
  }

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
}
