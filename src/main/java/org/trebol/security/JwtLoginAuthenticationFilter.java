package org.trebol.security;

import java.io.IOException;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.trebol.config.SecurityProperties;
import org.trebol.api.pojo.LoginPojo;

public class JwtLoginAuthenticationFilter
  extends GenericJwtAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JwtLoginAuthenticationFilter(SecurityProperties jwtProperties, SecretKey secretKey,
                                      AuthenticationManager authenticationManager) {
    super(jwtProperties, secretKey);
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException {
    if (!HttpMethod.POST.matches(request.getMethod())) {
      return null;
    } else {
      try {
        LoginPojo userData = new ObjectMapper().readValue(request.getInputStream(), LoginPojo.class);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userData.getName(),
            userData.getPassword());
        return authenticationManager.authenticate(authentication);
      } catch (IOException e) {
        throw new BadCredentialsException("Request body is not a login request");
      }
    }
  }
}
