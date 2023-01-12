package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.trebol.jpa.entities.ProductCategory;
import org.trebol.jpa.services.IPredicateJpaService;
import org.trebol.jpa.services.IProductCategoryTreeResolver;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ProductCategoriesPredicateJpaServiceTest {
  @Mock IProductCategoryTreeResolver treeResolver;
  IPredicateJpaService<ProductCategory> instance;

  @BeforeEach
  void beforeEach() {
    instance = new ProductCategoriesPredicateJpaServiceImpl(treeResolver);
  }

  @Test
  void parses_map() {
    Predicate emptyPredicate = new BooleanBuilder();
    List<Predicate> predicates = List.of(emptyPredicate,
      instance.parseMap(Map.of("id", "1")),
      instance.parseMap(Map.of("code", "code test")),
      instance.parseMap(Map.of("name", "name test")),
      instance.parseMap(Map.of("nameLike", "name portion")),
      instance.parseMap(Map.of("parentCode", "parent code test")),
      instance.parseMap(Map.of("parentId", "1")));
    Set<Predicate> distinctPredicates = new HashSet<>(predicates);
    assertEquals(predicates.size(), distinctPredicates.size());
  }
}
