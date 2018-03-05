package me.theyinspire.projects.grizzle.explorer;


import me.theyinspire.projects.grizzle.explorer.data.DataAccessConfiguration;
import me.theyinspire.projects.grizzle.explorer.web.ControllerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(useDefaultFilters = false)
@EnableAutoConfiguration
@Import({
                DataAccessConfiguration.class,
                ControllerConfiguration.class
        })
public class Launcher {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Launcher.class, args);
    }

}
