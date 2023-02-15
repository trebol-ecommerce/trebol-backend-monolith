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
import org.trebol.jpa.entities.Person;

/**
 * Builds & caches reusable instances of Person and PersonPojo
 */
public class PeopleTestHelper {
  public static final long GENERIC_ID = 1L;
  public static final String PERSON_ID_NUMBER = "111111111";
  public static final String PERSON_FIRST_NAME = "test f. name";
  public static final String PERSON_LAST_NAME = "test l. name";
  public static final String PERSON_EMAIL = "test@example.com";
  public static final String PERSON_PHONE1 = "1234567";
  public static final String PERSON_PHONE2 = "9876543";
  private PersonPojo pojoForFetch;
  private PersonPojo pojoBeforeCreation;
  private PersonPojo pojoAfterCreation;
  private Person entityBeforeCreation;
  private Person entityAfterCreation;

  public void resetPeople() {
    this.pojoForFetch = null;
    this.pojoBeforeCreation = null;
    this.pojoAfterCreation = null;
    this.entityBeforeCreation = null;
    this.entityAfterCreation = null;
  }

  public PersonPojo personPojoForFetch() {
    if (this.pojoForFetch == null) {
      this.pojoForFetch = PersonPojo.builder()
        .idNumber(PERSON_ID_NUMBER)
        .build();
    }
    return this.pojoForFetch;
  }

  public PersonPojo personPojoBeforeCreation() {
    if (this.pojoBeforeCreation == null) {
      this.pojoBeforeCreation = PersonPojo.builder()
        .idNumber(PERSON_ID_NUMBER)
        .firstName(PERSON_FIRST_NAME)
        .lastName(PERSON_LAST_NAME)
        .email(PERSON_EMAIL)
        .phone1(PERSON_PHONE1)
        .phone2(PERSON_PHONE2)
        .build();
    }
    return this.pojoBeforeCreation;
  }

  public PersonPojo personPojoAfterCreation() {
    if (this.pojoAfterCreation == null) {
      this.pojoAfterCreation = PersonPojo.builder()
        .id(GENERIC_ID)
        .firstName(PERSON_FIRST_NAME)
        .lastName(PERSON_LAST_NAME)
        .idNumber(PERSON_ID_NUMBER)
        .email(PERSON_EMAIL)
        .phone1(PERSON_PHONE1)
        .phone2(PERSON_PHONE2)
        .build();
    }
    return this.pojoAfterCreation;
  }

  public Person personEntityBeforeCreation() {
    if (this.entityBeforeCreation == null) {
      this.entityBeforeCreation = Person.builder()
        .firstName(PERSON_FIRST_NAME)
        .lastName(PERSON_LAST_NAME)
        .idNumber(PERSON_ID_NUMBER)
        .email(PERSON_EMAIL)
        .build();
    }
    return this.entityBeforeCreation;
  }

  public Person personEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = Person.builder()
          .id(GENERIC_ID)
          .firstName(PERSON_FIRST_NAME)
          .lastName(PERSON_LAST_NAME)
          .idNumber(PERSON_ID_NUMBER)
          .email(PERSON_EMAIL)
          .phone1(PERSON_PHONE1)
          .phone2(PERSON_PHONE2)
          .build();
    }
    return this.entityAfterCreation;
  }
}
