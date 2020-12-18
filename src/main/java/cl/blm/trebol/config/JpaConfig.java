package cl.blm.trebol.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Configuration
@EntityScan(basePackages = {"cl.blm.trebol.jpa.entities"})
@EnableJpaRepositories(basePackages = {"cl.blm.trebol.jpa.repositories"})
public class JpaConfig {
}
