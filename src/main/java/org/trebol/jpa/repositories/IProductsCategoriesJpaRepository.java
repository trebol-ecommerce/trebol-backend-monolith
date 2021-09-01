package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;


import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IProductsCategoriesJpaRepository
  extends IJpaRepository<ProductCategory> {

  Optional<ProductCategory> findByName(String name);
}
