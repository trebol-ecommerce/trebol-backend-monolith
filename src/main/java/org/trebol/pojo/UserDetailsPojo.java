package org.trebol.pojo;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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

  public UserDetailsPojo(
      List<? extends GrantedAuthority> authorities,
      String username,
      String password,
      boolean accountNonExpired,
      boolean accountNonLocked,
      boolean credentialsNonExpired,
      boolean enabled
  ) {
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
  public int hashCode() {
    int hash = 5;
    hash = 73 * hash + Objects.hashCode(this.authorities);
    hash = 73 * hash + Objects.hashCode(this.username);
    hash = 73 * hash + Objects.hashCode(this.password);
    hash = 73 * hash + (this.accountNonExpired ? 1 : 0);
    hash = 73 * hash + (this.accountNonLocked ? 1 : 0);
    hash = 73 * hash + (this.credentialsNonExpired ? 1 : 0);
    hash = 73 * hash + (this.enabled ? 1 : 0);
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
    final UserDetailsPojo other = (UserDetailsPojo)obj;
    if (this.accountNonExpired != other.accountNonExpired) {
      return false;
    }
    if (this.accountNonLocked != other.accountNonLocked) {
      return false;
    }
    if (this.credentialsNonExpired != other.credentialsNonExpired) {
      return false;
    }
    if (this.enabled != other.enabled) {
      return false;
    }
    if (!Objects.equals(this.username, other.username)) {
      return false;
    }
    if (!Objects.equals(this.password, other.password)) {
      return false;
    }
    return Objects.equals(this.authorities, other.authorities);
  }

  @Override
  public String toString() {
    return "UserDetailsPojo{" + "authorities=" + authorities + ", username=" + username + ", password=" + password + ", accountNonExpired=" + accountNonExpired + ", accountNonLocked=" + accountNonLocked + ", credentialsNonExpired=" + credentialsNonExpired + ", enabled=" + enabled + '}';
  }

}
