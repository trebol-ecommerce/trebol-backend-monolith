package org.trebol.jpa.entities;

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

import org.trebol.jpa.GenericEntity;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Entity
@Table(name = "app_sessions")
@NamedQueries({ @NamedQuery(name = "Session.findAll", query = "SELECT s FROM Session s") })
public class Session
    implements GenericEntity<Long> {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "session_id")
  private Long id;
  @Basic(optional = false)
  @NotNull
  @Size(min = 156, max = 156)
  @Column(name = "session_token")
  private String token;
  @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = true, updatable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  public Session() {
  }

  public Session(Long sessionId) {
    this.id = sessionId;
  }

  public Session(Long sessionId, String sessionToken) {
    this.id = sessionId;
    this.token = sessionToken;
  }

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
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Session)) {
      return false;
    }
    Session other = (Session) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "org.trebol.jpa.entities.Session[ id=" + id + " ]";
  }

}
