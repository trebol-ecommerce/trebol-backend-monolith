package org.trebol.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.trebol.jpa.entities.Address;
import org.trebol.jpa.IJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Repository
public interface IAddressesJpaRepository
    extends IJpaRepository<Address> {

  @Query(value = "SELECT a FROM Address a WHERE a.city = :city "
      + "AND a.municipality = :municipality "
      + "AND a.firstLine = :firstLine "
      + "AND a.secondLine = :secondLine "
      + "AND a.postalCode = :postalCode "
      + "AND a.notes = :notes")
  Optional<Address> findByFields(
    @Param("city") String city,
    @Param("municipality") String municipality,
    @Param("firstLine") String firstLine,
    @Param("secondLine") String secondLine,
    @Param("postalCode") String postalCode,
    @Param("notes") String notes);

}
