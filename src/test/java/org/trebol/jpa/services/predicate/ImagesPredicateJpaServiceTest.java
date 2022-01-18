package org.trebol.jpa.services.predicate;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;
import org.trebol.jpa.services.predicates.ImagesPredicateJpaServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImagesPredicateJpaServiceTest {

  @Test
  public void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    ImagesPredicateJpaServiceImpl service = instantiate();
    List<Predicate> predicates = List.of(emptyPredicate,
                                         service.parseMap(Map.of("id", "1")),
                                         service.parseMap(Map.of("code", "code test")),
                                         service.parseMap(Map.of("filename", "filename test")),
                                         service.parseMap(Map.of("codeLike", "code portion")),
                                         service.parseMap(Map.of("filenameLike", "filename portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }

  private ImagesPredicateJpaServiceImpl instantiate() {
    return new ImagesPredicateJpaServiceImpl();
  }
}
