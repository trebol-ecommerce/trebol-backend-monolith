package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.trebol.jpa.entities.Image;
import org.trebol.jpa.services.IPredicateService;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ImagesPredicateServiceTest {
  private IPredicateService<Image> instance;

  @BeforeEach
  void beforeEach() {
    instance = new ImagesPredicateServiceImpl();
  }

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    List<Predicate> predicates = List.of(emptyPredicate,
      instance.parseMap(Map.of("id", "1")),
      instance.parseMap(Map.of("code", "code test")),
      instance.parseMap(Map.of("filename", "filename test")),
      instance.parseMap(Map.of("codeLike", "code portion")),
      instance.parseMap(Map.of("filenameLike", "filename portion")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }
}
