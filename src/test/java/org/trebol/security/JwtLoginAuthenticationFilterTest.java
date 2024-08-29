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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.trebol.api.models.LoginPojo;
import org.trebol.config.SecurityProperties;

import javax.crypto.SecretKey;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.trebol.config.Constants.AUTHORITY_CHECKOUT;

/**
 * Spring Security integration tests for the JwtLoginAuthenticationFilter<br/>
 * For more insight on the how's and why's, check out these links.<br/>
 * <ul>
 * <li><a href="https://docs.spring.io/spring-security/reference/6.1-SNAPSHOT/servlet/configuration/java.html#jc-httpsecurity">Spring Security Reference - Java Configuration - HttpSecurity</a></li>
 * <li><a href="https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework">Spring Framework Reference - 7.MockMvc</a></li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest({JwtLoginAuthenticationFilterTest.NoOpController.class})
@ContextConfiguration(classes = {
    SecurityTestingConfig.class,
    JwtLoginAuthenticationFilterTest.MockSecurityConfig.class})
class JwtLoginAuthenticationFilterTest {
    static final String LOGIN_URL = "/login";
    static final String USERNAME = "SOME";
    static final String PASSWORD = "BODY";
    static final String POSITIVE_RESPONSE_BODY = "IT WORKS";
    static List<GrantedAuthority> USER_AUTHORITIES;
    @MockBean SecurityProperties securityPropertiesMock;
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        USER_AUTHORITIES = List.of(
            new SimpleGrantedAuthority(AUTHORITY_CHECKOUT));
    }

    @BeforeEach
    void beforeEach() {
        when(securityPropertiesMock.getJwtExpirationAfterHours()).thenReturn(1);
    }

    @Test
    @WithAnonymousUser
    void accepts_authentication() throws Exception {
        String jsonRequestBody = objectMapper.writeValueAsString(LoginPojo.builder()
            .name(USERNAME)
            .password(PASSWORD)
            .build());
        mockMvc.perform(MockMvcRequestBuilders
                .post(LOGIN_URL)
                .content(jsonRequestBody))
            .andExpect(status().isOk());
    }

    @TestConfiguration
    static class MockSecurityConfig {
        final SecurityProperties securityProperties;
        final PasswordEncoder passwordEncoder;
        final SecretKey secretKey;

        @Autowired
        MockSecurityConfig(SecurityProperties securityProperties,
                           PasswordEncoder passwordEncoder,
                           SecretKey secretKey) {
            this.securityProperties = securityProperties;
            this.passwordEncoder = passwordEncoder;
            this.secretKey = secretKey;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(LOGIN_URL).permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilter(this.loginFilterForUrl())
                .build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return new InMemoryUserDetailsManager(
                User.withUsername(USERNAME)
                    .password(passwordEncoder.encode(PASSWORD))
                    .authorities(USER_AUTHORITIES)
                    .build());
        }

        private JwtLoginAuthenticationFilter loginFilterForUrl() {
            JwtLoginAuthenticationFilter filter = new JwtLoginAuthenticationFilter(
                securityProperties,
                secretKey,
                new ProviderManager(this.daoAuthenticationProvider()));
            filter.setFilterProcessesUrl(LOGIN_URL);
            return filter;
        }

        private DaoAuthenticationProvider daoAuthenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
            provider.setUserDetailsService(this.userDetailsService());
            return provider;
        }
    }

    /**
     * A placeholder controller to prevent auto-configuration requiring too many class dependencies.
     */
    @Controller
    static class NoOpController { }
}
