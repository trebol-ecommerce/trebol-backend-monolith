package org.trebol.jpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.GenericRepository;
import org.trebol.jpa.entities.Param;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ParamsRepository
    extends GenericRepository<Param, Integer> {

  @Query("SELECT p FROM Param p WHERE p.category = :category")
  public Iterable<Param> findParamsByCategory(
      @org.springframework.data.repository.query.Param("category") String category);
}
