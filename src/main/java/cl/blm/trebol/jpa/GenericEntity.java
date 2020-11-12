package cl.blm.trebol.jpa;

import java.io.Serializable;

public interface GenericEntity<I>
    extends Serializable {
  I getId();
}
