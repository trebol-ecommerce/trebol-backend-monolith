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

package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trebol.jpa.entities.Product;
import org.trebol.pojo.ProductPojo;

@Component
public class Product2Pojo
    implements Converter<Product, ProductPojo> {

  @Override
  public ProductPojo convert(Product source) {
    ProductPojo target = new ProductPojo();
    target.setId(source.getId());
    target.setName(source.getName());
    target.setBarcode(source.getBarcode());
    target.setPrice(source.getPrice());
    target.setCurrentStock(source.getStockCurrent());
    target.setCriticalStock(source.getStockCritical());
    return target;
  }
}
