package cl.blm.newmarketing.backend.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import cl.blm.newmarketing.backend.model.entities.Seller;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface SellersRepository
    extends JpaRepository<Seller, Integer>, QuerydslPredicateExecutor<Seller> {

}
