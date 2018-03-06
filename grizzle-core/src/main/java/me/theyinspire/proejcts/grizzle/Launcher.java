package me.theyinspire.proejcts.grizzle;

import me.theyinspire.proejcts.grizzle.data.DataAccessConfiguration;
import me.theyinspire.proejcts.grizzle.service.SearchService;
import me.theyinspire.proejcts.grizzle.service.ServiceConfiguration;
import me.theyinspire.projects.grizzle.model.Track;
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
                ServiceConfiguration.class,
                DataAccessConfiguration.class
        })
public class Launcher {

    public static void main(String[] args) throws Exception {
        final ConfigurableApplicationContext context = SpringApplication.run(Launcher.class, args);
        final SearchService searchService = context.getBean(SearchService.class);
        final List<Track> list = searchService.search("Love in this life is amazing, brother");
        System.out.println("list = " + list);
        System.exit(0);
    }

}
