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

package org.trebol.api.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.api.models.PersonPojo;
import org.trebol.api.services.ProfileService;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.trebol.testing.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class AccountProfileControllerTest {
  @InjectMocks AccountProfileController instance;
  @Mock ProfileService userProfileServiceMock;

  @Test
  void fetches_profile() {
    PersonPojo expectedResult = PersonPojo.builder().build();
    when(userProfileServiceMock.getProfileFromUserName(anyString())).thenReturn(expectedResult);

    PersonPojo result = instance.getProfile(new SimplePrincipal());

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void updates_profile() {
    assertDoesNotThrow(() -> {
      PersonPojo changes = PersonPojo.builder()
        .idNumber(ANY)
        .firstName(ANY)
        .lastName(ANY)
        .email(ANY)
        .build();
      instance.updateProfile(new SimplePrincipal(), changes);
      verify(userProfileServiceMock).updateProfileForUserWithName(ANY, changes);
    });
  }

  static class SimplePrincipal
    implements Principal {
    @Override
    public String getName() {
      return ANY;
    }
  }
}
