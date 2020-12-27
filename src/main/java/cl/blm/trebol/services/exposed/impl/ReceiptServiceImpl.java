package cl.blm.trebol.services.exposed.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import cl.blm.trebol.api.pojo.ProductPojo;
import cl.blm.trebol.api.pojo.ReceiptDetailPojo;
import cl.blm.trebol.api.pojo.ReceiptPojo;
import cl.blm.trebol.jpa.entities.Product;
import cl.blm.trebol.jpa.entities.Sell;
import cl.blm.trebol.jpa.entities.SellDetail;
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
    Optional<Sell> match = salesRepository.findByIdWithDetails(id);
    if (!match.isPresent()) {
      throw new RuntimeException("The transaction could not be found, no receipt can be created");
    }
    Sell foundMatch = match.get();

    ReceiptPojo target = conversionService.convert(foundMatch, ReceiptPojo.class);

    List<ReceiptDetailPojo> targetDetails = new ArrayList<>();
    for (SellDetail d : foundMatch.getDetails()) {
      ReceiptDetailPojo targetDetail = conversionService.convert(d, ReceiptDetailPojo.class);
      Product pd = d.getProduct();
      ProductPojo targetDetailProduct = new ProductPojo();
      targetDetailProduct.setName(pd.getName());
      targetDetailProduct.setBarcode(pd.getBarcode());
      targetDetailProduct.setPrice(pd.getPrice());
      targetDetail.setProduct(targetDetailProduct);
      targetDetails.add(targetDetail);
    }
    target.setDetails(targetDetails);

    return target;
  }
}
