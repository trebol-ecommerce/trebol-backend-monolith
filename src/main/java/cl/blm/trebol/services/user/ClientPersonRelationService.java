package cl.blm.trebol.services.user;

import cl.blm.trebol.api.pojo.ClientPojo;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface ClientPersonRelationService {
  ClientPojo getClientFromPersonId(int personId);
}
