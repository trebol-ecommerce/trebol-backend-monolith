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

package org.trebol.api;

import org.trebol.api.models.DataPagePojo;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * {@link org.springframework.web.bind.annotation.RestController} that handles requests for reading data from a persistence context.
 *
 * @param <M> Its signature model class
 */
public interface DataController<M> {

  /**
   * Get a paged collection of data.
   *
   * @param requestParams A {@link java.util.Map} of key/value String pairs containing the parameters for reading the data.
   * @return An instance of {@link org.trebol.api.models.DataPagePojo} containing the data itself, and information about that page of data.
   */
  DataPagePojo<M> readMany(@NotNull Map<String, String> requestParams);
}
