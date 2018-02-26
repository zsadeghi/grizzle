package me.theyinspire.projects.grizzle.importer;

import me.theyinspire.projects.grizzle.importer.data.DataAccessConfiguration;
import me.theyinspire.projects.grizzle.importer.input.InputConfiguration;
import me.theyinspire.projects.grizzle.importer.web.ControllerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(
        useDefaultFilters = false
)
@EnableAutoConfiguration
@Import({
                InputConfiguration.class,
                ControllerConfiguration.class,
                DataAccessConfiguration.class
        })
public class Launcher {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }

}
