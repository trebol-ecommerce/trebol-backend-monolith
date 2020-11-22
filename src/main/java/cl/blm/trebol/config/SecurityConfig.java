package cl.blm.trebol.config;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import cl.blm.trebol.security.JwtTokenVerifierFilter;
import cl.blm.trebol.security.JwtUsernamePasswordAuthenticationFilter;
import cl.blm.trebol.services.security.AuthorizationHeaderParserService;

@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig
    extends WebSecurityConfigurerAdapter {

  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;
  private final SecretKey secretKey;
  private final JwtProperties jwtConfig;
  private final CorsProperties corsProperties;
  private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;

  @Autowired
  public SecurityConfig(
      PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService,
      SecretKey secretKey,
      JwtProperties jwtConfig,
      AuthorizationHeaderParserService<Claims> jwtClaimsParserService,
      CorsProperties corsProperties) {
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
    this.secretKey = secretKey;
    this.jwtConfig = jwtConfig;
    this.jwtClaimsParserService = jwtClaimsParserService;
    this.corsProperties = corsProperties;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .cors()
        .and()
        .csrf().disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
        .addFilterAfter(new JwtTokenVerifierFilter(jwtClaimsParserService),
            JwtUsernamePasswordAuthenticationFilter.class)
        .authorizeRequests()
        .antMatchers(
            "/",
            "/login",
            "/store/about",
            "/store/front",
            "/store/categories",
            "/store/categories/*",
            "/store/product/*"
        ).permitAll()
        .anyRequest().authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder);
    provider.setUserDetailsService(userDetailsService);
    return provider;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    return new CorsConfigurationSourceBuilder(corsProperties).build();
  }

}
