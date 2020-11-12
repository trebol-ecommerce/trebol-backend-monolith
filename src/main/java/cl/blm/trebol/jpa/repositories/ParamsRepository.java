package cl.blm.trebol.jpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cl.blm.trebol.jpa.GenericRepository;
import cl.blm.trebol.jpa.entities.Param;

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
