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
import org.trebol.config.ApiProperties;
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
  private final ApiProperties apiProperties;

  @Autowired
  public ProductCategoryTreeResolverServiceImpl(
    ProductsCategoriesRepository repository,
    ApiProperties apiProperties
  ) {
    this.repository = repository;
    this.apiProperties = apiProperties;
  }

  @Override
  public List<ProductCategory> getBranchesFromRoot(ProductCategory rootBranch) {
    int maxAllowedDepth = apiProperties.getMaxCategoryFetchingRecursionDepth();
    List<ProductCategory> immediateDescendants = repository.findByParent(rootBranch);
    List<ProductCategory> allBranches = new ArrayList<>(immediateDescendants);
    for (ProductCategory branch : immediateDescendants) {
      this.recursivelyAddBranches(allBranches, branch, maxAllowedDepth);
    }
    return allBranches;
  }

  @Override
  public List<Long> getBranchIdsFromRootId(Long rootId) {
    int depth = apiProperties.getMaxCategoryFetchingRecursionDepth();
    List<Long> immediateDescendantIds = repository.findIdsByParentId(rootId);
    List<Long> allBranchIds = new ArrayList<>(immediateDescendantIds);
    for (Long bId : immediateDescendantIds) {
      this.recursivelyAddBranchIds(allBranchIds, bId, depth);
    }
    return allBranchIds;
  }

  @Override
  public List<Long> getBranchIdsFromRootCode(String rootCode) {
    Optional<ProductCategory> byCode = repository.findByCode(rootCode);
    if (byCode.isEmpty()) {
      return List.of();
    }
    return this.getBranchIdsFromRootId(byCode.get().getId());
  }

  private void recursivelyAddBranches(Collection<ProductCategory> allBranches, ProductCategory branch, int deepnessLeft) {
    if (deepnessLeft > 0) {
      List<ProductCategory> subBranchIds = repository.findByParent(branch);
      for (ProductCategory bId2 : subBranchIds) {
        allBranches.add(bId2);
        this.recursivelyAddBranches(allBranches, bId2, deepnessLeft - 1);
      }
    }
  }

  private void recursivelyAddBranchIds(Collection<Long> branchIds, Long bId, int deepnessLeft) {
    if (deepnessLeft > 0) {
      List<Long> subBranchIds = repository.findIdsByParentId(bId);
      for (Long bId2 : subBranchIds) {
        branchIds.add(bId2);
        this.recursivelyAddBranchIds(branchIds, bId2, deepnessLeft - 1);
      }
    }
  }
}
