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

package org.trebol.operation.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.services.PredicateService;
import org.trebol.jpa.services.SortSpecService;
import org.trebol.jpa.services.crud.PeopleCrudService;
import org.trebol.operation.DataGenericControllerTest;
import org.trebol.operation.services.PaginationService;
import org.trebol.pojo.PersonPojo;

@ExtendWith(MockitoExtension.class)
class DataPeopleControllerTest
  extends DataGenericControllerTest<PersonPojo, Person> {
  @InjectMocks DataPeopleController instance;
  @Mock PaginationService paginationServiceMock;
  @Mock SortSpecService<Person> sortServiceMock;
  @Mock PeopleCrudService crudServiceMock;
  @Mock PredicateService<Person> predicateServiceMock;

  @BeforeEach
  protected void beforeEach() {
    super.instance = instance;
    super.crudServiceMock = crudServiceMock;
  }

  @Test
  void reads_people_data() {
    super.reads_data();
  }
}
