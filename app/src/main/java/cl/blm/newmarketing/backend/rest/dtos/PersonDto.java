package cl.blm.newmarketing.backend.rest.dtos;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class PersonDto {
  private Integer personId;
  private String personFullName;
  private String personIdCard;
  private String personAddress;
  private String personEmail;
  private Integer personPhone1;
  private Integer personPhone2;

  public PersonDto() {
    super();
  }

  public Integer getPersonId() {
    return personId;
  }

  public void setPersonId(int personId) {
    this.personId = personId;
  }

  public String getPersonFullName() {
    return personFullName;
  }

  public void setPersonFullName(String personFullName) {
    this.personFullName = personFullName;
  }

  public String getPersonIdCard() {
    return personIdCard;
  }

  public void setPersonIdCard(String personIdCard) {
    this.personIdCard = personIdCard;
  }

  public String getPersonAddress() {
    return personAddress;
  }

  public void setPersonAddress(String personAddress) {
    this.personAddress = personAddress;
  }

  public String getPersonEmail() {
    return personEmail;
  }

  public void setPersonEmail(String personEmail) {
    this.personEmail = personEmail;
  }

  public Integer getPersonPhone1() {
    return personPhone1;
  }

  public void setPersonPhone1(Integer personPhone1) {
    this.personPhone1 = personPhone1;
  }

  public Integer getPersonPhone2() {
    return personPhone2;
  }

  public void setPersonPhone2(Integer personPhone2) {
    this.personPhone2 = personPhone2;
  }
}
