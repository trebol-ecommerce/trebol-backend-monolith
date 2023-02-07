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

package org.trebol.common.converters;

import org.junit.jupiter.api.Test;
import org.trebol.api.models.ReceiptPojo;
import org.trebol.jpa.entities.Sell;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.trebol.testing.TestConstants.ANY;

class SellEntity2ReceiptPojoTest {
  final SellEntity2ReceiptPojo instance = new SellEntity2ReceiptPojo();

  @Test
  void converts_sell_entities_to_receipt_pojos() {
    List.of(
      sellWithId(),
      sellWithIdAndDate(),
      sellWithIdAndDateAndTotalItems(),
      sellWithIdAndDateAndTotalItemsAndMonetaryValues(),
      sellWithIdAndDateAndTotalItemsAndMonetaryValuesAndToken()
    ).forEach(sell -> {
      ReceiptPojo result = instance.convert(sell);
      assertNotNull(result);
      assertEquals(sell.getId(), result.getBuyOrder());
      assertEquals(sell.getDate(), result.getDate());
      assertEquals(sell.getTotalItems(), result.getTotalItems());
      assertEquals(sell.getTransportValue(), result.getTransportValue());
      assertEquals(sell.getTaxesValue(), result.getTaxValue());
      assertEquals(sell.getTotalValue(), result.getTotalValue());
      assertEquals(sell.getTransactionToken(), result.getToken());
    });
  }

  @Test
  void cannot_convert_incomplete_sell_entities_to_receipt_pojos() {
    List.of(
      new Sell()
    ).forEach(sell -> {
      assertThrows(Exception.class, () -> instance.convert(sell));
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
    Sell sell = sellWithIdAndDateAndTotalItems();
    sell.setTransportValue(0);
    sell.setTaxesValue(150);
    sell.setTotalValue(2500);
    return sell;
  }

  Sell sellWithIdAndDateAndTotalItemsAndMonetaryValuesAndToken() {
    Sell sell = sellWithIdAndDateAndTotalItemsAndMonetaryValues();
    sell.setTransactionToken(ANY);
    return sell;
  }
}
