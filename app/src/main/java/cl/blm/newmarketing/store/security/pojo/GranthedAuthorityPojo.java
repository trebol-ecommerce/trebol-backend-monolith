package cl.blm.newmarketing.store.security.pojo;

import org.springframework.security.core.GrantedAuthority;

public class GranthedAuthorityPojo
    implements GrantedAuthority {

  private static final long serialVersionUID = 1L;

  private final String authority;

  public GranthedAuthorityPojo(String authority) {
    super();
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return authority;
  }

}
