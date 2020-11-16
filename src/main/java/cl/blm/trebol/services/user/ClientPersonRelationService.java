package cl.blm.trebol.services.user;

import cl.blm.trebol.jpa.entities.Client;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface ClientPersonRelationService {
  Client getClientFromPersonId(int personId);
}
