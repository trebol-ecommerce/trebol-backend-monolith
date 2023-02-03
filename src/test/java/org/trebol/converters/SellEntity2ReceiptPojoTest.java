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

package org.trebol.converters;

import org.junit.jupiter.api.Test;
import org.trebol.jpa.entities.Sell;
import org.trebol.pojo.ReceiptPojo;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.trebol.constant.TestConstants.ANY;

public class SellEntity2ReceiptPojoTest {
  SellEntity2ReceiptPojo instance = new SellEntity2ReceiptPojo();

  @Test
  void converts_selldetail_entities_to_receiptdetail_pojos() {
    List.of(
      new Sell(),
      sellWithId(),
      sellWithIdAndDate(),
      sellWithIdAndDateAndTotalItems(),
      sellWithIdAndDateAndTotalItemsAndMonetaryValues(),
      sellWithIdAndDateAndTotalItemsAndMonetaryValuesAndToken()
    ).forEach(selldetail -> {
      ReceiptPojo result = instance.convert(selldetail);
      assertNotNull(result);
      assertEquals(selldetail.getId(), result.getBuyOrder());
      assertEquals(selldetail.getDate(), result.getDate());
      assertEquals(selldetail.getTotalItems(), result.getTotalItems());
      assertEquals(selldetail.getTransportValue(), result.getTransportValue());
      assertEquals(selldetail.getTaxesValue(), result.getTaxValue());
      assertEquals(selldetail.getTotalValue(), result.getTotalValue());
      assertEquals(selldetail.getTransactionToken(), result.getToken());
    });
  }

  Sell sellWithId() {
    Sell sell = new Sell();
    sell.setId(1000L);
    return sell;
  }

  Sell sellWithIdAndDate() {
    Sell sell = sellWithId();
    sell.setDate(Instant.now());
    return sell;
  }

  Sell sellWithIdAndDateAndTotalItems() {
    Sell sell = sellWithIdAndDate();
    sell.setTotalItems(4);
    return sell;
  }

  Sell sellWithIdAndDateAndTotalItemsAndMonetaryValues() {
    Sell sellDetail = sellWithIdAndDateAndTotalItems();
    sellDetail.setTransportValue(0);
    sellDetail.setTaxesValue(150);
    sellDetail.setTotalValue(2500);
    return sellDetail;
  }

  Sell sellWithIdAndDateAndTotalItemsAndMonetaryValuesAndToken() {
    Sell sellDetail = sellWithIdAndDateAndTotalItemsAndMonetaryValues();
    sellDetail.setTransactionToken(ANY);
    return sellDetail;
  }
}
