package cl.blm.trebol.store.api.pojo;

import java.util.Collection;

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
}
