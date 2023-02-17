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

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.trebol.jpa.Repository;
import org.trebol.jpa.entities.Sell;
import org.trebol.jpa.entities.SellStatus;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface SalesRepository
  extends Repository<Sell> {

  Optional<Sell> findByTransactionToken(String token);

  @Query(value = "SELECT s FROM Sell s "
    + "JOIN FETCH s.details "
    + "WHERE s.id = :id")
  Optional<Sell> findByIdWithDetails(@Param("id") Long id);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Sell s "
    + "SET s.status = :status "
    + "WHERE s.id = :id")
  int setStatus(@Param("id") Long id, @Param("status") SellStatus status);

  @Modifying
  @Query("UPDATE Sell s "
    + "SET s.transactionToken = :token "
    + "WHERE s.id = :id")
  int setTransactionToken(@Param("id") Long id, @Param("token") String token);
}
