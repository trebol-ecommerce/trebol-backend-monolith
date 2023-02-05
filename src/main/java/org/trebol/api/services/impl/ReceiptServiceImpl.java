/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.api.models.ProductPojo;
import org.trebol.api.models.ReceiptDetailPojo;
import org.trebol.api.models.ReceiptPojo;
import org.trebol.api.services.ReceiptService;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellDetail;
import org.trebol.jpa.repositories.SalesRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptServiceImpl
  implements ReceiptService {
  private final SalesRepository salesRepository;
  private final ConversionService conversionService;

  @Autowired
  public ReceiptServiceImpl(
    SalesRepository salesRepository,
    ConversionService conversionService
  ) {
    this.salesRepository = salesRepository;
    this.conversionService = conversionService;
  }

  @Override
  public ReceiptPojo fetchReceiptByTransactionToken(String token)
    throws EntityNotFoundException {
    Optional<Sell> match = salesRepository.findByTransactionToken(token);
    if (match.isEmpty()) {
      throw new EntityNotFoundException("The transaction could not be found, no receipt can be created");
    }
    Sell foundMatch = match.get();

    ReceiptPojo target = conversionService.convert(foundMatch, ReceiptPojo.class);

    if (target != null) {
      List<ReceiptDetailPojo> targetDetails = new ArrayList<>();
      for (SellDetail d : foundMatch.getDetails()) {
        ReceiptDetailPojo targetDetail = conversionService.convert(d, ReceiptDetailPojo.class);
        if (targetDetail != null) {
          Product pd = d.getProduct();
          ProductPojo targetDetailProduct = ProductPojo.builder().name(pd.getName()).barcode(pd.getBarcode()).build();
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
