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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trebol.api.models.ImagePojo;
import org.trebol.jpa.entities.Image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.trebol.testing.TestConstants.ANY;

class ImagesConverterServiceImplTest {
  ImagesConverterServiceImpl instance;

  @BeforeEach
  void beforeEach() {
    instance = new ImagesConverterServiceImpl();
  }

  @Test
  void testConvertToPojo() {
    Image input = Image.builder()
      .id(1L)
      .code(ANY)
      .filename(ANY)
      .url(ANY)
      .build();
    ImagePojo result = instance.convertToPojo(input);
    assertNotNull(result);
    assertEquals(input.getId(), result.getId());
    assertEquals(input.getFilename(), result.getFilename());
    assertEquals(input.getCode(), result.getCode());
  }

  @Test
  void testConvertToNewEntity() {
    ImagePojo input = ImagePojo.builder()
      .code(ANY)
      .filename(ANY)
      .url(ANY)
      .build();
    Image result = instance.convertToNewEntity(input);
    assertNotNull(result);
    assertEquals(input.getFilename(), result.getFilename());
    assertEquals(input.getCode(), result.getCode());
    assertEquals(input.getUrl(), result.getUrl());
  }
}
