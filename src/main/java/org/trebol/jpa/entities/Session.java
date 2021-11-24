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
public class Session
  implements Serializable {

  private static final long serialVersionUID = 17L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "session_id", nullable = false)
  private Long id;
  @Size(min = 32, max = 500)
  @Column(name = "session_token", nullable = false)
  private String token;
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", updatable = false)
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Session session = (Session) o;
    return Objects.equals(id, session.id) &&
        Objects.equals(token, session.token) &&
        Objects.equals(user, session.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, token, user);
  }

  @Override
  public String toString() {
    return "Session{" +
        "id=" + id +
        ", token='" + token + '\'' +
        ", user=" + user +
        '}';
  }
}
