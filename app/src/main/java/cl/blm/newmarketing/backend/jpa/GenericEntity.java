package cl.blm.newmarketing.backend.jpa;

import java.io.Serializable;

public interface GenericEntity<I>
    extends Serializable {
  I getId();
}
