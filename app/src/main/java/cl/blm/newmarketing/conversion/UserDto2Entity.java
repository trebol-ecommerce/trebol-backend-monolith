package cl.blm.newmarketing.conversion;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.rest.dtos.UserDto;
import cl.blm.newmarketing.model.entities.User;
import cl.blm.newmarketing.rest.services.UtilityService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class UserDto2Entity
    implements Converter<UserDto, User> {

  @Autowired private UtilityService util;
  
  @Override
  public User convert(UserDto source) {
    User target = new User();

    if (source.getUserId() != null) {
      target.setId(source.getUserId());
    }

    if (source.getUserName() == null) {
      target.setName(source.getUserName());
    }

    if (source.getUserRegistrationDate() == null) {
      Date now = Calendar.getInstance().getTime();
      target.setRegistrationDate(now);
    } else {
      Date regDate = util.formatString(source.getUserRegistrationDate());
      target.setRegistrationDate(regDate);
    }

    return target;
  }
}
