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
  private static CustomerPojo pojoForFetch;
  private static CustomerPojo pojoBeforeCreation;
  private static CustomerPojo pojoAfterCreation;
  private static Customer entityBeforeCreation;
  private static Customer entityAfterCreation;

  public static void resetCustomers() {
    pojoForFetch = null;
    pojoBeforeCreation = null;
    pojoAfterCreation = null;
    entityBeforeCreation = null;
    entityAfterCreation = null;
  }

  public static CustomerPojo customerPojoForFetch() {
    if (pojoForFetch == null) {
      pojoForFetch = new CustomerPojo(CUSTOMER_ID_NUMBER);
    }
    return pojoForFetch;
  }

  public static CustomerPojo customerPojoBeforeCreation() {
    if (pojoBeforeCreation == null) {
      pojoBeforeCreation = new CustomerPojo(CUSTOMER_ID_NUMBER);
    }
    return pojoBeforeCreation;
  }

  public static CustomerPojo customerPojoAfterCreation() {
    if (pojoAfterCreation == null) {
      pojoAfterCreation = new CustomerPojo(new PersonPojo(GENERIC_ID, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME,
        CUSTOMER_ID_NUMBER, CUSTOMER_EMAIL, CUSTOMER_PHONE1,
        CUSTOMER_PHONE2));
    }
    return pojoAfterCreation;
  }

  public static Customer customerEntityBeforeCreation() {
    if (entityBeforeCreation == null) {
      entityBeforeCreation = new Customer(new Person(CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME,
        CUSTOMER_ID_NUMBER, CUSTOMER_EMAIL));
    }
    return entityBeforeCreation;
  }

  public static Customer customerEntityAfterCreation() {
    if (entityAfterCreation == null) {
      entityAfterCreation = new Customer(new Person(GENERIC_ID, CUSTOMER_FIRST_NAME, CUSTOMER_LAST_NAME,
        CUSTOMER_ID_NUMBER, CUSTOMER_EMAIL, CUSTOMER_PHONE1,
        CUSTOMER_PHONE2));
    }
    return entityAfterCreation;
  }
}
