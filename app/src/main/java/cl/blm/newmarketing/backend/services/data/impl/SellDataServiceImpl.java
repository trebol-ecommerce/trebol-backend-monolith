package cl.blm.newmarketing.backend.services.data.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.backend.api.pojo.ClientPojo;
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

  private ConversionService conversion;

  @Autowired
  public SellDataServiceImpl(SalesRepository sales, ConversionService conversion) {
    super(LOG, sales);
    this.conversion = conversion;
  }

  private Collection<SellDetailPojo> convertCollection2Pojo(Collection<SellDetail> sourceCollection) {
    List<SellDetailPojo> targetList = new ArrayList<>();
    for (SellDetail sourceItem : sourceCollection) {
      SellDetailPojo targetItem = conversion.convert(sourceItem, SellDetailPojo.class);
      targetList.add(targetItem);
    }
    return targetList;
  }

  @Override
  public SellPojo entity2Pojo(Sell source) {
    SellPojo target = conversion.convert(source, SellPojo.class);
    SellTypePojo sellType = conversion.convert(source.getSellType(), SellTypePojo.class);
    target.setSellType(sellType);
    ClientPojo client = conversion.convert(source.getClient(), ClientPojo.class);
    target.setClient(client);
    Collection<SellDetailPojo> sellDetails = convertCollection2Pojo(source.getSellDetails());
    target.setSellDetails(sellDetails);
    if (source.getSeller() != null) {
      SellerPojo seller = conversion.convert(source.getSeller(), SellerPojo.class);
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
}
