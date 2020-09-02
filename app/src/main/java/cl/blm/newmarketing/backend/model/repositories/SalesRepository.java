package cl.blm.newmarketing.backend.model.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.blm.newmarketing.backend.model.GenericRepository;
import cl.blm.newmarketing.backend.model.entities.Sell;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface SalesRepository
    extends GenericRepository<Sell, Integer> {

  @Query(value = "SELECT s FROM Sell s JOIN FETCH s.sellType JOIN FETCH s.sellDetails JOIN FETCH s.client "
      + "JOIN FETCH s.seller WHERE s.id = :id")
  Optional<Sell> deepFindById(@Param("id") Integer id);
}
