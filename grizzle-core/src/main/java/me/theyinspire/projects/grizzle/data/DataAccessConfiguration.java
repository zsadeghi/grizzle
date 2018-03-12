package me.theyinspire.projects.grizzle.data;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("me.theyinspire.projects.grizzle.repository")
@EntityScan("me.theyinspire.projects.grizzle.model")
public class DataAccessConfiguration {

}
