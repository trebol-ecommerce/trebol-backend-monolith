package cl.blm.newmarketing.store.converters.topojo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import cl.blm.newmarketing.store.jpa.entities.Permission;
import cl.blm.newmarketing.store.security.pojo.GranthedAuthorityPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Component
public class Permission2GranthedAuthorityPojo
    implements Converter<Permission, GranthedAuthorityPojo> {

  @Override
  public GranthedAuthorityPojo convert(Permission source) {
    GranthedAuthorityPojo target = new GranthedAuthorityPojo(source.getCode());
    return target;
  }

}
