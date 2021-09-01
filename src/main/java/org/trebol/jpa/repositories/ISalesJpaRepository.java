package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;

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

  @Query(value = "SELECT s FROM Sell s "
      + "JOIN s.customer "
      + "JOIN s.paymentType "
      + "JOIN s.status "
      + "JOIN s.billingType "
      + "JOIN s.billingCompany "
      + "JOIN s.billingAddress "
      + "JOIN s.shippingAddress "
      + "JOIN s.shipper "
      + "JOIN s.salesperson "
      + "JOIN s.details "
      + "WHERE s.id = :id")
  Optional<Sell> deepFindById(@Param("id") Long id);

  @Query(value = "SELECT s FROM Sell s "
      + "JOIN FETCH s.details "
      + "WHERE s.id = :id")
  Optional<Sell> findByIdWithDetails(@Param("id") Long id);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Sell s "
      + "SET s.status = :status "
      + "WHERE s.id = :id")
  public Integer setStatus(@Param("id") Long id, @Param("status") SellStatus status);

  @Modifying
  @Query("UPDATE Sell s "
      + "SET s.transactionToken = :token "
      + "WHERE s.id = :id")
  public Integer setTransactionToken(@Param("id") Long id, @Param("token") String token);
}
