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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.trebol.api.models.PersonPojo;
import org.trebol.config.SecurityProperties;
import org.trebol.jpa.services.crud.CustomersCrudService;
import org.trebol.testing.PeopleTestHelper;

import javax.crypto.SecretKey;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
  JwtMockGuestAuthenticationFilterTest.SecurityMockBeans.class,
  JwtMockGuestAuthenticationFilterTest.MockSecurityConfig.class })
@WebAppConfiguration
class JwtMockGuestAuthenticationFilterTest {
  static String GUEST_URL = "/guest";
  @MockBean SecurityProperties securityPropertiesMock;
  @MockBean UserDetailsService userDetailsServiceMock;
  @Autowired WebApplicationContext webApplicationContext;
  PeopleTestHelper peopleTestHelper = new PeopleTestHelper();
  MockMvc mockMvc;

  @BeforeEach
  void beforeEach() {
    when(securityPropertiesMock.getJwtExpirationAfterHours()).thenReturn(1);
    when(userDetailsServiceMock.loadUserByUsername(anyString())).thenReturn(UserDetailsPojo.builder()
      .username("guest")
      .password("guest")
      .authorities(List.of(
        new SimpleGrantedAuthority("checkout")))
      .enabled(true)
      .accountNonLocked(true)
      .accountNonExpired(true)
      .credentialsNonExpired(true)
      .build());
    mockMvc = MockMvcBuilders
      .webAppContextSetup(webApplicationContext)
      .apply(springSecurity())
      .build();
  }

  @Test
  @WithAnonymousUser
  void accepts_authentication() throws Exception {
    PersonPojo profile = peopleTestHelper.personPojoBeforeCreation();
    String jsonRequestBody = new ObjectMapper().writeValueAsString(profile);
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
      .post(GUEST_URL)
      .content(jsonRequestBody);
    mockMvc.perform(requestBuilder)
      .andExpect(status().isOk());
  }

  @TestConfiguration
  @EnableWebSecurity
  static class MockSecurityConfig
    extends WebSecurityConfigurerAdapter {
    @MockBean CustomersCrudService customersService;
    final SecurityProperties securityProperties;
    final UserDetailsService userDetailsService;
    final PasswordEncoder passwordEncoder;
    final DaoAuthenticationProvider daoAuthenticationProvider;

    @Autowired
    MockSecurityConfig(
      SecurityProperties securityProperties,
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder,
      DaoAuthenticationProvider daoAuthenticationProvider
    ) {
      this.securityProperties = securityProperties;
      this.userDetailsService = userDetailsService;
      this.passwordEncoder = passwordEncoder;
      this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
        .antMatchers(GUEST_URL).permitAll()
        .and().csrf().disable()
        .addFilter(guestFilterForUrl(GUEST_URL));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(daoAuthenticationProvider)
        .inMemoryAuthentication()
        .withUser("guest")
        .password(passwordEncoder.encode("guest"))
        .authorities("checkout");
    }

    private JwtGuestAuthenticationFilter guestFilterForUrl(String url) throws Exception {
      SecretKey key = Keys.hmacShaKeyFor("a9s8dy030g8h39f7weh8eufesa0d8f7g".getBytes());
      JwtGuestAuthenticationFilter filter = new JwtGuestAuthenticationFilter(
        securityProperties,
        key,
        super.authenticationManager(),
        customersService);
      filter.setFilterProcessesUrl(url);
      return filter;
    }
  }

  @TestConfiguration
  static class SecurityMockBeans {

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
      PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService
    ) {
      DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
      provider.setPasswordEncoder(passwordEncoder);
      provider.setUserDetailsService(userDetailsService);
      return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder(5);
    }
  }
}
