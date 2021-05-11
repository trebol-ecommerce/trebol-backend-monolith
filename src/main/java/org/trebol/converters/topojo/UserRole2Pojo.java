package org.trebol.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.trebol.api.pojo.UserRolePojo;

import org.trebol.jpa.entities.UserRole;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class UserRole2Pojo
    implements Converter<UserRole, UserRolePojo> {

  @Override
  public UserRolePojo convert(UserRole source) {
    UserRolePojo target = new UserRolePojo();
    target.setId(source.getId());
    target.setName(source.getName());
    return target;
  }
}
