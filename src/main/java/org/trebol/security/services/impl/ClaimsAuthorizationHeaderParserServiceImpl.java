/*
 * Copyright (c) 2023 The Trebol eCommerce Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.trebol.security.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.trebol.security.services.AuthorizationHeaderParserService;

import javax.crypto.SecretKey;

@Service
public class ClaimsAuthorizationHeaderParserServiceImpl
  implements AuthorizationHeaderParserService<Claims> {
  private final SecretKey secretKey;

  @Autowired
  public ClaimsAuthorizationHeaderParserServiceImpl(
    SecretKey secretKey
  ) {
    this.secretKey = secretKey;
  }

  @Override
  public Claims parseToken(String token) throws IllegalStateException {
    try {
      Jws<Claims> claimsJws = Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token);

      return claimsJws.getBody();
    } catch (JwtException e) {
      throw new IllegalStateException(String.format("Token %s can't be trusted", token));
    }
  }

  @Nullable
  @Override
  public String extractAuthorizationHeader(HttpHeaders httpHeaders) {
    String authHeaderKey = HttpHeaders.AUTHORIZATION;
    return httpHeaders.containsKey(authHeaderKey) ? httpHeaders.getFirst(authHeaderKey) : null;
  }
}
