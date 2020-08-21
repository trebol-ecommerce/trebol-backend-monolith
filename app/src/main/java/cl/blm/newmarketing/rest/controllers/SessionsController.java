package cl.blm.newmarketing.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.blm.newmarketing.pojos.LoginPojo;
import cl.blm.newmarketing.rest.dtos.PersonDto;
import cl.blm.newmarketing.rest.dtos.SessionDto;
import cl.blm.newmarketing.rest.dtos.UserDto;
import cl.blm.newmarketing.rest.services.AuthService;
import cl.blm.newmarketing.rest.services.CrudService;

/**
 * API point of entry for anything session-related.
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@RestController
@RequestMapping("/api")
public class SessionsController {
  private final static Logger LOG = LoggerFactory.getLogger(SessionsController.class);

  final String MSG_EXPIRED = "Invalid/expired session token";

  @Autowired
  private AuthService authSvc;
  @Autowired
  private CrudService<SessionDto, String> sessionSvc;
  @Autowired
  private CrudService<UserDto, Long> userSvc;

  /**
   * Opens a new user session.
   *
   * @param credentials
   *
   * @return
   */
  @PostMapping("/session")
  public ResponseEntity<Boolean> openSession(@RequestBody LoginPojo credentials) {
    LOG.debug("openSession({})", credentials);
    if (credentials != null && credentials.credentialsAreNotEmpty()) {
      LOG.info("Authenticating...");
      Long userId = authSvc.identifyUser(credentials);
      if (userId != null) {
        UserDto user = userSvc.find(userId);
        String token = authSvc.generateToken(userId);

        SessionDto session = new SessionDto();
        session.setUser(user);
        session.setSessionHash(token);
        session = sessionSvc.create(session);
        boolean result = (session != null);
        return ResponseEntity.ok(result);
      }
    }
    LOG.warn("Invalid request data");
    return ResponseEntity.ok(false);
  }

  /**
   * Validates a given session token.
   *
   * @param token
   *
   * @return El ID de la sesion.
   */
  @GetMapping("/session")
  public ResponseEntity<Boolean> validateSessionToken(@RequestHeader("Authorization") String token) {
    LOG.debug("validateSessionToken()");
    if (token != null && !token.isEmpty()) {
      boolean validated = authSvc.validateToken(token);
      return ResponseEntity.ok(validated);
    }
    LOG.warn(MSG_EXPIRED);
    return ResponseEntity.ok(false);
  }

  /**
   * Closes any session identified by a session token.
   *
   * @param token
   *
   * @return Siempre devuelve true, por motivos de seguridad.
   */
  @DeleteMapping("/session")
  public boolean closeSessionFromToken(@RequestHeader("Authorization") String token) {
    LOG.debug("closeFromToken");
    if (token != null && !token.isEmpty()) {
      authSvc.killToken(token);
    } else {
      return false;
    }
    return true;
  }

  /**
   * Retrieves profile data for the user account associated to the provided
   * session token.
   *
   * @param token
   *
   * @return A PersonDto object with the profile data.
   */
  @GetMapping("/session/profile")
  public PersonDto getProfile(@RequestParam("hash") String token) {
    LOG.debug("getProfile");
    if (token != null && !token.isEmpty()) {
      SessionDto session = sessionSvc.find(token);
      if (session != null) {
        UserDto user = session.getUser();
        PersonDto person = userSvc.find(user.getUserId()).getPerson();
        return person;
      }
    }
    LOG.warn(MSG_EXPIRED);
    return null;
  }
}
