package cl.blm.trebol.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Commodity wrapper for RestTemplate
 * Based on code taken from https://stackoverflow.com/questions/22338176/spring-http-client
 *
 * @author Benjamin La Madrid <bg.lamadrid@gmail.com>
 */
public class RestClient {

  private final String server;
  private final RestTemplate rest;
  private final HttpHeaders headers;
  private HttpStatus status;

  public RestClient(String serverUrl) {
    this.rest = new RestTemplate();
    this.headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    headers.add("Accept", "*/*");
    this.server = serverUrl;
  }

  public String get(String uri) {
    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.GET, requestEntity, String.class);
    this.setStatus(responseEntity.getStatusCode());
    return responseEntity.getBody();
  }

  public String post(String uri, String json) {
    HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.POST, requestEntity, String.class);
    this.setStatus(responseEntity.getStatusCode());
    return responseEntity.getBody();
  }

  public void put(String uri, String json) {
    HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
    ResponseEntity<String> responseEntity = rest.exchange(server + uri, HttpMethod.PUT, requestEntity, String.class);
    this.setStatus(responseEntity.getStatusCode());
  }

  public void delete(String uri) {
    HttpEntity<String> requestEntity = new HttpEntity<>("", headers);
    ResponseEntity<Void> responseEntity = rest.exchange(server + uri, HttpMethod.DELETE, requestEntity, Void.class);
    this.setStatus(responseEntity.getStatusCode());
  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }
}
