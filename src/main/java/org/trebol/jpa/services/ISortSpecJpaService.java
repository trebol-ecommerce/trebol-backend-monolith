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

package org.trebol.jpa.services;

import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.domain.Sort;

import java.util.Map;

/**
 * An interface to support parsing of Maps into sort order clauses to be used in queries at the persistence layer.
 *
 * @param <E> The target @Entity class
 */
public interface ISortSpecJpaService<E> {

  /**
   * Get base QueryDSL type for building OrderSpecifiers
   */
  EntityPathBase<E> getBasePath();

  /**
   * Reads a Map and generates sort order constraints for a specific entity
   *
   * @param queryParamsMap A map of keys and values
   * @return A Sort order as parsed from the input map. May be null if the input is invalid
   */
  Sort parseMap(Map<String, String> queryParamsMap);
}
