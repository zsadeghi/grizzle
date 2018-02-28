package me.theyinspire.projects.grizzle.explorer.data;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@Configuration
@EnableJpaRepositories("me.theyinspire.projects.grizzle.repository")
@EntityScan("me.theyinspire.projects.grizzle.model")
public class DataAccessConfiguration {

    @Bean
    public DatabaseService databaseService(final EntityManager entityManager) {
        return new DatabaseService(entityManager);
    }

}
