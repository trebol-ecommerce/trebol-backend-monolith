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
      this.pojoForFetch = PersonPojo.builder().idNumber(PERSON_ID_NUMBER).build();
    }
    return this.pojoForFetch;
  }

  public PersonPojo personPojoBeforeCreation() {
    if (this.pojoBeforeCreation == null) {
      this.pojoBeforeCreation = PersonPojo.builder().idNumber(PERSON_ID_NUMBER).build();
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
      this.entityBeforeCreation = new Person(PERSON_FIRST_NAME, PERSON_LAST_NAME, PERSON_ID_NUMBER, PERSON_EMAIL);
    }
    return this.entityBeforeCreation;
  }

  public Person personEntityAfterCreation() {
    if (this.entityAfterCreation == null) {
      this.entityAfterCreation = new Person(GENERIC_ID, PERSON_FIRST_NAME, PERSON_LAST_NAME, PERSON_ID_NUMBER, PERSON_EMAIL,
        PERSON_PHONE1, PERSON_PHONE2);
    }
    return this.entityAfterCreation;
  }
}
