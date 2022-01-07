package org.trebol.jpa.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.trebol.jpa.IJpaRepository;
import org.trebol.jpa.entities.ProductListItem;

@Repository
public interface IProductListItemsJpaRepository
  extends IJpaRepository<ProductListItem> {

  @Modifying
  @Transactional
  @Query("DELETE FROM ProductListItem pi WHERE pi.list.id = :id")
  void deleteByListId(@Param("id") Long id);

  @Modifying
  @Transactional
  @Query("DELETE FROM ProductListItem pi WHERE pi.product.id = :id")
  void deleteByProductId(@Param("id") Long id);
}
