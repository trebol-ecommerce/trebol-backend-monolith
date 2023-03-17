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

package org.trebol.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.trebol.security.services.AuthorizationHeaderParserService;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.jsonwebtoken.Claims.EXPIRATION;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.trebol.config.Constants.AUTHORITY_CHECKOUT;
import static org.trebol.config.Constants.JWT_CLAIM_AUTHORITIES;
import static org.trebol.config.Constants.JWT_PREFIX;

/**
 * Spring Security integration tests for the JwtMockGuestAuthenticationFilter<br/>
 * For more insight on the how's and why's, check out these links.<br/>
 * <ul>
 * <li><a href="https://docs.spring.io/spring-security/site/docs/5.3.x/reference/html5/#test">Spring Security Reference - 19.Testing</a></li>
 * <li><a href="https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework">Spring Framework Reference - 7.MockMvc</a></li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
  SecurityTestingConfig.class,
  JwtVerifiterFilterTest.MockSecurityConfig.class })
@WebAppConfiguration
class JwtVerifiterFilterTest {
  static final String ENDPOINT_URL = "/";
  static final String POSITIVE_RESPONSE_BODY = "IT WORKS";
  @MockBean AuthorizationHeaderParserService<Claims> claimsParserServiceMock;
  @Autowired SecretKey secretkey;
  @Autowired WebApplicationContext webApplicationContext;
  MockMvc mockMvc;

  @BeforeEach
  void beforeEach() {
    SecurityContextHolder.clearContext();
    mockMvc = MockMvcBuilders
      .webAppContextSetup(webApplicationContext)
      .apply(springSecurity())
      .build();
  }

  @Test
  void accepts_valid_authentication_tokens() throws Exception {
    List<Map<String, String>> validAuthorities = List.of(
      Map.of("authority", AUTHORITY_CHECKOUT)
    );
    Date oneHourAfterTestExecution = Date.from(Instant.now().plus(Duration.ofHours(1)));
    DefaultClaims validClaims = new DefaultClaims(Map.of(
      EXPIRATION, oneHourAfterTestExecution,
      JWT_CLAIM_AUTHORITIES, validAuthorities
    ));
    String jwt = Jwts.builder()
      .signWith(secretkey)
      .setClaims(validClaims)
      .compact();
    when(claimsParserServiceMock.parseToken(anyString())).thenReturn(validClaims);
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
      .get(ENDPOINT_URL)
      .header(AUTHORIZATION, JWT_PREFIX + jwt);
    mockMvc.perform(requestBuilder)
      .andExpect(status().isOk())
      .andExpect(content().string(POSITIVE_RESPONSE_BODY));
  }


  @Test
  void rejects_invalid_authentication_tokens() throws Exception {
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
      .get(ENDPOINT_URL); // no jwt
    mockMvc.perform(requestBuilder)
      .andExpect(status().isForbidden());
  }

  @TestConfiguration
  @EnableWebSecurity
  static class MockSecurityConfig
    extends WebSecurityConfigurerAdapter {
    final PasswordEncoder passwordEncoder;
    final AuthenticationProvider authenticationProvider;
    final SecretKey secretKey;
    final AuthorizationHeaderParserService<Claims> claimsParserService;

    @Autowired
    MockSecurityConfig(
      PasswordEncoder passwordEncoder,
      AuthenticationProvider authenticationProvider,
      SecretKey secretKey,
      AuthorizationHeaderParserService<Claims> claimsParserService
    ) {
      this.passwordEncoder = passwordEncoder;
      this.authenticationProvider = authenticationProvider;
      this.secretKey = secretKey;
      this.claimsParserService = claimsParserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      JwtTokenVerifierFilter instance = new JwtTokenVerifierFilter(claimsParserService);
      http.authorizeRequests()
        .antMatchers(ENDPOINT_URL).authenticated()
        .and().csrf().disable()
        .addFilterAfter(instance, DigestAuthenticationFilter.class); // first authorization filter
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
      auth.authenticationProvider(authenticationProvider);
    }

    /**
     * A primitive controller for mocking responses. It only accepts authenticated requests.
     * It responds with 200 OK, and the response body is set in plain text format.
     */
    @RestController
    static class SimpleController {

      @GetMapping("/")
      public String respond() {
        return POSITIVE_RESPONSE_BODY;
      }
    }
  }
}
