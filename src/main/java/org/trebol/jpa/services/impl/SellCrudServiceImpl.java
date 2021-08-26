package org.trebol.jpa.services.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

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

import org.trebol.jpa.entities.QSell;

import org.trebol.api.pojo.CustomerPojo;
import org.trebol.api.pojo.PersonPojo;
import org.trebol.api.pojo.ProductPojo;
import org.trebol.api.pojo.SellDetailPojo;
import org.trebol.api.pojo.SellPojo;
import org.trebol.api.pojo.SellTypePojo;
import org.trebol.api.pojo.SalespersonPojo;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.repositories.SalesRepository;
import org.trebol.jpa.services.GenericJpaCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellCrudServiceImpl
    extends GenericJpaCrudService<SellPojo, Sell> {
  private static final Logger LOG = LoggerFactory.getLogger(SellCrudServiceImpl.class);

  private final SalesRepository repository;
  private final ConversionService conversion;

  @Autowired
  public SellCrudServiceImpl(SalesRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  @Nullable
  private CustomerPojo convertCustomerToPojo(Customer source) {
    CustomerPojo customer = conversion.convert(source, CustomerPojo.class);
    if (customer != null) {
      PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
      if (person != null) {
        customer.setPerson(person);
      }
    }
    return customer;
  }

  private List<SellDetailPojo> convertDetailsToPojo(Collection<SellDetail> source) {
    List<SellDetailPojo> sellDetails = new ArrayList<>();
    for (SellDetail sourceSellDetail : source) {
      SellDetailPojo targetSellDetail = conversion.convert(sourceSellDetail, SellDetailPojo.class);
      if (targetSellDetail != null) {
        ProductPojo product = conversion.convert(sourceSellDetail.getProduct(), ProductPojo.class);
        if (product != null) {
          targetSellDetail.setProduct(product);
        }
        sellDetails.add(targetSellDetail);
      }
    }
    return sellDetails;
  }

  @Nullable
  private SalespersonPojo convertSalespersonToPojo(Salesperson source) {
    SalespersonPojo target = conversion.convert(source, SalespersonPojo.class);
    if (target != null) {
      PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
      if (person != null) {
        target.setPerson(person);
      }
    }
    return target;
  }

  @Nullable
  @Override
  public SellPojo entity2Pojo(Sell source) {
    SellPojo target = conversion.convert(source, SellPojo.class);
    if (target != null) {
      SellTypePojo sellType = conversion.convert(source.getType(), SellTypePojo.class);
      if (sellType != null) {
        target.setSellType(sellType);
      }

      CustomerPojo customer = this.convertCustomerToPojo(source.getCustomer());
      if (customer != null) {
        target.setCustomer(customer);
      }

      SalespersonPojo salesperson = this.convertSalespersonToPojo(source.getSalesperson());
      if (salesperson != null) {
        target.setSalesperson(salesperson);
      }
    }
    return target;
  }

  @Nullable
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
        Long longValue = Long.valueOf(stringValue);
        switch (paramName) {
          case "id":
            return predicate.and(qSell.id.eq(longValue)); // match por id es único
          case "date":
            Date dateValue = DateFormat.getInstance().parse(stringValue);
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
  public SellPojo readOne(Long id) {
    Optional<Sell> personById = repository.deepFindById(id);
    if (!personById.isPresent()) {
      return null;
    } else {
      Sell found = personById.get();
      SellPojo foundPojo = this.entity2Pojo(found);
      if (foundPojo != null) {
        List<SellDetailPojo> sellDetails = this.convertDetailsToPojo(found.getDetails());
        foundPojo.setSellDetails(sellDetails);
      }
      return foundPojo;
    }
  }
}
