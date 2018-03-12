package me.theyinspire.projects.grizzle.web;

import me.theyinspire.projects.grizzle.service.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {

    @Bean
    public SearchController searchController(final SearchService searchService) {
        return new SearchController(searchService);
    }

}
