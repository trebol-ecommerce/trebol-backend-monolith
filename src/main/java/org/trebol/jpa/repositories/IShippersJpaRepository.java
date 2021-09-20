package org.trebol.jpa.repositories;

import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.Shipper;
import org.trebol.jpa.IJpaRepository;

import java.util.Optional;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IShippersJpaRepository
  extends IJpaRepository<Shipper> {

  Optional<Shipper> findByName(String name);
}
