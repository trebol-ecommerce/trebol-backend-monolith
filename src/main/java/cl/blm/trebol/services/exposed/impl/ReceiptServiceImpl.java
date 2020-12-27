package cl.blm.trebol.services.exposed.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import cl.blm.trebol.api.pojo.ReceiptPojo;
import cl.blm.trebol.jpa.repositories.SalesRepository;
import cl.blm.trebol.services.exposed.ReceiptService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class ReceiptServiceImpl
    implements ReceiptService {

  private final SalesRepository salesRepository;
  private final ConversionService conversionService;

  @Autowired
  public ReceiptServiceImpl(
      SalesRepository salesRepository,
      ConversionService conversionService) {
    this.salesRepository = salesRepository;
    this.conversionService = conversionService;
  }

  @Override
  public ReceiptPojo fetchReceiptById(int id) {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
