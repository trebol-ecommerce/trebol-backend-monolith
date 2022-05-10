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

package org.trebol.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Positive;

@Configuration
@ConfigurationProperties(prefix = "trebol.operation")
public class OperationProperties {
  private Integer itemsPerPage;
  private Integer maxAllowedPageSize;
  @Positive
  private int maxCategoryFetchingRecursionDepth;

  public Integer getMaxAllowedPageSize() {
    return maxAllowedPageSize;
  }

  public void setMaxAllowedPageSize(Integer maxAllowedPageSize) {
    this.maxAllowedPageSize = maxAllowedPageSize;
  }

  public Integer getItemsPerPage() {
    return itemsPerPage;
  }

  public void setItemsPerPage(Integer itemsPerPage) {
    this.itemsPerPage = itemsPerPage;
  }

  public int getMaxCategoryFetchingRecursionDepth() {
    return maxCategoryFetchingRecursionDepth;
  }

  public void setMaxCategoryFetchingRecursionDepth(int maxCategoryFetchingRecursionDepth) {
    this.maxCategoryFetchingRecursionDepth = maxCategoryFetchingRecursionDepth;
  }
}
