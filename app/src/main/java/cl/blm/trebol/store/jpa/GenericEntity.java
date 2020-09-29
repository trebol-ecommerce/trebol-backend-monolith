package cl.blm.trebol.store.jpa;

import java.io.Serializable;

public interface GenericEntity<I>
    extends Serializable {
  I getId();
}
