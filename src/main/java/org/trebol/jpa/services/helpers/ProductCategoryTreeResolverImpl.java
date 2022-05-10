package org.trebol.jpa.services.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trebol.config.OperationProperties;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.repositories.IProductsCategoriesJpaRepository;
import org.trebol.jpa.services.IProductCategoryTreeResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductCategoryTreeResolverImpl
  implements IProductCategoryTreeResolver {

  private final IProductsCategoriesJpaRepository repository;
  private final OperationProperties operationProperties;

  @Autowired
  public ProductCategoryTreeResolverImpl(
          IProductsCategoriesJpaRepository repository,
          OperationProperties operationProperties) {
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
