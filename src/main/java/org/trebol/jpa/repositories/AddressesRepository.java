/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trebol.jpa.Repository;
import org.trebol.jpa.entities.Address;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface AddressesRepository
  extends Repository<Address> {

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
