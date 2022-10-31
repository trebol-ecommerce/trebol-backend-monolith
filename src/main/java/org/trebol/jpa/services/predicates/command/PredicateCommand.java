package org.trebol.jpa.services.predicates.command;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.trebol.jpa.entities.QProductCategory;
import org.trebol.jpa.services.IProductCategoryTreeResolver;

import java.util.List;
import java.util.Map;

public class PredicateCommand {
  private static Map<String, Command> PREDICATE_MAP = Map.of(
    "id", (stringValue, treeResolver, category, booleanBuilder) -> booleanBuilder.and(category.id.eq(Long.valueOf(stringValue))),
    "code", (stringValue, treeResolver, category, booleanBuilder) -> booleanBuilder.and(category.code.eq(stringValue)),
    "name", (stringValue, treeResolver, category, booleanBuilder) -> booleanBuilder.and(category.name.eq(stringValue)),
    "nameLike", (stringValue, treeResolver, category, booleanBuilder) -> booleanBuilder.and(category.name.likeIgnoreCase("%" + stringValue + "%")),
    "parentCode", (stringValue, treeResolver, category, booleanBuilder) -> {
      if (StringUtils.isNotBlank(stringValue)) {
        return booleanBuilder.and(category.parent.code.eq(stringValue));
      }
      return booleanBuilder;
    },
    "parentId", (stringValue, treeResolver, category, booleanBuilder) -> {
      if (StringUtils.isNotBlank(stringValue)) {
        return booleanBuilder.and(category.parent.isNull());
      } else {
        return booleanBuilder.and(category.parent.id.eq(Long.valueOf(stringValue)));
      }
    },
    "rootId", (stringValue, treeResolver, category, booleanBuilder) -> {
      if (StringUtils.isNotBlank(stringValue)) {
        List<Long> branchParentIds = treeResolver.getBranchIdsFromRootId(Long.valueOf(stringValue));
        return booleanBuilder.and(category.parent.id.in(branchParentIds));
      }
      return booleanBuilder;
    },
    "rootCode", (stringValue, treeResolver, category, booleanBuilder) -> {
      if (StringUtils.isNotBlank(stringValue)) {
        List<Long> branchParentIds = treeResolver.getBranchIdsFromRootCode(stringValue);
        if (!CollectionUtils.isEmpty(branchParentIds)) {
          return booleanBuilder.and(category.parent.id.in(branchParentIds));
        }
      }
      return booleanBuilder;
    }
  );


  /*
        switch (paramName) {
          case "id":
            return getBasePath().id.eq(Long.valueOf(stringValue));
          case "code":
            return getBasePath().code.eq(stringValue);
          case "name":
            predicate.and(getBasePath().name.eq(stringValue));
            break;
          case "nameLike":
            predicate.and(getBasePath().name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "parentCode":
            if (StringUtils.isNotBlank(stringValue)) {
              predicate.and(getBasePath().parent.code.eq(stringValue));
            }
            break;
          case "parentId":
            if (StringUtils.isNotBlank(stringValue)) {
              predicate.and(getBasePath().parent.isNull());
            } else {
              predicate.and(getBasePath().parent.id.eq(Long.valueOf(stringValue)));
            }
            break;
          case "rootId":
            if (StringUtils.isNotBlank(stringValue)) {
              List<Long> branchParentIds = treeResolver.getBranchIdsFromRootId(Long.valueOf(stringValue));
              predicate.and(getBasePath().parent.id.in(branchParentIds));
            }
            break;
          case "rootCode":
            if (StringUtils.isNotBlank(stringValue)) {
              List<Long> branchParentIds = treeResolver.getBranchIdsFromRootCode(stringValue);
              if (!CollectionUtils.isEmpty(branchParentIds)) {
                predicate.and(getBasePath().parent.id.in(branchParentIds));
              }
            }
            break;
          default:
            break;
        }
*/



  public static Predicate getPredicate(String param, String stringValue, IProductCategoryTreeResolver treeResolver, QProductCategory category, BooleanBuilder booleanBuilder) {
    Command command = PREDICATE_MAP.get(param);

    if (command == null) {
      throw new IllegalArgumentException("Invalid param type: "
        + param);
    }

    Predicate predicate = command.getPredicate(stringValue, treeResolver, category, booleanBuilder);
    return predicate;
  }
}
