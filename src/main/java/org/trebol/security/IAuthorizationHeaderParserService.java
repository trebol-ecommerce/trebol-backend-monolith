package org.trebol.security;

import javax.annotation.Nullable;

import org.springframework.http.HttpHeaders;

/**
 * Interface for parsing Authorization headers into tokens
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 *
 * @param <T> The token body type
 */
public interface IAuthorizationHeaderParserService<T> {

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
