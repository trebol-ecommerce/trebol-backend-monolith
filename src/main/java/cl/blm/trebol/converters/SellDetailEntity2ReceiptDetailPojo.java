package cl.blm.trebol.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.trebol.api.pojo.ReceiptDetailPojo;
import cl.blm.trebol.jpa.entities.SellDetail;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class SellDetailEntity2ReceiptDetailPojo
    implements Converter<SellDetail, ReceiptDetailPojo> {

  @Override
  public ReceiptDetailPojo convert(SellDetail source) {
    ReceiptDetailPojo target = new ReceiptDetailPojo();
    target.setUnits(source.getUnits());
    return target;
  }
}
