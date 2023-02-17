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

package org.trebol.jpa.services;

import org.trebol.jpa.entities.ProductCategory;

import java.util.List;

public interface ProductCategoryTreeResolverService {
  /**
   * Recursively fetches all categories descendants of a given "root" category.
   *
   * @param rootBranch A category that other categories may be related to.
   * @return A list with all categories that are descendant of the provided one
   */
  List<ProductCategory> getBranchesFromRoot(ProductCategory rootBranch);

  /**
   * Recursively fetches the Ids of all categories descendants of a given "root" category.
   *
   * @param rootId The Id of a category that other categories may be related to
   * @return A list with the Ids of all categories that are descendant of the provided one
   */
  List<Long> getBranchIdsFromRootId(Long rootId);

  /**
   * Recursively fetches the Ids of all categories descendants of a given "root" category.
   *
   * @param rootCode The code of a category that other categories may be related to
   * @return A list with the Ids of all categories that are descendant of the provided one
   */
  List<Long> getBranchIdsFromRootCode(String rootCode);
}
