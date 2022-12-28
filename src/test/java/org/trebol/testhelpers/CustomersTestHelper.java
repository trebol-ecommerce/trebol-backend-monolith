package org.trebol.testhelpers;

import org.trebol.jpa.entities.Customer;
import org.trebol.jpa.entities.Person;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;

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
      this.pojoForFetch = CustomerPojo.builder()
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
      this.entityBeforeCreation = new Customer(new Person(CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME,
                                                     CUSTOMER_ID_NUMBER, CUSTOMER_EMAIL));
    }
    return this.entityBeforeCreation;
  }

  public Customer customerEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = new Customer(new Person(GENERIC_ID, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME,
                                                    CUSTOMER_ID_NUMBER, CUSTOMER_EMAIL, CUSTOMER_PHONE1,
                                                    CUSTOMER_PHONE2));
    }
    return this.entityAfterCreation;
  }
}
