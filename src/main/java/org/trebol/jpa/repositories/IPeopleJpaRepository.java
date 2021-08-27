package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.Person;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IPeopleJpaRepository
  extends IJpaRepository<Person> {

  Optional<Person> findByIdNumber(String idNumber);

}
