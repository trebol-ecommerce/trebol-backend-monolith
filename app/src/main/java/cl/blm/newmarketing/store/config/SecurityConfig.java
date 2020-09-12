package cl.blm.newmarketing.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import cl.blm.newmarketing.store.services.security.impl.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig
    extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .antMatcher("/api/*").authorizeRequests().anyRequest().authenticated();
  }

  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    // TODO implement an AuthenticationManager
    return super.authenticationManager();
  }

  @Override
  protected UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
  }

}
