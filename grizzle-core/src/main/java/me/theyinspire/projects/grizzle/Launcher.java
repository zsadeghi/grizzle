package me.theyinspire.projects.grizzle;

import me.theyinspire.projects.grizzle.data.DataAccessConfiguration;
import me.theyinspire.projects.grizzle.service.ServiceConfiguration;
import me.theyinspire.projects.grizzle.web.ControllerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(useDefaultFilters = false)
@EnableAutoConfiguration
@Import({
                ServiceConfiguration.class,
                DataAccessConfiguration.class,
                ControllerConfiguration.class
        })
public class Launcher {

    public static void main(String[] args) {
        SpringApplication.run(Launcher.class, args);
    }

}
