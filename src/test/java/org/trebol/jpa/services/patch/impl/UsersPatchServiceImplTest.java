/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.jpa.services.patch.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.UserPojo;
import org.trebol.common.exceptions.BadInputException;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.UserRolesRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;
import static org.trebol.testing.TestConstants.NOT_ANY;

@ExtendWith(MockitoExtension.class)
class UsersPatchServiceImplTest {
  @InjectMocks UsersPatchServiceImpl instance;
  @Mock UserRolesRepository rolesRepositoryMock;
  private static ObjectMapper MAPPER;
  private static User EXISTING_USER;

  @BeforeAll
  static void beforeAll() {
    MAPPER = new ObjectMapper()
      .setSerializationInclusion(NON_NULL);
    EXISTING_USER = User.builder()
      .id(1L)
      .name(ANY)
      .userRole(UserRole.builder()
        .id(1L)
        .name(ANY)
        .build())
      .password(null)
      .person(null)
      .build();
  }

  @Test
  void performs_empty_patch() throws BadInputException {
    Map<String, Object> input = this.mapFrom(UserPojo.builder().build());
    User result = instance.patchExistingEntity(input, EXISTING_USER);
    assertEquals(EXISTING_USER, result);
  }

  @Test
  void patches_name() throws BadInputException {
    Map<String, Object> input = this.mapFrom(UserPojo.builder()
      .name(NOT_ANY)
      .build());
    User result = instance.patchExistingEntity(input, EXISTING_USER);
    assertNotEquals(EXISTING_USER, result);
    assertEquals(NOT_ANY, result.getName());
  }

  @Test
  void patches_role() throws BadInputException {
    UserRole existingRole = UserRole.builder().build();
    Map<String, Object> input = this.mapFrom(UserPojo.builder()
      .role(NOT_ANY)
      .build());
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingRole));
    User result = instance.patchExistingEntity(input, EXISTING_USER);
    assertNotEquals(EXISTING_USER, result);
    assertEquals(existingRole, result.getUserRole());
  }

  @Test
  void patches_all_fields() throws BadInputException {
    UserRole existingRole = UserRole.builder().build();
    Map<String, Object> input = this.mapFrom(UserPojo.builder()
      .name(NOT_ANY)
      .role(NOT_ANY)
      .build());
    when(rolesRepositoryMock.findByName(anyString())).thenReturn(Optional.of(existingRole));
    User result = instance.patchExistingEntity(input, EXISTING_USER);
    assertNotEquals(EXISTING_USER, result);
    assertEquals(NOT_ANY, result.getName());
    assertEquals(existingRole, result.getUserRole());
  }

  @Test
  void does_not_support_old_method_signature() {
    UserPojo input = UserPojo.builder().build();
    assertThrows(UnsupportedOperationException.class,
      () -> instance.patchExistingEntity(input, EXISTING_USER));
  }

  private Map<String, Object> mapFrom(UserPojo data) {
    return MAPPER.convertValue(data, new TypeReference<HashMap<String, Object>>() {});
  }
}
