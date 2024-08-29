/*
 * Copyright (c) 2020-2024 The Trebol eCommerce Project
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.trebol.jpa.services.crud.CustomersCrudService;
import org.trebol.security.JwtGuestAuthenticationFilter;
import org.trebol.security.JwtLoginAuthenticationFilter;
import org.trebol.security.JwtTokenVerifierFilter;
import org.trebol.security.services.AuthorizationHeaderParserService;

import javax.crypto.SecretKey;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final SecretKey secretKey;
    private final SecurityProperties securityProperties;
    private final AuthorizationHeaderParserService<Claims> jwtClaimsParserService;
    private final CustomersCrudService customersService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService,
                          SecretKey secretKey,
                          SecurityProperties securityProperties,
                          AuthorizationHeaderParserService<Claims> jwtClaimsParserService,
                          CustomersCrudService customersService) {
        this.userDetailsService = userDetailsService;
        this.secretKey = secretKey;
        this.securityProperties = securityProperties;
        this.jwtClaimsParserService = jwtClaimsParserService;
        this.customersService = customersService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .authenticationManager(this.authenticationManager())
            .headers(configure -> configure.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(configure -> configure.sessionCreationPolicy(STATELESS))
            .addFilter(this.loginFilterForUrl("/public/login"))
            .addFilterAfter(this.guestFilterForUrl("/public/guest"),
                            JwtLoginAuthenticationFilter.class)
            .addFilterAfter(new JwtTokenVerifierFilter(jwtClaimsParserService),
                            JwtGuestAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        int strength = securityProperties.getBcryptEncoderStrength();
        return new BCryptPasswordEncoder(strength);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        if (this.authenticationManager == null) {
            this.authenticationManager = new ProviderManager(this.daoAuthenticationProvider());
        }
        return this.authenticationManager;
    }

    private DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(this.passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    private UsernamePasswordAuthenticationFilter loginFilterForUrl(String url) throws Exception {
        JwtLoginAuthenticationFilter filter = new JwtLoginAuthenticationFilter(
            securityProperties,
            secretKey,
            authenticationManager);
        filter.setFilterProcessesUrl(url);
        return filter;
    }

    private UsernamePasswordAuthenticationFilter guestFilterForUrl(String url) throws Exception {
        JwtGuestAuthenticationFilter filter = new JwtGuestAuthenticationFilter(
            securityProperties,
            secretKey,
            authenticationManager,
            customersService);
        filter.setFilterProcessesUrl(url);
        return filter;
    }
}
