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

import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;

import java.util.Map;

public abstract class GenericSortSpecJpaService<E>
  implements ISortSpecJpaService<E> {

  private Map<String, OrderSpecifier<?>> orderSpecMap;

  protected abstract Map<String, OrderSpecifier<?>> createOrderSpecMap();

  @Override
  public Sort parseMap(Map<String, String> queryParamsMap) {
    String propertyName = queryParamsMap.get("sortBy");
    OrderSpecifier<?> orderSpecifier = this.getOrderSpecMap().get(propertyName);
    Sort sortBy = QSort.by(orderSpecifier);
    switch (queryParamsMap.get("order")) {
      case "asc":
        return sortBy.ascending();
      case "desc":
        return sortBy.descending();
      default:
        return sortBy;
    }
  }

  private Map<String, OrderSpecifier<?>> getOrderSpecMap() {
    if (this.orderSpecMap == null) {
      this.orderSpecMap = this.createOrderSpecMap();
    }
    return this.orderSpecMap;
  }
}
