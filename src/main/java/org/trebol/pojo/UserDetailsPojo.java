package org.trebol.pojo;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetailsPojo
    implements UserDetails {
  private static final long serialVersionUID = 1L;

  private final List<? extends GrantedAuthority> authorities;
  private final String username;
  private final String password;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;

  public UserDetailsPojo(List<? extends GrantedAuthority> authorities,
                         String username,
                         String password,
                         boolean accountNonExpired,
                         boolean accountNonLocked,
                         boolean credentialsNonExpired,
                         boolean enabled) {
    this.authorities = authorities;
    this.username = username;
    this.password = password;
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
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDetailsPojo that = (UserDetailsPojo) o;
    return accountNonExpired == that.accountNonExpired &&
        accountNonLocked == that.accountNonLocked &&
        credentialsNonExpired == that.credentialsNonExpired &&
        enabled == that.enabled &&
        Objects.equals(authorities, that.authorities) &&
        Objects.equals(username, that.username) &&
        Objects.equals(password, that.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authorities, username, password, accountNonExpired, accountNonLocked, credentialsNonExpired,
        enabled);
  }

  @Override
  public String toString() {
    return "UserDetailsPojo{" +
        "authorities=" + authorities +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", accountNonExpired=" + accountNonExpired +
        ", accountNonLocked=" + accountNonLocked +
        ", credentialsNonExpired=" + credentialsNonExpired +
        ", enabled=" + enabled +
        '}';
  }
}
