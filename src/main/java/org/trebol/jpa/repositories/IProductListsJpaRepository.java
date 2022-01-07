package org.trebol.jpa.repositories;

import org.springframework.stereotype.Repository;
import org.trebol.jpa.IJpaRepository;
import org.trebol.jpa.entities.ProductList;

import java.util.Optional;

@Repository
public interface IProductListsJpaRepository
  extends IJpaRepository<ProductList> {

  Optional<ProductList> findByName(String name);
}
