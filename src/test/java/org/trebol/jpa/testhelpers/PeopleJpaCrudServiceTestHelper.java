package org.trebol.jpa.testhelpers;

import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

public class PeopleJpaCrudServiceTestHelper {

  public static final long GENERIC_ID = 1L;
  public static final String PERSON_ID_NUMBER = "111111111";
  public static final String PERSON_FIRST_NAME = "test f. name";
  public static final String PERSON_LAST_NAME = "test l. name";
  public static final String PERSON_EMAIL = "test@example.com";
  public static final String PERSON_PHONE1 = "1234567";
  public static final String PERSON_PHONE2 = "9876543";
  private static PersonPojo pojoForFetch;
  private static PersonPojo pojoBeforeCreation;
  private static PersonPojo pojoAfterCreation;
  private static Person entityBeforeCreation;
  private static Person entityAfterCreation;

  public static void resetPeople() {
    pojoForFetch = null;
    pojoBeforeCreation = null;
    pojoAfterCreation = null;
    entityBeforeCreation = null;
    entityAfterCreation = null;
  }

  public static PersonPojo personPojoForFetch() {
    if (pojoForFetch == null) {
      pojoForFetch = new PersonPojo(PERSON_ID_NUMBER);
    }
    return pojoForFetch;
  }

  public static PersonPojo personPojoBeforeCreation() {
    if (pojoBeforeCreation == null) {
      pojoBeforeCreation = new PersonPojo(PERSON_ID_NUMBER);
    }
    return pojoBeforeCreation;
  }

  public static PersonPojo personPojoAfterCreation() {
    if (pojoAfterCreation == null) {
      pojoAfterCreation = new PersonPojo(GENERIC_ID, PERSON_FIRST_NAME, PERSON_LAST_NAME, PERSON_ID_NUMBER,
                                         PERSON_EMAIL, PERSON_PHONE1, PERSON_PHONE2);
    }
    return pojoAfterCreation;
  }

  public static Person personEntityBeforeCreation() {
    if (entityBeforeCreation == null) {
      entityBeforeCreation = new Person(PERSON_FIRST_NAME, PERSON_LAST_NAME, PERSON_ID_NUMBER, PERSON_EMAIL);
    }
    return entityBeforeCreation;
  }

  public static Person personEntityAfterCreation() {
    if (entityAfterCreation == null) {
      entityAfterCreation = new Person(GENERIC_ID, PERSON_FIRST_NAME, PERSON_LAST_NAME, PERSON_ID_NUMBER, PERSON_EMAIL,
                                       PERSON_PHONE1, PERSON_PHONE2);
    }
    return entityAfterCreation;
  }
}
