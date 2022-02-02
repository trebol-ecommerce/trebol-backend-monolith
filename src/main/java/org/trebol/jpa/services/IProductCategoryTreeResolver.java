package org.trebol.jpa.services;

import org.trebol.jpa.entities.ProductCategory;

import javax.validation.constraints.Positive;
import java.util.List;

public interface IProductCategoryTreeResolver {
  /**
   * Recursively fetches all categories descendants of a given "root" category.
   * @param rootBranch A category that other categories may be related to.
   * @return A list with all categories that are descendant of the provided one
   */
  List<ProductCategory> getBranchesFromRoot(ProductCategory rootBranch);

  /**
   * Recursively fetches all categories descendants of a given "root" category, up to a certain
   * amount of recursion.
   * @param rootBranch A category that other categories may be related to
   * @param depth The amount of times that branches will be recursively added to the returned list.
   *              A value of 1 should gather the immediate descendants of the root branch.
   * @return A list with all categories that are descendant of the provided one,
   *          up to the specified level of depth/recursion.
   */
  List<ProductCategory> getBranchesFromRoot(ProductCategory rootBranch, @Positive int depth);

  /**
   * Recursively fetches the Ids of all categories descendants of a given "root" category.
   * @param rootId The Id of a category that other categories may be related to
   * @return A list with the Ids of all categories that are descendant of the provided one
   */
  List<Long> getBranchIdsFromRootId(Long rootId);

  /**
   * Recursively fetches the Ids of all categories descendants of a given "root" category.
   * @param rootCode The code of a category that other categories may be related to
   * @return A list with the Ids of all categories that are descendant of the provided one
   */
  List<Long> getBranchIdsFromRootCode(String rootCode);
}
