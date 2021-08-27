package org.trebol.api;

import java.util.Collection;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 * @param <T>
 */
@JsonInclude
public class DataPage<T extends Object> {
  private Collection<T> items;
  private int pageIndex;
  private long totalCount;
  private int pageSize;

  public DataPage() { }

  public DataPage(
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
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.items);
    hash = 37 * hash + this.pageIndex;
    hash = 37 * hash + (int)(this.totalCount ^ (this.totalCount >>> 32));
    hash = 37 * hash + this.pageSize;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DataPage<?> other = (DataPage<?>)obj;
    if (this.pageIndex != other.pageIndex) {
      return false;
    }
    if (this.totalCount != other.totalCount) {
      return false;
    }
    if (this.pageSize != other.pageSize) {
      return false;
    }
    if (!Objects.equals(this.items, other.items)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "DataPage{items=" + items
        + ", pageIndex=" + pageIndex
        + ", totalCount=" + totalCount
        + ", pageSize=" + pageSize + '}';
  }
}
