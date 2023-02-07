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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.services.ProductCategoryTreeResolverService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductsPredicateServiceImplTest {
  @InjectMocks ProductsPredicateServiceImpl instance;
  @Mock ProductCategoryTreeResolverService categoryTreeResolverMock;

  @Test
  void parses_map() {
    when(categoryTreeResolverMock.getBranchIdsFromRootCode("category-code")).thenReturn(new ArrayList<>());
    Predicate emptyPredicate = new BooleanBuilder();
    List<Predicate> predicates = List.of(emptyPredicate,
      instance.parseMap(Map.of("id", "1")),
      instance.parseMap(Map.of("barcode", "test1")),
      instance.parseMap(Map.of("name", "test2")),
      instance.parseMap(Map.of("categoryCode", "category-code")),
      instance.parseMap(Map.of("barcodeLike", "portion")),
      instance.parseMap(Map.of("nameLike", "portion")),
      instance.parseMap(Map.of("categoryCodeLike", "portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }
}
