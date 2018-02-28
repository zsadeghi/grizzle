package me.theyinspire.projects.grizzle.explorer.web;

import me.theyinspire.projects.grizzle.explorer.data.DatabaseService;
import me.theyinspire.projects.grizzle.explorer.web.controller.DatabaseController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {

    @Bean
    public DatabaseController databaseController(final DatabaseService service) {
        return new DatabaseController(service);
    }

}
