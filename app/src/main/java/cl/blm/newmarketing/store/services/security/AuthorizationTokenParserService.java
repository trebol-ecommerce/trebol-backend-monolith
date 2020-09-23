package cl.blm.newmarketing.store.services.security;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;

/**
 * Interface for parsing Authorization headers into tokens
 * 
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <T> The token body type
 */
public interface AuthorizationTokenParserService<T> {

  /**
   * Extracts the Authorization header value from an http request
   * 
   * @param request
   * @return The value for the Authorization header, or null if not found
   */
  @Nullable
  public String extractAuthorizationHeaderFromRequest(HttpServletRequest request);

  /**
   * Extracts the Authorization header value from a map of http headers
   * 
   * @param httpHeaders The http headers
   * @return The value for the Authorization header, or null if not found
   */
  @Nullable
  public String extractAuthorizationHeader(HttpHeaders httpHeaders);

  /**
   * Parse a full Authorization header into the expected token body class
   * 
   * @param authorizationHeader
   * @return
   */
  public T parseToken(String authorizationHeader);
}
