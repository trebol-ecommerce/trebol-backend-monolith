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

import org.trebol.api.models.CustomerPojo;
import org.trebol.api.models.PersonPojo;
import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;

/**
 * Builds & caches reusable instances of Customer and CustomerPojo
 */
public class CustomersTestHelper {
  public static final long GENERIC_ID = 1L;
  public static final String CUSTOMER_ID_NUMBER = "222222222";
  public static final String CUSTOMER_FIRST_NAME = "customer f. name";
  public static final String CUSTOMER_LAST_NAME = "customer l. name";
  public static final String CUSTOMER_EMAIL = "customer@example.com";
  public static final String CUSTOMER_PHONE1 = "1234567";
  public static final String CUSTOMER_PHONE2 = "9876543";
  private CustomerPojo pojoForFetch;
  private CustomerPojo pojoBeforeCreation;
  private CustomerPojo pojoAfterCreation;
  private Customer entityBeforeCreation;
  private Customer entityAfterCreation;

  public void resetCustomers() {
    this.pojoForFetch = null;
    this.pojoBeforeCreation = null;
    this.pojoAfterCreation = null;
    this.entityBeforeCreation = null;
    this.entityAfterCreation = null;
  }

  public CustomerPojo customerPojoForFetch() {
    if (this.pojoForFetch == null) {
      this.pojoForFetch = CustomerPojo.builder()
        .person(PersonPojo.builder().idNumber(CUSTOMER_ID_NUMBER).build())
        .build();
    }
    return this.pojoForFetch;
  }

  public CustomerPojo customerPojoBeforeCreation() {
    if (this.pojoBeforeCreation == null) {
      this.pojoBeforeCreation = CustomerPojo.builder()
        .person(PersonPojo.builder().idNumber(CUSTOMER_ID_NUMBER).build())
        .build();
    }
    return this.pojoBeforeCreation;
  }

  public CustomerPojo customerPojoAfterCreation() {
    if (this.pojoAfterCreation == null) {
      this.pojoAfterCreation = CustomerPojo.builder()
        .person(PersonPojo.builder()
          .id(GENERIC_ID)
          .firstName(CUSTOMER_FIRST_NAME)
          .lastName(CUSTOMER_LAST_NAME)
          .idNumber(CUSTOMER_ID_NUMBER)
          .email(CUSTOMER_EMAIL)
          .phone1(CUSTOMER_PHONE1)
          .phone2(CUSTOMER_PHONE2)
          .build())
        .build();
    }
    return this.pojoAfterCreation;
  }

  public Customer customerEntityBeforeCreation() {
    if (this.entityBeforeCreation == null) {
      this.entityBeforeCreation = Customer.builder()
        .person(Person.builder()
          .firstName(CUSTOMER_FIRST_NAME)
          .lastName(CUSTOMER_LAST_NAME)
          .idNumber(CUSTOMER_ID_NUMBER)
          .email(CUSTOMER_EMAIL)
          .build())
        .build();
    }
    return this.entityBeforeCreation;
  }

  public Customer customerEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = Customer.builder()
        .person(Person.builder()
          .id(GENERIC_ID)
          .firstName(CUSTOMER_FIRST_NAME)
          .lastName(CUSTOMER_LAST_NAME)
          .idNumber(CUSTOMER_ID_NUMBER)
          .email(CUSTOMER_EMAIL)
          .phone1(CUSTOMER_PHONE1)
          .phone2(CUSTOMER_PHONE2)
          .build())
        .build();
    }
    return this.entityAfterCreation;
  }
}
