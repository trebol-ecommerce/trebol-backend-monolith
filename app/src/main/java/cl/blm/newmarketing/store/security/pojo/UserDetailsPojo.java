package cl.blm.newmarketing.store.security.pojo;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsPojo
    implements UserDetails {
  private static final long serialVersionUID = 1L;

  private final List<? extends GrantedAuthority> authorities;
  private final String password;
  private final String username;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;

  public UserDetailsPojo(List<? extends GrantedAuthority> authorities, String password, String username,
      boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
    super();
    this.authorities = authorities;
    this.password = password;
    this.username = username;
    this.accountNonExpired = accountNonExpired;
    this.accountNonLocked = accountNonLocked;
    this.credentialsNonExpired = credentialsNonExpired;
    this.enabled = enabled;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

}
