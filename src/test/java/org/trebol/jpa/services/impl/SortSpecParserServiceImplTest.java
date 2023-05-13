package org.trebol.jpa.services.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.trebol.jpa.services.impl.SortSpecParserServiceImpl.SORT_DIRECTION_QUERY_MAP_KEY;
import static org.trebol.jpa.services.impl.SortSpecParserServiceImpl.SORT_PROPERTY_QUERY_MAP_KEY;
import static org.trebol.testing.TestConstants.ANY;

class SortSpecParserServiceImplTest {
  SortSpecParserServiceImpl instance;

  @BeforeEach
  void setUp() {
    instance = new SortSpecParserServiceImpl();
  }

  @Test
  void parses_empty_instructions_as_unsorted() {
    Map<String, OrderSpecifier<?>> orderSpecifierMap = Map.of();
    Map<String, String> queryMap = Map.of();

    Sort result = instance.parse(orderSpecifierMap, queryMap);

    assertEquals(Sort.unsorted(), result);
  }

  @Test
  void parses_ascending_order() {
    OrderSpecifier<String> orderSpecifier = new OrderSpecifier<>(Order.ASC, new GenericExpression());
    Map<String, OrderSpecifier<?>> orderSpecifierMap = Map.of(ANY, orderSpecifier);
    Map<String, String> queryMap = Map.of(
      SORT_PROPERTY_QUERY_MAP_KEY, ANY,
      SORT_DIRECTION_QUERY_MAP_KEY, "asc"
    );

    Sort result = instance.parse(orderSpecifierMap, queryMap);

    Optional<Sort.Order> sortOrder = result.get().findFirst();
    assertTrue(sortOrder.isPresent());
    assertEquals(Sort.Direction.ASC, sortOrder.get().getDirection());
    assertEquals(QSort.by(orderSpecifier), result);
  }

  @Test
  void parses_descending_order() {
    OrderSpecifier<String> orderSpecifier = new OrderSpecifier<>(Order.DESC, new GenericExpression());
    Map<String, OrderSpecifier<?>> orderSpecifierMap = Map.of(ANY, orderSpecifier);
    Map<String, String> queryMap = Map.of(
      SORT_PROPERTY_QUERY_MAP_KEY, ANY,
      SORT_DIRECTION_QUERY_MAP_KEY, "desc"
    );

    Sort result = instance.parse(orderSpecifierMap, queryMap);

    Optional<Sort.Order> sortOrder = result.get().findFirst();
    assertTrue(sortOrder.isPresent());
    assertEquals(Sort.Direction.DESC, sortOrder.get().getDirection());
    assertEquals(QSort.by(orderSpecifier), result);
  }

  @Test
  void parses_any_other_order() {
    OrderSpecifier<String> orderSpecifier = new OrderSpecifier<>(Order.ASC, new GenericExpression());
    Map<String, OrderSpecifier<?>> orderSpecifierMap = Map.of(ANY, orderSpecifier);
    Map<String, String> queryMap = Map.of(
      SORT_PROPERTY_QUERY_MAP_KEY, ANY,
      SORT_DIRECTION_QUERY_MAP_KEY, ANY
    );

    Sort result = instance.parse(orderSpecifierMap, queryMap);

    Optional<Sort.Order> sortOrder = result.get().findFirst();
    assertTrue(sortOrder.isPresent());
    assertEquals(Sort.DEFAULT_DIRECTION, sortOrder.get().getDirection());
    assertEquals(QSort.by(orderSpecifier), result);
  }

  static class GenericExpression implements Expression<String> {
    @Override
    public <R, C> @Nullable R accept(Visitor<R, C> visitor, @Nullable C c) {
      return null;
    }

    @Override
    public Class<String> getType() {
      return String.class;
    }
  }
}
