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

package org.trebol.jpa.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.config.ApiProperties;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.ProductsCategoriesRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class ProductCategoryTreeResolverServiceImplTest {
  @InjectMocks ProductCategoryTreeResolverServiceImpl instance;
  @Mock ProductsCategoriesRepository repositoryMock;
  @Mock ApiProperties apiPropertiesMock;
  List<ProductCategory> descendants;
  List<Long> descendantIds;
  ProductCategory root;

  @BeforeEach
  void beforeEach() {
    root = ProductCategory.builder()
      .id(1L)
      .code("root")
      .name("ROOT")
      .parent(null)
      .build();
    descendants = List.of(
      ProductCategory.builder()
        .id(2L)
        .code(ANY)
        .name(ANY)
        .parent(root)
        .build()
    );
    descendantIds = descendants.stream()
      .map(ProductCategory::getId)
      .collect(Collectors.toList());
  }

  @Test
  void collects_all_descendants_until_max_depth_is_reached() {
    when(apiPropertiesMock.getMaxCategoryFetchingRecursionDepth()).thenReturn(4);
    when(repositoryMock.findByParent(any(ProductCategory.class))).thenReturn(descendants);

    List<ProductCategory> result = instance.getBranchesFromRoot(root);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(5, result.size()); // immediate descendant of root plus four more branches from the tree
    assertEquals(result.get(2), result.get(1));
  }

  @Test
  void given_an_id_collects_all_ids_of_its_descendants_until_max_depth_is_reached() {
    when(apiPropertiesMock.getMaxCategoryFetchingRecursionDepth()).thenReturn(1);
    when(repositoryMock.findIdsByParentId(anyLong())).thenReturn(descendantIds);

    List<Long> result = instance.getBranchIdsFromRootId(root.getId());

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(2, result.size()); // immediate descendant of root and another of its own
  }

  @Test
  void given_a_code_collects_all_ids_of_its_descendants_until_max_depth_is_reached() {
    when(apiPropertiesMock.getMaxCategoryFetchingRecursionDepth()).thenReturn(0);
    when(repositoryMock.findByCode(anyString())).thenReturn(Optional.of(root));
    when(repositoryMock.findIdsByParentId(anyLong())).thenReturn(descendantIds);

    List<Long> result = instance.getBranchIdsFromRootCode(root.getCode());

    verify(repositoryMock).findIdsByParentId(root.getId());
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(1, result.size()); // just the immediate descendant of root
  }

  @Test
  void short_circuits_when_root_by_code_does_not_exist() {
    when(repositoryMock.findByCode(anyString())).thenReturn(Optional.empty());

    List<Long> result = instance.getBranchIdsFromRootCode(root.getCode());

    verify(apiPropertiesMock, times(0)).getMaxCategoryFetchingRecursionDepth();
    verify(repositoryMock, times(0)).findIdsByParentId(anyLong());
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
