package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.trebol.jpa.entities.UserRole;
import org.trebol.pojo.UserRolePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRolesConverterJpaServiceImplTest {
    @InjectMocks UserRolesConverterJpaServiceImpl sut;
    @Mock ConversionService conversionService;

    private UserRole userRole;
    private UserRolePojo userRolePojo ;

    @BeforeEach
    void beforeEach() {
        userRole = new UserRole();
        userRole.setName("ANY");
        userRole.setId(1L);

        userRolePojo = UserRolePojo.builder()
          .id(1L)
          .name("ANY")
          .build();
    }

    @AfterEach
    void afterEach() {
        userRole = null;
        userRolePojo = null;
    }

    @Test
    void testConvertToPojo() {
        when(conversionService.convert(any(UserRole.class), eq(UserRolePojo.class))).thenReturn(userRolePojo);
        UserRolePojo actual = sut.convertToPojo(userRole);
        assertEquals(1L, actual.getId());
        verify(conversionService, times(1)).convert(any(UserRole.class), eq(UserRolePojo.class));
    }

    @Test
    void testConvertToNewEntity() {
        when(conversionService.convert(any(UserRolePojo.class), eq(UserRole.class))).thenReturn(userRole);
        UserRole actual = sut.convertToNewEntity(userRolePojo);
        assertEquals(1L, actual.getId());
        verify(conversionService, times(1)).convert(any(UserRolePojo.class), eq(UserRole.class));
    }
}
