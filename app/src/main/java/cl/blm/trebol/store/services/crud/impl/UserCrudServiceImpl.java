package cl.blm.trebol.store.services.crud.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import cl.blm.trebol.store.api.pojo.UserPojo;
import cl.blm.newmarketing.store.jpa.entities.QUser;
import cl.blm.trebol.store.jpa.entities.User;
import cl.blm.trebol.store.jpa.repositories.UsersRepository;
import cl.blm.trebol.store.services.crud.GenericEntityCrudService;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class UserCrudServiceImpl
    extends GenericEntityCrudService<UserPojo, User, Integer> {
  private static final Logger LOG = LoggerFactory.getLogger(UserCrudServiceImpl.class);

//  private final UsersRepository repository;
  private final ConversionService conversion;

  @Autowired
  public UserCrudServiceImpl(UsersRepository repository, ConversionService conversion) {
    super(repository);
    this.repository = repository;
    this.conversion = conversion;
  }

  // TODO implement a more appropiate solution
  @Override
  public UserPojo entity2Pojo(User source) {
    UserPojo target = conversion.convert(source, UserPojo.class);
//    PersonPojo person = conversion.convert(source.getPerson(), PersonPojo.class);
//    target.setPerson(person);
    return target;
  }

  // TODO implement a more appropiate solution
  @Override
  public User pojo2Entity(UserPojo source) {
    User target = conversion.convert(source, User.class);
//    Person personTarget = conversion.convert(source.getPerson(), Person.class); 
//    target.setPerson(personTarget);
    return target;
  }

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    QUser qUser = QUser.user;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Integer intValue;
        switch (paramName) {
        case "id":
          intValue = Integer.valueOf(stringValue);
          return predicate.and(qUser.id.eq(intValue)); // id matching is final
        case "name":
          predicate.and(qUser.name.likeIgnoreCase("%" + stringValue + "%"));
          break;
        default:
          break;
        }
      } catch (NumberFormatException exc) {
        LOG.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }
}
