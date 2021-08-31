package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.BillingType;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IBillingTypesJpaRepository
    extends IJpaRepository<BillingType> {

  Optional<BillingType> findByName(String name);
}
