package org.trebol.jpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.Param;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IParamsJpaRepository
    extends IJpaRepository<Param> {

  @Query("SELECT p FROM Param p WHERE p.category = :category")
  Iterable<Param> findParamsByCategory(
      @org.springframework.data.repository.query.Param("category") String category);
}
