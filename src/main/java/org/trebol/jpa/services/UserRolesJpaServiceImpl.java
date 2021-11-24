package org.trebol.jpa.services;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

import org.trebol.jpa.entities.QUserRole;

import org.trebol.pojo.UserRolePojo;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.GenericJpaService;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class UserRolesJpaServiceImpl
  extends GenericJpaService<UserRolePojo, UserRole> {

  private final IUserRolesJpaRepository userRolesRepository;
  private final ConversionService conversion;

  @Autowired
  public UserRolesJpaServiceImpl(IUserRolesJpaRepository repository, ConversionService conversion) {
    super(repository, LoggerFactory.getLogger(UserRolesJpaServiceImpl.class));
    this.userRolesRepository = repository;
    this.conversion = conversion;
  }

  @Override
  public UserRolePojo convertToPojo(UserRole source) {
    return conversion.convert(source, UserRolePojo.class);
  }

  @Override
  public UserRole convertToNewEntity(UserRolePojo source) {
    return conversion.convert(source, UserRole.class);
  }

  @Override
  public UserRole applyChangesToExistingEntity(UserRolePojo source, UserRole existing) throws BadInputException {
    UserRole target = new UserRole(existing);

    String name = source.getName();
    if (name != null && !name.isBlank() && target.getName().equals(name)) {
      target.setName(name);
    }

    return target;
  }

  @Override
  public Predicate parsePredicate(Map<String, String> queryParamsMap) {
    QUserRole qUserRole = QUserRole.userRole;
    BooleanBuilder predicate = new BooleanBuilder();
    for (String paramName : queryParamsMap.keySet()) {
      String stringValue = queryParamsMap.get(paramName);
      try {
        switch (paramName) {
          case "id":
            return qUserRole.id.eq(Long.valueOf(stringValue));
          case "name":
            return qUserRole.name.eq(stringValue);
          case "nameLike":
            predicate.and(qUserRole.name.likeIgnoreCase("%" + stringValue + "%"));
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

  @Override
  public Optional<UserRole> getExisting(UserRolePojo input) throws BadInputException {
    String name = input.getName();
    if (name == null || name.isBlank()) {
      throw new BadInputException("Invalid user role name");
    } else {
      return userRolesRepository.findByName(name);
    }
  }
}
