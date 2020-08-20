package cl.blm.newmarketing.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Configuration
@PropertySource("classpath:custom.properties")
public class AppGlobals {
  @Value("${items.per_page}") public int ITEMS_PER_PAGE;

  @Value("${date.format}") public String DATE_FORMAT;

  @Value("${crypto.algorithm}") public String CRYPTOGRAPHIC_ALGORITHM;

  @Value("${crypto.salt}") public String CRYPTOGRAPHIC_SALT;

  @Value("${crypto.charset}") public String CRYPTOGRAPHIC_CHARSET;

  @Value("${session.lifetime}") public long SESSION_LIFETIME;
}
