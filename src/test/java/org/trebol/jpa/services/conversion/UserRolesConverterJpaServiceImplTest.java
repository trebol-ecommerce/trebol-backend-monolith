package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.UserRole;
import org.trebol.pojo.UserRolePojo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
class UserRolesConverterJpaServiceImplTest {
    @InjectMocks UserRolesConverterJpaServiceImpl sut;
    UserRole userRole;
    UserRolePojo userRolePojo ;

    @BeforeEach
    void beforeEach() {
        userRole = new UserRole();
        userRole.setName(ANY);
        userRole.setId(1L);

        userRolePojo = UserRolePojo.builder()
          .id(1L)
          .name(ANY)
          .build();
    }

    @AfterEach
    void afterEach() {
        userRole = null;
        userRolePojo = null;
    }

    @Test
    void testConvertToPojo() {
        UserRolePojo actual = sut.convertToPojo(userRole);
        assertEquals(userRole.getName(), actual.getName());
    }

    @Test
    void testConvertToNewEntity() {
        UserRole actual = sut.convertToNewEntity(userRolePojo);
        assertEquals(userRolePojo.getName(), actual.getName());
    }
}
