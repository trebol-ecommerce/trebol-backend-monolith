package org.trebol.jpa.repositories;

import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.PaymentType;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IPaymentTypesJpaRepository
    extends IJpaRepository<PaymentType> {

}
