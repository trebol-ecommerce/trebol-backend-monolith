package org.trebol.converters;

import java.text.SimpleDateFormat;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.ReceiptPojo;
import org.trebol.jpa.entities.Sell;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellEntity2ReceiptPojo
    implements Converter<Sell, ReceiptPojo> {

  @Override
  public ReceiptPojo convert(Sell source) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
    ReceiptPojo target = new ReceiptPojo();
    target.setBuyOrder(source.getId());
    target.setDate(dateFormat.format(source.getDate()));
    target.setAmount(source.getTotalValue());
    return target;
  }
}
