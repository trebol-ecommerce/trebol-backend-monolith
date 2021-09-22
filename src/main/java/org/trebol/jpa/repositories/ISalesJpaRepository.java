package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ISalesJpaRepository
  extends IJpaRepository<Sell> {

  Optional<Sell> findByTransactionToken(String token);

  @Query(value = "SELECT s FROM Sell s "
      + "JOIN FETCH s.details "
      + "WHERE s.id = :id")
  Optional<Sell> findByIdWithDetails(@Param("id") Long id);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Sell s "
      + "SET s.status = :status "
      + "WHERE s.id = :id")
  int setStatus(@Param("id") Long id, @Param("status") SellStatus status);

  @Modifying
  @Query("UPDATE Sell s "
      + "SET s.transactionToken = :token "
      + "WHERE s.id = :id")
  int setTransactionToken(@Param("id") Long id, @Param("token") String token);
}
