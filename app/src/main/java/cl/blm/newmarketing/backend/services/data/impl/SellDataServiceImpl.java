package cl.blm.newmarketing.backend.services.data.impl;

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
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.api.pojo.ClientPojo;
import cl.blm.newmarketing.backend.api.pojo.PersonPojo;
import cl.blm.newmarketing.backend.api.pojo.ProductPojo;
import cl.blm.newmarketing.backend.api.pojo.SellDetailPojo;
import cl.blm.newmarketing.backend.api.pojo.SellPojo;
import cl.blm.newmarketing.backend.api.pojo.SellTypePojo;
import cl.blm.newmarketing.backend.api.pojo.SellerPojo;
import cl.blm.newmarketing.backend.model.entities.QSell;
import cl.blm.newmarketing.backend.model.entities.Sell;
import cl.blm.newmarketing.backend.model.entities.SellDetail;
import cl.blm.newmarketing.backend.model.repositories.SalesRepository;
import cl.blm.newmarketing.backend.services.data.GenericDataService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SellDataServiceImpl
    extends GenericDataService<SellPojo, Sell, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(SellDataServiceImpl.class);

  private SalesRepository repository;
  private ConversionService conversion;

  @Autowired
  public SellDataServiceImpl(SalesRepository repository, ConversionService conversion) {
    super(LOG, repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  private ClientPojo convertClient(Sell source) {
    ClientPojo client = conversion.convert(source.getClient(), ClientPojo.class);
    PersonPojo person = conversion.convert(source.getClient().getPerson(), PersonPojo.class);
    client.setPerson(person);
    return client;
  }

  private List<SellDetailPojo> convertDetails(Sell source) {
    List<SellDetailPojo> sellDetails = new ArrayList<>();
    for (SellDetail sourceSellDetail : source.getSellDetails()) {
      SellDetailPojo targetSellDetail = conversion.convert(sourceSellDetail, SellDetailPojo.class);
      // TODO is there a problem fetching PRODUCT data ?
      ProductPojo product = conversion.convert(sourceSellDetail.getProduct(), ProductPojo.class);
      targetSellDetail.setProduct(product);
      sellDetails.add(targetSellDetail);
    }
    return sellDetails;
  }

  private SellerPojo convertSeller(Sell source) {
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

    ClientPojo client = convertClient(source);
    target.setClient(client);

    List<SellDetailPojo> sellDetails = convertDetails(source);
    target.setSellDetails(sellDetails);

    if (source.getSeller() != null) {
      SellerPojo seller = convertSeller(source);
      target.setSeller(seller);
    }

    return target;
  }

  @Override
  public Sell pojo2Entity(SellPojo source) {
    return conversion.convert(source, Sell.class);
  }

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

  /**
   * Convert a pojo to an entity, save it, fetch it back with all recursive data,
   * convert it all back to pojo and return it.
   */
  @Nullable
  @Override
  public SellPojo create(SellPojo inputPojo) {
    LOG.debug("create({})", inputPojo);
    Sell input = pojo2Entity(inputPojo);
    Sell output = repository.saveAndFlush(input);
    SellPojo outputPojo = find(output.getId());
    return outputPojo;
  }

  @Nullable
  @Override
  public SellPojo find(Integer id) {
    LOG.debug("find({})", id);
    Optional<Sell> personById = repository.deepFindById(id);
    if (!personById.isPresent()) {
      return null;
    } else {
      Sell found = personById.get();
      SellPojo foundPojo = entity2Pojo(found);
      return foundPojo;
    }
  }
}
