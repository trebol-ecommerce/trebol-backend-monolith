package cl.blm.trebol.services.user;

import javax.annotation.Nullable;

import cl.blm.trebol.api.pojo.ClientPojo;

/**
 * Service interface to link Client entities to Person
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface ClientPersonRelationService {
  @Nullable
  ClientPojo getClientFromPersonId(int personId);
}
