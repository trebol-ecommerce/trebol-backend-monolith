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

package org.trebol.jpa.services.conversion.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.ShipperPojo;
import org.trebol.jpa.entities.Shipper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ShippersConverterServiceImplTest {
  @InjectMocks ShippersConverterServiceImpl sut;
  Shipper shipper;
  ShipperPojo shipperPojo;

  @BeforeEach
  void beforeEach() {
    shipper = new Shipper();
    shipper.setName(ANY);
    shipper.setId(1L);
    shipper.setName(ANY);
    shipperPojo = ShipperPojo.builder()
      .name(ANY)
      .build();
  }

  @AfterEach
  void afterEach() {
    shipper = null;
    shipperPojo = null;
  }

  @Test
  void testConvertToPojo() {
    ShipperPojo actual = sut.convertToPojo(shipper);
    assertEquals(shipper.getName(), actual.getName());
  }

  @Test
  void testConvertToNewEntity() {
    Shipper actual = sut.convertToNewEntity(shipperPojo);
    assertEquals(shipperPojo.getName(), actual.getName());
  }
}