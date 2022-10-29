package org.trebol.jpa.services.predicates.command;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.trebol.jpa.entities.QProductCategory;
import org.trebol.jpa.services.IProductCategoryTreeResolver;

public interface Command {
  Predicate getPredicate(String stringValue, IProductCategoryTreeResolver treeResolver, QProductCategory category, BooleanBuilder booleanBuilder);
}
