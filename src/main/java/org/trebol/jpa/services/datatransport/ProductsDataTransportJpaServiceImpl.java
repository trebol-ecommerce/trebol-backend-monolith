/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

package org.trebol.jpa.services.datatransport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Product;
import org.trebol.jpa.services.IDataTransportJpaService;
import org.trebol.pojo.ProductPojo;

@Transactional
@Service
public class ProductsDataTransportJpaServiceImpl
  implements IDataTransportJpaService<ProductPojo, Product> {

  public ProductsDataTransportJpaServiceImpl(
  ) {
  }

  @Override
  public Product applyChangesToExistingEntity(ProductPojo source, Product existing) throws BadInputException {
    Product target = new Product(existing);

    String barcode = source.getBarcode();
    if (barcode != null && !barcode.isBlank() && !target.getBarcode().equals(barcode)) {
      target.setBarcode(barcode);
    }

    String name = source.getName();
    if (name != null && !name.isBlank() && !target.getName().equals(name)) {
      target.setName(name);
    }

    Integer price = source.getPrice();
    target.setPrice(price);

    String description = source.getDescription();
    if (description != null) {
      target.setDescription(description);
    }

    Integer currentStock = source.getCurrentStock();
    if (currentStock != null) {
      target.setStockCurrent(currentStock);
    }

    return target;
  }
}