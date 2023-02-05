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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.ProductsCategoriesRepository;
import org.trebol.jpa.services.ProductCategoryTreeResolverService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryTreeResolverServiceImpl
  implements ProductCategoryTreeResolverService {
  private final ProductsCategoriesRepository repository;
  private final OperationProperties operationProperties;

  @Autowired
  public ProductCategoryTreeResolverServiceImpl(
    ProductsCategoriesRepository repository,
    OperationProperties operationProperties
  ) {
    this.repository = repository;
    this.operationProperties = operationProperties;
  }

  @Override
  public List<ProductCategory> getBranchesFromRoot(ProductCategory rootBranch) {
    int depth = operationProperties.getMaxCategoryFetchingRecursionDepth();
    List<ProductCategory> immediateDescendants = repository.findByParent(rootBranch);
    List<ProductCategory> allBranches = new ArrayList<>(immediateDescendants);
    for (ProductCategory b : immediateDescendants) {
      this.recursivelyAddBranches(allBranches, b, depth);
    }
    return allBranches;
  }

  @Override
  public List<Long> getBranchIdsFromRootId(Long rootId) {
    int depth = operationProperties.getMaxCategoryFetchingRecursionDepth();
    List<Long> immediateDescendantIds = repository.findIdsByParentId(rootId);
    List<Long> allBranches = new ArrayList<>(immediateDescendantIds);
    for (Long bId : immediateDescendantIds) {
      this.recursivelyAddBranchIds(allBranches, bId, depth);
    }
    return allBranches;
  }

  @Override
  public List<Long> getBranchIdsFromRootCode(String rootCode) {
    Optional<ProductCategory> byCode = repository.findByCode(rootCode);
    if (byCode.isEmpty()) {
      return List.of();
    }
    return this.getBranchIdsFromRootId(byCode.get().getId());
  }

  private void recursivelyAddBranches(Collection<ProductCategory> branchIds, ProductCategory bId, int depth) {
    int currentDepth = (depth - 1);
    List<ProductCategory> subBranchIds = repository.findByParent(bId);
    for (ProductCategory bId2 : subBranchIds) {
      branchIds.add(bId2);
      if (currentDepth > 0) {
        this.recursivelyAddBranches(branchIds, bId2, currentDepth);
      }
    }
  }

  private void recursivelyAddBranchIds(Collection<Long> branchIds, Long bId, int depth) {
    int currentDepth = (depth - 1);
    List<Long> subBranchIds = repository.findIdsByParentId(bId);
    for (Long bId2 : subBranchIds) {
      branchIds.add(bId2);
      if (currentDepth > 0) {
        this.recursivelyAddBranchIds(branchIds, bId2, currentDepth);
      }
    }
  }
}
