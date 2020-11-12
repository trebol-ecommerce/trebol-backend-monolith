package cl.blm.trebol.jpa.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cl.blm.trebol.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "app_users")
@NamedQueries({ @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u") })
public class User
    implements GenericEntity<Integer> {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "user_id")
  private Integer id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 50)
  @Column(name = "user_name")
  private String name;
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "user_password")
  private String password;
  @JoinColumn(name = "person_id", referencedColumnName = "person_id", insertable = true, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Person person;
  @JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id", insertable = true, updatable = true)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private UserRole userRole;

  public User() {
  }

  public User(Integer userId) {
    this.id = userId;
  }

  public User(Integer userId, String userName, String userPassword) {
    this.id = userId;
    this.name = userName;
    this.password = userPassword;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public UserRole getUserRole() {
    return userRole;
  }

  public void setUserRole(UserRole userRole) {
    this.userRole = userRole;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof User)) {
      return false;
    }
    User other = (User) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "cl.blm.newmarketing.store.model.entities.User[ userId=" + id + " ]";
  }

}
