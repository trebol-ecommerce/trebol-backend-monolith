package org.trebol.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trebol.jpa.entities.Sell;
import org.trebol.pojo.ReceiptPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellEntity2ReceiptPojo
    implements Converter<Sell, ReceiptPojo> {

  @Override
  public ReceiptPojo convert(Sell source) {
    ReceiptPojo target = new ReceiptPojo();
    target.setBuyOrder(source.getId());
    target.setDate(source.getDate());
    target.setTransportValue(source.getTransportValue());
    target.setTaxValue(source.getTaxesValue());
    target.setTotalItems(source.getTotalItems());
    target.setTotalValue(source.getTotalValue());
    target.setToken(source.getTransactionToken());
    target.setAmount(source.getTotalValue());
    return target;
  }
}
