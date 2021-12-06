package org.trebol.jpa.services.predicates;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.QPerson;
import org.trebol.jpa.services.IPredicateJpaService;

import java.util.Map;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Service
public class PeoplePredicateJpaServiceImpl
  implements IPredicateJpaService<Person> {

  private final Logger logger = LoggerFactory.getLogger(PeoplePredicateJpaServiceImpl.class);

  @Override
  public QPerson getBasePath() {
    return QPerson.person;
  }

  @Override
  public Predicate parseMap(Map<String, String> queryParamsMap) {
    BooleanBuilder predicate = new BooleanBuilder();
    for (Map.Entry<String, String> entry : queryParamsMap.entrySet()) {
      String paramName = entry.getKey();
      String stringValue = entry.getValue();
      try {
        switch (paramName) {
          case "id":
            return getBasePath().id.eq(Long.valueOf(stringValue));
          case "idNumber":
            return getBasePath().idNumber.eq(stringValue);
          case "nameLike":
            predicate.and(getBasePath().firstName.likeIgnoreCase("%" + stringValue + "%")
                    .or(getBasePath().lastName.likeIgnoreCase("%" + stringValue + "%")));
            break;
          case "firstNameLike":
            predicate.and(getBasePath().firstName.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "lastNameLike":
            predicate.and(getBasePath().lastName.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "idNumberLike":
            predicate.and(getBasePath().idNumber.likeIgnoreCase("%" + stringValue + "%"));
            break;
          case "emailLike":
            predicate.and(getBasePath().email.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.info("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue);
      }
    }

    return predicate;
  }
}
