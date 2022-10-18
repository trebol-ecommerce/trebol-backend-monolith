package org.trebol.testhelpers;

import org.trebol.jpa.entities.Person;
import org.trebol.pojo.PersonPojo;

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
      pojoForFetch = PersonPojo.builder().idNumber(PERSON_ID_NUMBER).build();
    }
    return pojoForFetch;
  }

  public static PersonPojo personPojoBeforeCreation() {
    if (pojoBeforeCreation == null) {
      pojoBeforeCreation = PersonPojo.builder().idNumber(PERSON_ID_NUMBER).build();
    }
    return pojoBeforeCreation;
  }

  public static PersonPojo personPojoAfterCreation() {
    if (pojoAfterCreation == null) {
      pojoAfterCreation = PersonPojo.builder()
        .id(GENERIC_ID)
        .firstName(PERSON_FIRST_NAME)
        .lastName(PERSON_LAST_NAME)
        .idNumber(PERSON_ID_NUMBER)
        .email(PERSON_EMAIL)
        .phone1(PERSON_PHONE1)
        .phone2(PERSON_PHONE2)
        .build();
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
