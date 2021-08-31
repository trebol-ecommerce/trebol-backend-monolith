package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.SellStatus;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface ISellStatusesJpaRepository
  extends IJpaRepository<SellStatus> {

  public Optional<SellStatus> findByName(String name);
}
