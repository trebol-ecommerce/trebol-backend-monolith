package cl.blm.trebol.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cl.blm.trebol.config.CorsProperties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter for CORS headers
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
public class CorsFilter
    implements Filter {

  private final String origins;
  private final Map<String, String> corsMappings;

  public CorsFilter(CorsProperties corsProperties) {
    this.origins = corsProperties.getOrigins();
    this.corsMappings = new HashMap<>();
    corsMappings.put("/", "GET,OPTIONS");
    corsMappings.putAll(corsProperties.getStoreMappingsAsMap());
    corsMappings.putAll(corsProperties.getSessionMappingsAsMap());
    corsMappings.putAll(corsProperties.getAccessMappingsAsMap());
    corsMappings.putAll(corsProperties.getDataMappingsAsMap());
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

      HttpServletRequest request = (HttpServletRequest) req;
      HttpServletResponse response = (HttpServletResponse) res;

      String uri = request.getRequestURI();
      if (corsMappings.containsKey(uri)) {
        String methods =  corsMappings.get(uri);

        response.setHeader("Access-Control-Allow-Origin", origins);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", methods);
        response.setHeader("Access-Control-Max-Age", "300");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization");

        chain.doFilter(req, res);
      }
  }

  @Override
  public void init(FilterConfig filterConfig) {
  }

  @Override
  public void destroy() {
  }

}
