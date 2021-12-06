package org.trebol.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Objects;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AuthorizedAccessPojo that = (AuthorizedAccessPojo) o;
    return Objects.equals(routes, that.routes) && Objects.equals(permissions, that.permissions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routes, permissions);
  }

  @Override
  public String toString() {
    return "AuthorizedAccessPojo{" +
        "routes=" + routes +
        ", permissions=" + permissions +
        '}';
  }
}
