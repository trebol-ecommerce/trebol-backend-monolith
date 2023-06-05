package org.trebol.security.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.trebol.security.UserDetailsPojo;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AuthorizedApiServiceImplTest {
  AuthorizedApiServiceImpl instance;
  List<GrantedAuthority> dummyAuthorities;

  @BeforeEach
  void beforeEach() {
    instance = new AuthorizedApiServiceImpl();
    dummyAuthorities = List.of(
      new SimpleGrantedAuthority("users:create"),
      new SimpleGrantedAuthority("users:read"),
      new SimpleGrantedAuthority("users:update"),
      new SimpleGrantedAuthority("users:delete"),
      new SimpleGrantedAuthority("customers:delete"),
      new SimpleGrantedAuthority("sales:read")
    );
  }

  @Test
  void fetches_authorized_api_routes() {
    UserDetailsPojo build = UserDetailsPojo.builder()
      .authorities(dummyAuthorities)
      .build();
    Collection<String> result = instance.getAuthorizedApiRoutes(build);
    assertFalse(result.isEmpty());
    assertEquals(3, result.size());
    assertEquals(Set.of("customers", "sales", "users"), result);
  }

  @Test
  void fetches_context_specific_authorized_api_operations() {
    UserDetailsPojo build = UserDetailsPojo.builder()
      .authorities(dummyAuthorities)
      .build();
    Map.of(
      "customers", Set.of("delete"),
      "sales", Set.of("read"),
      "users", Set.of("create", "read", "update", "delete")
    ).forEach((context, operations) -> {
      Collection<String> result = instance.getAuthorizedApiRouteAccess(build, context);
      assertEquals(operations.size(), result.size());
      assertEquals(operations, result);
    });
  }
}
