/*
 * Copyright (c) 2022 The Trebol eCommerce Project
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

package org.trebol.jpa.services.sortspecs;

import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.QSalesperson;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.jpa.services.GenericSortSpecJpaService;

import java.util.Map;

@Service
public class SalespeopleSortSpecJpaServiceImpl
  extends GenericSortSpecJpaService<Salesperson> {

  public SalespeopleSortSpecJpaServiceImpl() {
    super(Map.of("idNumber",  QSalesperson.salesperson.person.idNumber.asc(),
                 "firstName", QSalesperson.salesperson.person.firstName.asc(),
                 "email",     QSalesperson.salesperson.person.email.asc(),
                 "phone1",    QSalesperson.salesperson.person.phone1.asc(),
                 "phone2",    QSalesperson.salesperson.person.phone2.asc(),
                 "name",      QSalesperson.salesperson.person.lastName.asc(),
                 "lastName",  QSalesperson.salesperson.person.lastName.asc()));
  }

  @Override
  public QSalesperson getBasePath() { return QSalesperson.salesperson; }
}
