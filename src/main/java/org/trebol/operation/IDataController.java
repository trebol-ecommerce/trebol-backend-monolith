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

package org.trebol.operation;

import org.trebol.pojo.DataPagePojo;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Interface for API controllers that handle reading of data stored in a persistence context and
 * return it converted to the parameterized class, presumably a Pojo
 * @param <P> The Pojo class
 */
public interface IDataController<P> {

  /**
   * Get a paged collection of Pojos.
   * @param requestParams A map of key/value String pairs, that may be parsed as sorting, pagination and filtering
   *                      conditions.
   * @return An instance of DataPagePojo, with the items and page information.
   */
  DataPagePojo<P> readMany(@NotNull Map<String, String> requestParams);
}
