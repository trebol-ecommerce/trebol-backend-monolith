package org.trebol.config;

import javax.crypto.SecretKey;

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
import org.trebol.pojo.CustomerPojo;
import org.trebol.exceptions.CorsMappingParseException;

import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.entities.Customer;
import org.trebol.security.JwtGuestAuthenticationFilter;
import org.trebol.security.JwtTokenVerifierFilter;
import org.trebol.security.JwtLoginAuthenticationFilter;
import org.trebol.security.IAuthorizationHeaderParserService;

@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig
    extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final SecretKey secretKey;
  private final SecurityProperties securityProperties;
  private final CorsProperties corsProperties;
  private final IAuthorizationHeaderParserService<Claims> jwtClaimsParserService;
  private final GenericJpaCrudService<CustomerPojo, Customer> customersService;

  @Autowired
  public SecurityConfig(UserDetailsService userDetailsService, SecretKey secretKey,
    SecurityProperties securityProperties, IAuthorizationHeaderParserService<Claims> jwtClaimsParserService,
    CorsProperties corsProperties, GenericJpaCrudService<CustomerPojo, Customer> customersService) {
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
            .frameOptions().sameOrigin()
          .and()
        .cors()
          .and()
        .csrf()
            .disable()
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
        .addFilter(
            this.loginFilterForUrl("/public/login"))
        .addFilterAfter(
            this.guestFilterForUrl("/public/guest"),
            JwtLoginAuthenticationFilter.class)
        .addFilterAfter(
            new JwtTokenVerifierFilter(jwtClaimsParserService),
            JwtGuestAuthenticationFilter.class)
        .authorizeRequests()
            .antMatchers(
              "/",
              "/public/login",
              "/public/register",
              "/public/about",
              "/public/categories",
              "/public/categories/*",
              "/public/products",
              "/public/products/*",
              "/public/receipt/*",
              "/public/checkout",
              "/public/checkout/validate")
                .permitAll()
            .antMatchers(
              "/data/customers",
              "/data/customers/*",
              "/data/images",
              "/data/images/*",
              "/data/people",
              "/data/people/*",
              "/data/product_categories",
              "/data/product_categories/*",
              "/data/products",
              "/data/products/*",
              "/data/sales",
              "/data/sales/*",
              "/data/salespeople",
              "/data/salespeople/*",
              "/data/sell_statuses",
              "/data/user_roles",
              "/data/users",
              "/data/users/*",
              "/access",
              "/access/*",
              "/account/profile")
                .authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
    auth.inMemoryAuthentication()
            .withUser("guest")
            .password(passwordEncoder().encode("guest"))
            .authorities("checkout");
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
    return new CorsConfigurationSourceBuilder(corsProperties).build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    Integer strength = securityProperties.getBcryptEncoderStrength();
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
