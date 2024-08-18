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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
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
import static org.trebol.config.Constants.AUTHORITY_CHECKOUT;

/**
 * Spring Security integration tests for the JwtMockGuestAuthenticationFilter<br/>
 * For more insight on the how's and why's, check out these links.<br/>
 * <ul>
 * <li><a href="https://docs.spring.io/spring-security/site/docs/5.3.x/reference/html5/#test">Spring Security Reference - 19.Testing</a></li>
 * <li><a href="https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework">Spring Framework Reference - 7.MockMvc</a></li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(classes = {
    SecurityTestingConfig.class,
    JwtGuestAuthenticationFilterTest.MockSecurityConfig.class})
@WebAppConfiguration
class JwtGuestAuthenticationFilterTest {
    static final String GUEST_URL = "/guest";
    static final String USERNAME = "guest";
    static final String PASSWORD = USERNAME;
    static List<GrantedAuthority> GUEST_AUTHORITIES;
    @MockBean SecurityProperties securityPropertiesMock;
    @Autowired WebApplicationContext webApplicationContext;
    PeopleTestHelper peopleTestHelper = new PeopleTestHelper();
    MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        GUEST_AUTHORITIES = List.of(
            new SimpleGrantedAuthority(AUTHORITY_CHECKOUT));
    }

    @BeforeEach
    void beforeEach() {
        when(securityPropertiesMock.getJwtExpirationAfterHours()).thenReturn(1);
        when(securityPropertiesMock.getGuestUserName()).thenReturn(USERNAME);
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
    static class MockSecurityConfig {
        @MockBean
        CustomersCrudService customersService;
        final SecurityProperties securityProperties;
        final PasswordEncoder passwordEncoder;
        final SecretKey secretKey;

        @Autowired
        MockSecurityConfig(
            SecurityProperties securityProperties,
            PasswordEncoder passwordEncoder,
            SecretKey secretKey
        ) {
            this.securityProperties = securityProperties;
            this.passwordEncoder = passwordEncoder;
            this.secretKey = secretKey;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests()
                .requestMatchers(GUEST_URL).permitAll()
                .and().csrf().disable()
                .addFilter(guestFilterForUrl());
            return http.build();
        }

        @Bean
        public InMemoryUserDetailsManager userDetailsService() {
            UserDetails user = User.withUsername(USERNAME)
                .password(this.passwordEncoder.encode(PASSWORD))
                .authorities(GUEST_AUTHORITIES)
                .build();
            return new InMemoryUserDetailsManager(user);
        }

        private JwtGuestAuthenticationFilter guestFilterForUrl() {
            JwtGuestAuthenticationFilter filter = new JwtGuestAuthenticationFilter(
                securityProperties,
                secretKey,
                new ProviderManager(this.daoAuthenticationProvider()),
                customersService);
            filter.setFilterProcessesUrl(GUEST_URL);
            return filter;
        }

        private DaoAuthenticationProvider daoAuthenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
            provider.setUserDetailsService(this.userDetailsService());
            return provider;
        }
    }
}
