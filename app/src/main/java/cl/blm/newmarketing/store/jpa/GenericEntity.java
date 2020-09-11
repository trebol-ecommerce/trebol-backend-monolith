package cl.blm.newmarketing.store.jpa;

import java.io.Serializable;

public interface GenericEntity<I>
    extends Serializable {
  I getId();
}
