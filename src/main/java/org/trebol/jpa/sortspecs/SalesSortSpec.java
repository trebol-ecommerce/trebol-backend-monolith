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

package org.trebol.jpa.sortspecs;

import com.querydsl.core.types.OrderSpecifier;
import lombok.NoArgsConstructor;
import org.trebol.jpa.entities.QPerson;
import org.trebol.jpa.entities.QSell;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SalesSortSpec {
  private static final QSell BASE_PATH = QSell.sell;
  private static final QPerson CUSTOMER_PATH = BASE_PATH.customer.person;
  public static final Map<String, OrderSpecifier<?>> ORDER_SPEC_MAP = Map.of(
    "buyOrder", BASE_PATH.id.asc(),
    "date", BASE_PATH.date.asc(),
    "status", BASE_PATH.status.code.asc(),
    "customer", CUSTOMER_PATH.lastName.asc(),
    "shipper", BASE_PATH.shipper.name.asc(),
    "totalValue", BASE_PATH.totalValue.asc(),
    "netValue", BASE_PATH.netValue.asc(),
    "totalItems", BASE_PATH.totalItems.asc(),
    "transportValue", BASE_PATH.transportValue.asc()
  );
}
