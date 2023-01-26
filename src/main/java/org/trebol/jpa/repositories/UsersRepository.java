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
import org.trebol.jpa.entities.User;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface UsersRepository
  extends Repository<User> {

  Optional<User> findByName(String name);

  @Query("SELECT u FROM User u JOIN FETCH u.userRole WHERE u.name = :name")
  Optional<User> findByNameWithRole(@Param("name") String name);

  @Query("SELECT u FROM User u JOIN FETCH u.person WHERE u.name = :name")
  Optional<User> findByNameWithProfile(@Param("name") String name);

  @Query("SELECT u FROM User u JOIN FETCH u.person WHERE u.id = :id")
  Optional<User> findByIdWithProfile(@Param("id") Long id);

  @Query("SELECT u FROM User u JOIN FETCH u.person p WHERE p.idNumber = :idNumber")
  Optional<User> findByPersonIdNumber(@Param("idNumber") String idNumber);
}
