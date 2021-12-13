package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.jsonwebtoken.lang.Maps;
import org.junit.Test;
import org.trebol.jpa.services.predicates.ImagesPredicateJpaServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImagesPredicateJpaServiceTest {

  @Test
  public void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    ImagesPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Maps.of("id", "1").build()),
                                         service.parseMap(Maps.of("code", "code test").build()),
                                         service.parseMap(Maps.of("filename", "filename test").build()),
                                         service.parseMap(Maps.of("codeLike", "code portion").build()),
                                         service.parseMap(Maps.of("filenameLike", "filename portion").build()));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private ImagesPredicateJpaServiceImpl instantiate() {
    return new ImagesPredicateJpaServiceImpl();
  }
}
