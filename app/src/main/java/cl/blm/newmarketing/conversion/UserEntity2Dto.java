package cl.blm.newmarketing.conversion;

import cl.blm.newmarketing.rest.dtos.UserDto;
import cl.blm.newmarketing.model.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class UserEntity2Dto
        implements Converter<User, UserDto> {
  @Override
  public UserDto convert(User source) {
    UserDto target = new UserDto();
    target.setUserId(source.getId());
    target.setUserName(source.getName());
    return target;
  }
}
