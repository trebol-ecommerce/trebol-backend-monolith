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

package org.trebol.config.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.trebol.testing.TestConstants.ANY;

class CorsMappingParseExceptionTest {

  @Test
  void has_a_default_error_message() {
    CorsMappingParseException instance = assertThrows(CorsMappingParseException.class, () -> {
      throw new CorsMappingParseException(null);
    });
    assertNull(instance.getCorsMapping());
    assertNotNull(instance.getMessage());
    assertEquals(CorsMappingParseException.BASE_MESSAGE, instance.getMessage());
  }

  @Test
  void holds_the_failing_cors_mapping_which_triggered_the_exception() {
    String corsMapping = ANY;
    CorsMappingParseException instance = assertThrows(CorsMappingParseException.class, () -> {
      throw new CorsMappingParseException(corsMapping);
    });
    assertNotNull(instance.getCorsMapping());
    assertEquals(corsMapping, instance.getCorsMapping());
  }
}
