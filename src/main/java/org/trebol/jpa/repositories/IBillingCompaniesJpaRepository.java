package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.BillingCompany;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IBillingCompaniesJpaRepository
    extends IJpaRepository<BillingCompany> {

  Optional<BillingCompany> findByIdNumber(String idNumber);

}
