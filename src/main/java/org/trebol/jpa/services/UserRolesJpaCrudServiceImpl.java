package org.trebol.jpa.services;

import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QUserRole;

import org.trebol.api.pojo.UserRolePojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class UserRolesJpaCrudServiceImpl
  extends GenericJpaCrudService<UserRolePojo, UserRole> {

  private static final Logger logger = LoggerFactory.getLogger(UserRolesJpaCrudServiceImpl.class);
  private final ConversionService conversion;

  @Autowired
  public UserRolesJpaCrudServiceImpl(IUserRolesJpaRepository sellTypes, ConversionService conversion) {
    super(sellTypes);
    this.conversion = conversion;
  }

  @Nullable
  @Override
  public UserRolePojo entity2Pojo(UserRole source) {
    return conversion.convert(source, UserRolePojo.class);
  }

  @Nullable
  @Override
  public UserRole pojo2Entity(UserRolePojo source) {
    return conversion.convert(source, UserRole.class);
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QUserRole qUserRole = QUserRole.userRole;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        Long longValue = Long.valueOf(stringValue);
        switch (paramName) {
          case "id":
            return predicate.and(qUserRole.id.eq(longValue)); // match por id es único
          case "name":
            predicate.and(qUserRole.name.likeIgnoreCase("%" + stringValue + "%"));
            break;
          default:
            break;
        }
      } catch (NumberFormatException exc) {
        logger.warn("Param '{}' couldn't be parsed as number (value: '{}')", paramName, stringValue, exc);
      }
    }

    return predicate;
  }

  @Override
  public boolean itemExists(UserRolePojo input) throws BadInputException {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
