package cl.blm.trebol.store.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 * @author Benjamin La Madrid <bg.lamadrid at gmail.com>
 */
@Configuration
@EntityScan(basePackages = {"cl.blm.trebol.store.jpa.entities"})
@EnableJpaRepositories(basePackages = {"cl.blm.trebol.store.jpa.repositories"})
@PropertySources({ @PropertySource("classpath:jpa.properties"), @PropertySource("classpath:datasource.properties") })
public class JpaConfig {
}
