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

package org.trebol.jpa.services.predicates.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.jpa.services.ProductCategoryTreeResolverService;
import org.trebol.jpa.services.predicates.ProductsPredicateService;

import java.util.List;
import java.util.Map;

@Service
public class ProductsPredicateServiceImpl
  implements ProductsPredicateService {
  private final Logger logger = LoggerFactory.getLogger(ProductsPredicateServiceImpl.class);
  private final ProductCategoryTreeResolverService categoryTreeResolver;

  @Autowired
  public ProductsPredicateServiceImpl(
    ProductCategoryTreeResolverService categoryTreeResolver
  ) {
    this.categoryTreeResolver = categoryTreeResolver;
  }

  @Override
  public Predicate parseMap(Map<String, String> queryParamsMap) {
    BooleanBuilder predicate = new BooleanBuilder();
    for (Map.Entry<String, String> entry : queryParamsMap.entrySet()) {
      String paramName = entry.getKey();
      String stringValue = entry.getValue();
      try {
        switch (paramName) {
          case "id":
            return basePath.id.eq(Long.valueOf(stringValue));
          case "barcode":
            return basePath.barcode.eq(stringValue);
          case "name":
            return basePath.name.eq(stringValue);
          case "barcodeLike":
            predicate.and(basePath.barcode.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "nameLike":
            predicate.and(basePath.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "categoryCode":
            List<Long> branchIds = categoryTreeResolver.getBranchIdsFromRootCode(stringValue);
            predicate.and(basePath.productCategory.code.eq(stringValue)
              .or(basePath.productCategory.id.in(branchIds)));
            break;
          case "categoryCodeLike":
            predicate.and(basePath.productCategory.code.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }
}
