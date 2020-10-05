package cl.blm.trebol.store.services.exposed;

import cl.blm.trebol.store.api.pojo.CompanyDetailsPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface CompanyService {
  public CompanyDetailsPojo readDetails();
}
