package org.trebol.api.pojo;

import java.util.Collection;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class AuthorizedAccessPojo {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Collection<String> routes;
  @JsonInclude(JsonInclude.Include.NON_NULL)
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
    if (!Objects.equals(this.permissions, other.permissions)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "AuthorizedAccessPojo{" + "routes=" + routes + ", permissions=" + permissions + '}';
  }
}
