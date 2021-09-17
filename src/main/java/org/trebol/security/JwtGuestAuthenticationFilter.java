package org.trebol.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.trebol.pojo.CustomerPojo;
import org.trebol.pojo.PersonPojo;
import org.trebol.config.SecurityProperties;
import org.trebol.exceptions.BadInputException;
import org.trebol.exceptions.EntityAlreadyExistsException;
import org.trebol.jpa.GenericJpaCrudService;
import org.trebol.jpa.entities.Customer;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtGuestAuthenticationFilter
  extends GenericJwtAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final SecurityProperties jwtProperties;
  private final SecretKey secretKey;
  private final GenericJpaCrudService<CustomerPojo, Customer> customersService;

  public JwtGuestAuthenticationFilter(SecurityProperties jwtProperties, SecretKey secretKey,
                                      AuthenticationManager authenticationManager, GenericJpaCrudService<CustomerPojo, Customer> customersService) {
    super(jwtProperties, secretKey);
    this.authenticationManager = authenticationManager;
    this.jwtProperties = jwtProperties;
    this.secretKey = secretKey;
    this.customersService = customersService;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException {
    if (!HttpMethod.POST.matches(request.getMethod())) {
      return null;
    } else {
      try {
        PersonPojo guestCustomerData = new ObjectMapper().readValue(request.getInputStream(), PersonPojo.class);
        this.saveCustomerData(guestCustomerData);
        Authentication authentication = new UsernamePasswordAuthenticationToken("guest", "guest");
        return authenticationManager.authenticate(authentication);
      } catch (IOException e) {
        throw new BadCredentialsException("Invalid request body for guest session");
      } catch (BadInputException e) {
        throw new BadCredentialsException("Insufficient or invalid profile data for guest");
      }
    }
  }

  private void saveCustomerData(PersonPojo guestData) throws BadInputException {
    try {
      CustomerPojo targetCustomer = new CustomerPojo();
      targetCustomer.setPerson(guestData);
      customersService.create(targetCustomer);
    } catch (EntityAlreadyExistsException e) { }
  }
}
