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
  private static final String ID = "id";
  private static final String CODE = "code";
  private static final String NAME = "name";
  private static final String NAME_LIKE = "nameLike";
  private static final String PARENT_CODE = "parentCode";
  private static final String PARENT_ID = "parentId";
  private static final String ROOT_ID = "rootId";
  private static final String ROOT_CODE = "rootCode";
  private static final Map<String, Command> PREDICATE_MAP = Map.of(
    ID, (stringValue, treeResolver, category, booleanBuilder) -> booleanBuilder.and(category.id.eq(Long.valueOf(stringValue))),
    CODE, (stringValue, treeResolver, category, booleanBuilder) -> booleanBuilder.and(category.code.eq(stringValue)),
    NAME, (stringValue, treeResolver, category, booleanBuilder) -> booleanBuilder.and(category.name.eq(stringValue)),
    NAME_LIKE, (stringValue, treeResolver, category, booleanBuilder) -> booleanBuilder.and(category.name.likeIgnoreCase("%" + stringValue + "%")),
    PARENT_CODE, (stringValue, treeResolver, category, booleanBuilder) -> {
      if (StringUtils.isNotBlank(stringValue)) {
        return booleanBuilder.and(category.parent.code.eq(stringValue));
      }
      return booleanBuilder;
    },
    PARENT_ID, (stringValue, treeResolver, category, booleanBuilder) -> {
      if (StringUtils.isNotBlank(stringValue)) {
        return booleanBuilder.and(category.parent.isNull());
      } else {
        return booleanBuilder.and(category.parent.id.eq(Long.valueOf(stringValue)));
      }
    },
    ROOT_ID, (stringValue, treeResolver, category, booleanBuilder) -> {
      if (StringUtils.isNotBlank(stringValue)) {
        List<Long> branchParentIds = treeResolver.getBranchIdsFromRootId(Long.valueOf(stringValue));
        return booleanBuilder.and(category.parent.id.in(branchParentIds));
      }
      return booleanBuilder;
    },
    ROOT_CODE, (stringValue, treeResolver, category, booleanBuilder) -> {
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
