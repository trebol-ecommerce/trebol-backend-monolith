package cl.blm.trebol.converters;

import java.text.SimpleDateFormat;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.ReceiptPojo;
import cl.blm.trebol.jpa.entities.Sell;

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
