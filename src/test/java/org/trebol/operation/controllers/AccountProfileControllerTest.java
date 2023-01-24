package org.trebol.operation.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.exceptions.BadInputException;
import org.trebol.operation.services.ProfileService;
import org.trebol.pojo.PersonPojo;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class AccountProfileControllerTest {
  @InjectMocks AccountProfileController instance;
  @Mock ProfileService userProfileService;

  @Test
  void fetches_profile() {
    PersonPojo expectedResult = PersonPojo.builder().build();
    when(userProfileService.getProfileFromUserName(anyString())).thenReturn(expectedResult);

    PersonPojo result = instance.getProfile(new SimplePrincipal());

    assertNotNull(result);
    assertEquals(expectedResult, result);
  }

  @Test
  void updates_profile() throws BadInputException {
    PersonPojo changes = PersonPojo.builder()
      .idNumber(ANY)
      .firstName(ANY)
      .lastName(ANY)
      .email(ANY)
      .build();
    instance.updateProfile(new SimplePrincipal(), changes);
  }

  @Test
  void may_fail_to_update_profile() throws BadInputException {
    PersonPojo changes = PersonPojo.builder()
      .idNumber(ANY)
      .firstName(ANY)
      .lastName(ANY)
      .email(ANY)
      .build();
    doThrow(BadInputException.class).when(userProfileService).updateProfileForUserWithName(anyString(), any(PersonPojo.class));
    assertThrows(BadInputException.class, () -> instance.updateProfile(new SimplePrincipal(), changes));
  }

  static class SimplePrincipal
    implements Principal {
    @Override
    public String getName() {
      return ANY;
    }
  }
}
