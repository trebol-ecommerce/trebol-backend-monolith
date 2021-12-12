package org.trebol.testhelpers;

import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SalespersonPojo;

public class SalespeopleTestHelper {

  public static final long GENERIC_ID = 1L;
  public static final String SALESPERSON_ID_NUMBER = "333333333";
  public static final String SALESPERSON_FIRST_NAME = "salesperson f. name";
  public static final String SALESPERSON_LAST_NAME = "salesperson l. name";
  public static final String SALESPERSON_EMAIL = "salesperson@example.com";
  public static final String SALESPERSON_PHONE1 = "1234567";
  public static final String SALESPERSON_PHONE2 = "9876543";
  private static SalespersonPojo pojoForFetch;
  private static SalespersonPojo pojoBeforeCreation;
  private static SalespersonPojo pojoAfterCreation;
  private static Salesperson entityBeforeCreation;
  private static Salesperson entityAfterCreation;

  public static void resetSalespeople() {
    pojoForFetch = null;
    pojoBeforeCreation = null;
    pojoAfterCreation = null;
    entityBeforeCreation = null;
    entityAfterCreation = null;
  }

  public static SalespersonPojo salespersonPojoForFetch() {
    if (pojoForFetch == null) {
      pojoForFetch = new SalespersonPojo(SALESPERSON_ID_NUMBER);
    }
    return pojoForFetch;
  }

  public static SalespersonPojo salespersonPojoBeforeCreation() {
    if (pojoBeforeCreation == null) {
      pojoBeforeCreation = new SalespersonPojo(SALESPERSON_ID_NUMBER);
    }
    return pojoBeforeCreation;
  }

  public static SalespersonPojo salespersonPojoAfterCreation() {
    if (pojoAfterCreation == null) {
      pojoAfterCreation = new SalespersonPojo(new PersonPojo(GENERIC_ID, SALESPERSON_FIRST_NAME, SALESPERSON_LAST_NAME,
                                                             SALESPERSON_ID_NUMBER, SALESPERSON_EMAIL,
                                                             SALESPERSON_PHONE1, SALESPERSON_PHONE2));
    }
    return pojoAfterCreation;
  }

  public static Salesperson salespersonEntityBeforeCreation() {
    if (entityBeforeCreation == null) {
      entityBeforeCreation = new Salesperson(new Person(SALESPERSON_FIRST_NAME, SALESPERSON_LAST_NAME,
                                                        SALESPERSON_ID_NUMBER, SALESPERSON_EMAIL));
    }
    return entityBeforeCreation;
  }

  public static Salesperson salespersonEntityAfterCreation() {
    if (entityAfterCreation == null) {
      entityAfterCreation = new Salesperson(new Person(GENERIC_ID, SALESPERSON_FIRST_NAME, SALESPERSON_LAST_NAME,
                                                       SALESPERSON_ID_NUMBER, SALESPERSON_EMAIL, SALESPERSON_PHONE1,
                                                       SALESPERSON_PHONE2));
    }
    return entityAfterCreation;
  }
}
