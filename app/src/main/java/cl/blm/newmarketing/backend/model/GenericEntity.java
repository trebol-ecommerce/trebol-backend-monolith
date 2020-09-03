package cl.blm.newmarketing.backend.model;

import java.io.Serializable;

public interface GenericEntity<I>
    extends Serializable {
  I getId();
}
