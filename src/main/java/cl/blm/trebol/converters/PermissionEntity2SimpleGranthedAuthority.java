package cl.blm.trebol.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import cl.blm.trebol.jpa.entities.Permission;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class PermissionEntity2SimpleGranthedAuthority
    implements Converter<Permission, SimpleGrantedAuthority> {

  @Override
  public SimpleGrantedAuthority convert(Permission source) {
    SimpleGrantedAuthority target = new SimpleGrantedAuthority(source.getCode());
    return target;
  }

}
