package cl.blm.newmarketing.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Read-only wrapper class for settings read from 'custom.properties' file
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Configuration
@PropertySource("classpath:custom.properties")
public class BackendAppGlobals {
  public final int ITEMS_PER_PAGE;
  public final String DATE_FORMAT;
  public final String CRYPTOGRAPHIC_ALGORITHM;
  public final String CRYPTOGRAPHIC_SALT;
  public final String CRYPTOGRAPHIC_CHARSET;
  public final long SESSION_LIFETIME;

  public BackendAppGlobals(@Value("${items.per_page}") int itemsPerPage, @Value("${date.format}") String dateFormat,
      @Value("${crypto.algorithm}") String cryptoAlgorithm, @Value("${crypto.salt}") String cryptoSalt,
      @Value("${crypto.charset}") String cryptoCharset, @Value("${session.lifetime}") long sessionLifetime) {
    this.ITEMS_PER_PAGE = itemsPerPage;
    this.DATE_FORMAT = dateFormat;
    this.CRYPTOGRAPHIC_ALGORITHM = cryptoAlgorithm;
    this.CRYPTOGRAPHIC_SALT = cryptoSalt;
    this.CRYPTOGRAPHIC_CHARSET = cryptoCharset;
    this.SESSION_LIFETIME = sessionLifetime;
  }
}
