package cl.blm.newmarketing.rest.services.impl;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;

import cl.blm.newmarketing.rest.dtos.SessionDto;
import cl.blm.newmarketing.rest.services.CrudService;

//TODO implement all methods in this class

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Transactional
@Service
public class SessionCrudServiceImpl
    implements CrudService<SessionDto, String> {
  private static final Logger LOG = LoggerFactory.getLogger(SessionCrudServiceImpl.class);

  @Override
  public Predicate queryParamsMapToPredicate(Map<String, String> queryParamsMap) {
    LOG.debug("queryParamsMapToPredicate({})", queryParamsMap);
    throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools
                                                                   // | Templates.
  }

  @Nullable
  @Override
  public SessionDto create(SessionDto dto) {
    LOG.debug("create({})", dto);
    throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools
                                                                   // | Templates.
  }

  @Override
  public Collection<SessionDto> read(int pageSize, int pageIndex, Predicate filters) {
    LOG.debug("read({}, {}, {})", pageSize, pageIndex, filters);
    throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools
                                                                   // | Templates.
  }

  @Nullable
  @Override
  public SessionDto update(SessionDto dto) {
    LOG.debug("update({})", dto);
    throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools
                                                                   // | Templates.
  }

  @Override
  public boolean delete(String id) {
    LOG.debug("delete({})", id);
    throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools
                                                                   // | Templates.
  }

  @Nullable
  @Override
  public SessionDto find(String id) {
    LOG.debug("delete({})", id);
    throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose Tools
                                                                   // | Templates.
  }

}
