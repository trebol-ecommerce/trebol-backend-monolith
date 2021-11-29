package org.trebol.jpa.services.conversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.UserRolePojo;

import javax.annotation.Nullable;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class UserRolesConverterJpaServiceImpl
  implements ITwoWayConverterJpaService<UserRolePojo, UserRole> {

  private final ConversionService conversion;

  @Autowired
  public UserRolesConverterJpaServiceImpl(ConversionService conversion) {
    this.conversion = conversion;
  }

  @Override
  @Nullable
  public UserRolePojo convertToPojo(UserRole source) {
    return conversion.convert(source, UserRolePojo.class);
  }

  @Override
  public UserRole convertToNewEntity(UserRolePojo source) {
    return conversion.convert(source, UserRole.class);
  }

  @Override
  public UserRole applyChangesToExistingEntity(UserRolePojo source, UserRole existing) throws BadInputException {
    UserRole target = new UserRole(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }
}
