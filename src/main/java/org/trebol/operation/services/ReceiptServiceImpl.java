package org.trebol.operation.services;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.repositories.ISalesJpaRepository;
import org.trebol.operation.IReceiptService;
import org.trebol.pojo.ProductPojo;
import org.trebol.pojo.ReceiptDetailPojo;
import org.trebol.pojo.ReceiptPojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptServiceImpl
  implements IReceiptService {

  private final ISalesJpaRepository salesRepository;
  private final ConversionService conversionService;

  @Autowired
  public ReceiptServiceImpl(ISalesJpaRepository salesRepository, ConversionService conversionService) {
    this.salesRepository = salesRepository;
    this.conversionService = conversionService;
  }

  @Override
  public ReceiptPojo fetchReceiptByTransactionToken(String token) throws NotFoundException {
    Optional<Sell> match = salesRepository.findByTransactionToken(token);
    if (match.isEmpty()) {
      throw new NotFoundException("The transaction could not be found, no receipt can be created");
    }
    Sell foundMatch = match.get();

    ReceiptPojo target = conversionService.convert(foundMatch, ReceiptPojo.class);

    if (target != null) {
      List<ReceiptDetailPojo> targetDetails = new ArrayList<>();
      for (SellDetail d : foundMatch.getDetails()) {
        ReceiptDetailPojo targetDetail = conversionService.convert(d, ReceiptDetailPojo.class);
        if (targetDetail != null) {
          Product pd = d.getProduct();
          ProductPojo targetDetailProduct = new ProductPojo();
          targetDetailProduct.setName(pd.getName());
          targetDetailProduct.setBarcode(pd.getBarcode());
          targetDetail.setProduct(targetDetailProduct);
          targetDetail.setUnitValue(d.getUnitValue());
          targetDetails.add(targetDetail);
        }
      }
      target.setDetails(targetDetails);
      target.setStatus(foundMatch.getStatus().getName());
    }

    return target;
  }
}
