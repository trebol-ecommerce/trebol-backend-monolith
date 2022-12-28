package org.trebol.testhelpers;

import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.Salesperson;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.SalespersonPojo;

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
      this.pojoForFetch = SalespersonPojo.builder()
        .person(PersonPojo.builder().idNumber(SALESPERSON_ID_NUMBER).build())
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
      this.entityBeforeCreation = new Salesperson(new Person(SALESPERSON_FIRST_NAME, SALESPERSON_LAST_NAME,
                                                        SALESPERSON_ID_NUMBER, SALESPERSON_EMAIL));
    }
    return this.entityBeforeCreation;
  }

  public Salesperson salespersonEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = new Salesperson(new Person(GENERIC_ID, SALESPERSON_FIRST_NAME, SALESPERSON_LAST_NAME,
                                                       SALESPERSON_ID_NUMBER, SALESPERSON_EMAIL, SALESPERSON_PHONE1,
                                                       SALESPERSON_PHONE2));
    }
    return this.entityAfterCreation;
  }
}
