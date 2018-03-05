package me.theyinspire.proejcts.grizzle;

import me.theyinspire.proejcts.grizzle.data.DataAccessConfiguration;
import me.theyinspire.proejcts.grizzle.service.ServiceConfiguration;
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
                DataAccessConfiguration.class
        })
public class Launcher {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Launcher.class, args);
    }

}
