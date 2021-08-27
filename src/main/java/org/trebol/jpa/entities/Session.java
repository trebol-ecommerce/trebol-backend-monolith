package org.trebol.jpa.entities;

import java.io.Serializable;
import java.util.Objects;

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
import javax.validation.constraints.Size;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "app_sessions")
@NamedQueries({ @NamedQuery(name = "Session.findAll", query = "SELECT s FROM Session s") })
public class Session
  implements Serializable {

  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "session_id")
  private Long id;
  @Basic(optional = false)
  @Size(min = 32, max = 500)
  @Column(name = "session_token")
  private String token;
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = true, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private User user;

  public Session() { }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
    hash = 97 * hash + Objects.hashCode(this.token);
    hash = 97 * hash + Objects.hashCode(this.user);
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
    final Session other = (Session)obj;
    if (!Objects.equals(this.token, other.token)) {
      return false;
    }
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.user, other.user)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Session{id=" + id +
        ", token=" + token +
        ", user=" + user + '}';
  }

}
