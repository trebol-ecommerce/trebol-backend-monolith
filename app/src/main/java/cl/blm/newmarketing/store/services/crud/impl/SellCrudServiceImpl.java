package cl.blm.newmarketing.store.services.crud.impl;

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

import cl.blm.newmarketing.store.api.pojo.ClientPojo;
import cl.blm.newmarketing.store.api.pojo.PersonPojo;
import cl.blm.newmarketing.store.api.pojo.ProductPojo;
import cl.blm.newmarketing.store.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.store.api.pojo.SellPojo;
import cl.blm.newmarketing.store.api.pojo.SellTypePojo;
import cl.blm.newmarketing.store.api.pojo.SellerPojo;
import cl.blm.newmarketing.store.jpa.entities.QSell;
import cl.blm.newmarketing.store.jpa.entities.Sell;
import cl.blm.newmarketing.store.jpa.entities.SellDetail;
import cl.blm.newmarketing.store.jpa.repositories.SalesRepository;
import cl.blm.newmarketing.store.services.crud.GenericEntityCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellCrudServiceImpl
    extends GenericEntityCrudService<SellPojo, Sell, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SellCrudServiceImpl.class);

  private final SalesRepository repository;
  private final ConversionService conversion;

  @Autowired
  public SellCrudServiceImpl(SalesRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  private ClientPojo convertClientToPojo(Sell source) {
    ClientPojo client = conversion.convert(source.getClient(), ClientPojo.class);
    PersonPojo person = conversion.convert(source.getClient().getPerson(), PersonPojo.class);
    client.setPerson(person);
    return client;
  }

  private List<SellDetailPojo> convertDetailsToPojo(Sell source) {
    List<SellDetailPojo> sellDetails = new ArrayList<>();
    for (SellDetail sourceSellDetail : source.getSellDetails()) {
      SellDetailPojo targetSellDetail = conversion.convert(sourceSellDetail, SellDetailPojo.class);
      ProductPojo product = conversion.convert(sourceSellDetail.getProduct(), ProductPojo.class);
      targetSellDetail.setProduct(product);
      sellDetails.add(targetSellDetail);
    }
    return sellDetails;
  }

  private SellerPojo convertSellerToPojo(Sell source) {
    SellerPojo seller = conversion.convert(source.getSeller(), SellerPojo.class);
    PersonPojo person = conversion.convert(source.getSeller().getPerson(), PersonPojo.class);
    seller.setPerson(person);
    return seller;
  }

  @Override
  public SellPojo entity2Pojo(Sell source) {
    SellPojo target = conversion.convert(source, SellPojo.class);
    SellTypePojo sellType = conversion.convert(source.getSellType(), SellTypePojo.class);
    target.setSellType(sellType);

    ClientPojo client = convertClientToPojo(source);
    target.setClient(client);

    if (source.getSeller() != null) {
      SellerPojo seller = convertSellerToPojo(source);
      target.setSeller(seller);
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
      return repository.deepFindAll(filters, paged);
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
