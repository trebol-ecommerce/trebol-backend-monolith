package cl.blm.trebol.services.user;

import javax.annotation.Nullable;

import cl.blm.trebol.api.pojo.CustomerPojo;

/**
 * Service interface to link Client entities to Person
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public interface CustomerPersonRelationService {
  @Nullable
  CustomerPojo getClientFromPersonId(int personId);
}
