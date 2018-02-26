package me.theyinspire.projects.grizzle.importer.data;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("me.theyinspire.projects.grizzle.importer.data.repository")
public class DataAccessConfiguration {

}
