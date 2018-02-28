package me.theyinspire.projects.grizzle.explorer;


import me.theyinspire.projects.grizzle.explorer.data.DataAccessConfiguration;
import me.theyinspire.projects.grizzle.explorer.data.DatabaseService;
import me.theyinspire.projects.grizzle.explorer.dto.ResultPage;
import me.theyinspire.projects.grizzle.explorer.web.ControllerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration
@ComponentScan(useDefaultFilters = false)
@EnableAutoConfiguration
@Import({
                DataAccessConfiguration.class,
                ControllerConfiguration.class
        })
public class Launcher {

    public static void main(String[] args) throws Exception {
        final ConfigurableApplicationContext context = SpringApplication.run(Launcher.class, args);
        final DatabaseService service = context.getBean(DatabaseService.class);
        final List<String> names = service.tableNames();
        for (String name : names) {
            final ResultPage page = service.fetch(name, 1);
        }
    }

}
