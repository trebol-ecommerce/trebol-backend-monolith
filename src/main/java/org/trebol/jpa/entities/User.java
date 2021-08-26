package org.trebol.jpa.entities;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.trebol.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(
  name = "app_users",
  indexes = @Index(columnList = "user_name"),
  uniqueConstraints = @UniqueConstraint(columnNames = {"user_name"}))
@NamedQueries({ @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u") })
public class User
    implements GenericEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "user_id")
  private Long id;
  @Basic(optional = false)
  @Size(min = 1, max = 50)
  @Column(name = "user_name")
  private String name;
  @Basic(optional = false)
  @Size(min = 1, max = 100)
  @Column(name = "user_password")
  private String password;
  @JoinColumn(name = "person_id", referencedColumnName = "person_id", insertable = true, updatable = false)
  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  private Person person;
  @JoinColumn(name = "user_role_id", referencedColumnName = "user_role_id", insertable = true, updatable = true)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private UserRole userRole;

  public User() {
  }

  public User(Long id, String name, String password, Person person, UserRole userRole) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.person = person;
    this.userRole = userRole;
  }

  @Override
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
    int hash = 7;
    hash = 13 * hash + Objects.hashCode(this.id);
    hash = 13 * hash + Objects.hashCode(this.name);
    hash = 13 * hash + Objects.hashCode(this.password);
    hash = 13 * hash + Objects.hashCode(this.person);
    hash = 13 * hash + Objects.hashCode(this.userRole);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final User other = (User)obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.password, other.password)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.person, other.person)) {
      return false;
    }
    if (!Objects.equals(this.userRole, other.userRole)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "User{id=" + id +
        ", name=" + name +
        ", password=" + password +
        ", person=" + person +
        ", userRole=" + userRole + '}';
  }

}
