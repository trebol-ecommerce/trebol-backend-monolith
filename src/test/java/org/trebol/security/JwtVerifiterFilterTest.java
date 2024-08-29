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

package org.trebol.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.trebol.security.services.AuthorizationHeaderParserService;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static io.jsonwebtoken.Claims.EXPIRATION;
import static java.time.Instant.now;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.trebol.config.Constants.AUTHORITY_CHECKOUT;
import static org.trebol.config.Constants.JWT_CLAIM_AUTHORITIES;
import static org.trebol.config.Constants.JWT_PREFIX;

/**
 * Spring Security integration tests for the JwtVerifiterFilter<br/>
 * For more insight on the how's and why's, check out these links.<br/>
 * <ul>
 * <li><a href="https://docs.spring.io/spring-security/reference/6.1-SNAPSHOT/servlet/configuration/java.html#jc-httpsecurity">Spring Security Reference - Java Configuration - HttpSecurity</a></li>
 * <li><a href="https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework">Spring Framework Reference - 7.MockMvc</a></li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest({JwtVerifiterFilterTest.SimpleController.class})
@ContextConfiguration(classes = {
    SecurityTestingConfig.class,
    JwtVerifiterFilterTest.MockSecurityConfig.class,
    JwtVerifiterFilterTest.SimpleController.class})
class JwtVerifiterFilterTest {
    static final String TEST_ENDPOINT_PATH = "/";
    static final Map<String, String> POSITIVE_RESPONSE_BODY = Map.of("message", "IT WORKS");
    @MockBean AuthorizationHeaderParserService<Claims> claimsParserServiceMock;
    @Autowired SecretKey secretkey;
    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @BeforeEach
    void beforeEach() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void accepts_valid_authentication_tokens() throws Exception {
        DefaultClaims validClaims = new DefaultClaims(Map.of(
            EXPIRATION, Date.from(now().plus(Duration.ofHours(1))),
            JWT_CLAIM_AUTHORITIES, List.of(Map.of("authority", AUTHORITY_CHECKOUT))));
        String expectedResponseBody = objectMapper.writeValueAsString(POSITIVE_RESPONSE_BODY);
        when(claimsParserServiceMock.parseToken(anyString())).thenReturn(validClaims);
        String jwt = Jwts.builder()
            .signWith(secretkey)
            .setClaims(validClaims)
            .compact();
        mockMvc.perform(MockMvcRequestBuilders
                .get(TEST_ENDPOINT_PATH)
                .header(AUTHORIZATION, JWT_PREFIX + jwt))
            .andExpect(status().isOk())
            .andExpect(content().json(expectedResponseBody));
    }


    @Test
    void rejects_absence_of_authentication_tokens() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(TEST_ENDPOINT_PATH))
            .andExpect(status().isForbidden());
    }

    @TestConfiguration
    @EnableWebSecurity
    static class MockSecurityConfig {
        final AuthorizationHeaderParserService<Claims> claimsParserService;

        @Autowired
        MockSecurityConfig(AuthorizationHeaderParserService<Claims> claimsParserService) {
            this.claimsParserService = claimsParserService;
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(TEST_ENDPOINT_PATH).authenticated())
                .sessionManagement(configure -> configure.sessionCreationPolicy(STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(
                    new JwtTokenVerifierFilter(claimsParserService),
                    LogoutFilter.class) // first authorization filter
                .build();
        }
    }

    /**
     * A primitive controller that always responds 200 OK with a predetermined response body.
     */
    @Controller
    static class SimpleController {

        @GetMapping(TEST_ENDPOINT_PATH)
        public ResponseEntity<Map<String, String>> respond() {
            return ResponseEntity.ok(POSITIVE_RESPONSE_BODY);
        }
    }
}
