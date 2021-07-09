package org.trebol.converters.toentity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import org.trebol.api.pojo.UserPojo;
import org.trebol.jpa.entities.User;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class User2Entity
    implements Converter<UserPojo, User> {

  @Override
  public User convert(UserPojo source) {
    User target = new User();
    target.setName(source.getName());

    if (source.getId() != null) {
      target.setId(source.getId());
    }

    if (!(source.getPassword()== null || source.getPassword().isEmpty())) {
      target.setPassword(source.getPassword());
    }

    return target;
  }
}
