package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <T>
 */
@JsonInclude
public class DataPagePojo<T> {
  private Collection<T> items;
  private int pageIndex;
  private long totalCount;
  private int pageSize;

  public DataPagePojo(int pageIndex, int pageSize) {
    this.items = new ArrayList<>();
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  public DataPagePojo(
    Collection<T> items,
    int pageIndex,
    long totalCount,
    int pageSize
  ) {
    this.items = items;
    this.pageIndex = pageIndex;
    this.totalCount = totalCount;
    this.pageSize = pageSize;
  }

  public Collection<T> getItems() {
    return items;
  }

  public void setItems(Collection<T> items) {
    this.items = items;
  }

  public int getPageIndex() {
    return pageIndex;
  }

  public void setPageIndex(int pageIndex) {
    this.pageIndex = pageIndex;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataPagePojo<?> that = (DataPagePojo<?>) o;
    return pageIndex == that.pageIndex &&
        totalCount == that.totalCount &&
        pageSize == that.pageSize &&
        Objects.equals(items, that.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(items, pageIndex, totalCount, pageSize);
  }

  @Override
  public String toString() {
    return "DataPagePojo{" +
        "items=" + items +
        ", pageIndex=" + pageIndex +
        ", totalCount=" + totalCount +
        ", pageSize=" + pageSize +
        '}';
  }
}
