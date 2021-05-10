package org.trebol.services.crud.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QSell;

import org.trebol.api.pojo.CustomerPojo;
import org.trebol.api.pojo.PersonPojo;
import org.trebol.api.pojo.ProductPojo;
import org.trebol.api.pojo.SellDetailPojo;
import org.trebol.api.pojo.SellPojo;
import org.trebol.api.pojo.SellTypePojo;
import org.trebol.api.pojo.SalespersonPojo;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.repositories.SalesRepository;
import org.trebol.services.crud.GenericCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellCrudServiceImpl
    extends GenericCrudService<SellPojo, Sell, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SellCrudServiceImpl.class);

  private final SalesRepository repository;
  private final ConversionService conversion;

  @Autowired
  public SellCrudServiceImpl(SalesRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  private CustomerPojo convertCustomerToPojo(Sell source) {
    CustomerPojo customer = conversion.convert(source.getCustomer(), CustomerPojo.class);
    PersonPojo person = conversion.convert(source.getCustomer().getPerson(), PersonPojo.class);
    customer.setPerson(person);
    return customer;
  }

  private List<SellDetailPojo> convertDetailsToPojo(Sell source) {
    List<SellDetailPojo> sellDetails = new ArrayList<>();
    for (SellDetail sourceSellDetail : source.getDetails()) {
      SellDetailPojo targetSellDetail = conversion.convert(sourceSellDetail, SellDetailPojo.class);
      ProductPojo product = conversion.convert(sourceSellDetail.getProduct(), ProductPojo.class);
      targetSellDetail.setProduct(product);
      sellDetails.add(targetSellDetail);
    }
    return sellDetails;
  }

  private SalespersonPojo convertSalespersonToPojo(Sell source) {
    SalespersonPojo target = conversion.convert(source.getSalesperson(), SalespersonPojo.class);
    PersonPojo person = conversion.convert(source.getSalesperson().getPerson(), PersonPojo.class);
    target.setPerson(person);
    return target;
  }

  @Override
  public SellPojo entity2Pojo(Sell source) {
    SellPojo target = conversion.convert(source, SellPojo.class);
    SellTypePojo sellType = conversion.convert(source.getType(), SellTypePojo.class);
    target.setSellType(sellType);

    CustomerPojo customer = convertCustomerToPojo(source);
    target.setCustomer(customer);

    if (source.getSalesperson()!= null) {
      SalespersonPojo salesperson = convertSalespersonToPojo(source);
      target.setSalesperson(salesperson);
    }

    return target;
  }

  @Override
  public Sell pojo2Entity(SellPojo source) {
    return conversion.convert(source, Sell.class);
  }

  @Override
  public Page<Sell> getAllEntities(Pageable paged, Predicate filters) {
    if (filters == null) {
      return repository.deepFindAll(paged);
    } else {
      return repository.findAll(filters, paged);
    }
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
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
        LOG.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      } catch (ParseException exc) {
        LOG.warn("Param '{}' couldn't be parsed as date (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }

  @Nullable
  @Override
  public SellPojo find(Integer id) {
    Optional<Sell> personById = repository.deepFindById(id);
    if (!personById.isPresent()) {
      return null;
    } else {
      Sell found = personById.get();
      SellPojo foundPojo = entity2Pojo(found);
      List<SellDetailPojo> sellDetails = convertDetailsToPojo(found);
      foundPojo.setSellDetails(sellDetails);
      return foundPojo;
    }
  }
}
