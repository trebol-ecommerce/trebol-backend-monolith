package org.trebol.pojo;

import java.util.Collection;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
@JsonInclude(NON_NULL)
public class AuthorizedAccessPojo {
  private Collection<String> routes;
  private Collection<String> permissions;

  public Collection<String> getRoutes() {
    return routes;
  }

  public void setRoutes(Collection<String> routes) {
    this.routes = routes;
  }

  public Collection<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(Collection<String> permissions) {
    this.permissions = permissions;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 67 * hash + Objects.hashCode(this.routes);
    hash = 67 * hash + Objects.hashCode(this.permissions);
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
    final AuthorizedAccessPojo other = (AuthorizedAccessPojo)obj;
    if (!Objects.equals(this.routes, other.routes)) {
      return false;
    }
    return Objects.equals(this.permissions, other.permissions);
  }

  @Override
  public String toString() {
    return "AuthorizedAccessPojo{routes=" + routes +
        ", permissions=" + permissions + '}';
  }
}
