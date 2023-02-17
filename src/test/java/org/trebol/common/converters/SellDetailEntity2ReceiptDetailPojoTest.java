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
import org.trebol.api.models.ReceiptDetailPojo;
import org.trebol.jpa.entities.SellDetail;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.trebol.testing.TestConstants.ANY;

class SellDetailEntity2ReceiptDetailPojoTest {
  final SellDetailEntity2ReceiptDetailPojo instance = new SellDetailEntity2ReceiptDetailPojo();

  @Test
  void converts_selldetail_entities_to_receiptdetail_pojos() {
    List.of(
      SellDetail.builder().build(),
      SellDetail.builder().description(ANY).build(),
      SellDetail.builder().description(ANY).unitValue(1000).build(),
      SellDetail.builder().description(ANY).unitValue(1000).units(2).build()
    ).forEach(selldetail -> {
      ReceiptDetailPojo result = instance.convert(selldetail);
      assertNotNull(result);
      assertEquals(selldetail.getDescription(), result.getDescription());
      assertEquals(selldetail.getUnits(), result.getUnits());
      assertEquals(selldetail.getUnitValue(), result.getUnitValue());
    });
  }
}
