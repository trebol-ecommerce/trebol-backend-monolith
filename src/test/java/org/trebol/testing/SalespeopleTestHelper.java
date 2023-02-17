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

package org.trebol.testing;

import org.trebol.api.models.PersonPojo;
import org.trebol.api.models.SalespersonPojo;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;

/**
 * Builds & caches reusable instances of Salesperson and SalespersonPojo
 */
public class SalespeopleTestHelper {
  public static final long GENERIC_ID = 1L;
  public static final String SALESPERSON_ID_NUMBER = "333333333";
  public static final String SALESPERSON_FIRST_NAME = "salesperson f. name";
  public static final String SALESPERSON_LAST_NAME = "salesperson l. name";
  public static final String SALESPERSON_EMAIL = "salesperson@example.com";
  public static final String SALESPERSON_PHONE1 = "1234567";
  public static final String SALESPERSON_PHONE2 = "9876543";
  private SalespersonPojo pojoForFetch;
  private SalespersonPojo pojoBeforeCreation;
  private SalespersonPojo pojoAfterCreation;
  private Salesperson entityBeforeCreation;
  private Salesperson entityAfterCreation;

  public void resetSalespeople() {
    this.pojoForFetch = null;
    this.pojoBeforeCreation = null;
    this.pojoAfterCreation = null;
    this.entityBeforeCreation = null;
    this.entityAfterCreation = null;
  }

  public SalespersonPojo salespersonPojoForFetch() {
    if (this.pojoForFetch == null) {
      this.pojoForFetch = SalespersonPojo.builder()
        .person(PersonPojo.builder().idNumber(SALESPERSON_ID_NUMBER).build())
        .build();
    }
    return this.pojoForFetch;
  }

  public SalespersonPojo salespersonPojoBeforeCreation() {
    if (this.pojoBeforeCreation == null) {
      this.pojoBeforeCreation = SalespersonPojo.builder()
        .person(PersonPojo.builder()
          .idNumber(SALESPERSON_ID_NUMBER)
          .build())
        .build();
    }
    return this.pojoBeforeCreation;
  }

  public SalespersonPojo salespersonPojoAfterCreation() {
    if (this.pojoAfterCreation == null) {
      this.pojoAfterCreation = SalespersonPojo.builder()
        .person(PersonPojo.builder()
          .id(GENERIC_ID)
          .firstName(SALESPERSON_FIRST_NAME)
          .lastName(SALESPERSON_LAST_NAME)
          .idNumber(SALESPERSON_ID_NUMBER)
          .email(SALESPERSON_EMAIL)
          .phone1(SALESPERSON_PHONE1)
          .phone2(SALESPERSON_PHONE2)
          .build())
        .build();
    }
    return this.pojoAfterCreation;
  }

  public Salesperson salespersonEntityBeforeCreation() {
    if (this.entityBeforeCreation == null) {
      this.entityBeforeCreation = Salesperson.builder()
        .person(Person.builder()
          .firstName(SALESPERSON_FIRST_NAME)
          .lastName(SALESPERSON_LAST_NAME)
          .idNumber(SALESPERSON_ID_NUMBER)
          .email(SALESPERSON_EMAIL)
          .build())
        .build();
    }
    return this.entityBeforeCreation;
  }

  public Salesperson salespersonEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = Salesperson.builder()
        .person(Person.builder()
          .id(GENERIC_ID)
          .firstName(SALESPERSON_FIRST_NAME)
          .lastName(SALESPERSON_LAST_NAME)
          .idNumber(SALESPERSON_ID_NUMBER)
          .email(SALESPERSON_EMAIL)
          .phone1(SALESPERSON_PHONE1)
          .phone2(SALESPERSON_PHONE2)
          .build())
        .build();
    }
    return this.entityAfterCreation;
  }
}
