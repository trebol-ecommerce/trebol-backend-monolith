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

package org.trebol.config;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.trebol.config.exceptions.CorsMappingParseException;
import org.trebol.jpa.services.crud.CustomersCrudService;
import org.trebol.security.JwtGuestAuthenticationFilter;
import org.trebol.security.JwtLoginAuthenticationFilter;
import org.trebol.security.JwtTokenVerifierFilter;
import org.trebol.security.services.AuthorizationHeaderParserService;

import javax.crypto.SecretKey;

import static org.trebol.config.Constants.AUTHORITY_CHECKOUT;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig
  extends WebSecurityConfigurerAdapter {
  private final UserDetailsService userDetailsService;
  private final SecretKey secretKey;
  private final SecurityProperties securityProperties;
  private final CorsProperties corsProperties;
  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  private final CustomersCrudService customersService;

  @Autowired
  public SecurityConfig(
    UserDetailsService userDetailsService,
    SecretKey secretKey,
    SecurityProperties securityProperties,
    AuthorizationHeaderParserService<Claims> jwtClaimsParserService,
    CorsProperties corsProperties,
    CustomersCrudService customersService
  ) {
    this.userDetailsService = userDetailsService;
    this.secretKey = secretKey;
    this.securityProperties = securityProperties;
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.corsProperties = corsProperties;
    this.customersService = customersService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .headers()
        .frameOptions().sameOrigin().and()
        .cors().and()
        .csrf().disable()
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .addFilter(this.loginFilterForUrl("/public/login"))
      .addFilterAfter(
        this.guestFilterForUrl("/public/guest"),
        JwtLoginAuthenticationFilter.class)
      .addFilterAfter(
        new JwtTokenVerifierFilter(jwtClaimsParserService),
        JwtGuestAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
    if (securityProperties.isGuestUserEnabled() &&
      !securityProperties.getGuestUserName().isBlank()) {
      String credential = securityProperties.getGuestUserName();
      auth.inMemoryAuthentication()
        .withUser(credential)
        .password(passwordEncoder().encode(credential))
        .authorities(AUTHORITY_CHECKOUT);
    }
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() throws CorsMappingParseException {
    return new CorsConfigurationSourceBuilder(corsProperties.getListDelimiter())
      .allowedHeaders(corsProperties.getAllowedHeaders())
      .allowedOrigins(corsProperties.getAllowedOrigins())
      .corsMappings(corsProperties.getMappings())
      .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    int strength = securityProperties.getBcryptEncoderStrength();
    return new BCryptPasswordEncoder(strength);
  }

  private UsernamePasswordAuthenticationFilter loginFilterForUrl(String url) throws Exception {
    JwtLoginAuthenticationFilter filter = new JwtLoginAuthenticationFilter(
      securityProperties,
      secretKey,
      super.authenticationManager());
    filter.setFilterProcessesUrl(url);
    return filter;
  }

  private UsernamePasswordAuthenticationFilter guestFilterForUrl(String url) throws Exception {
    JwtGuestAuthenticationFilter filter = new JwtGuestAuthenticationFilter(
      securityProperties,
      secretKey,
      super.authenticationManager(),
      customersService);
    filter.setFilterProcessesUrl(url);
    return filter;
  }
}
