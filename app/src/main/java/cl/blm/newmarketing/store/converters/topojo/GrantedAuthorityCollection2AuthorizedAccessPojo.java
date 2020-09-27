package cl.blm.newmarketing.store.converters.topojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;

import cl.blm.newmarketing.store.api.pojo.AuthorizedAccessPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class GrantedAuthorityCollection2AuthorizedAccessPojo
    implements Converter<Collection<? extends GrantedAuthority>, AuthorizedAccessPojo> {

  @Override
  public AuthorizedAccessPojo convert(Collection<? extends GrantedAuthority> source) {
    AuthorizedAccessPojo target = new AuthorizedAccessPojo();
    List<String> permissions = new ArrayList<>();
    for (GrantedAuthority authority : source) {
      permissions.add(authority.toString());
    }
    target.setPermissions(permissions);
    return target;
  }

}
