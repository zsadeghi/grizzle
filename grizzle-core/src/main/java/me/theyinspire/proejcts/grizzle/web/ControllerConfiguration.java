package me.theyinspire.proejcts.grizzle.web;

import me.theyinspire.proejcts.grizzle.service.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfiguration {

    @Bean
    public SearchController searchController(final SearchService searchService) {
        return new SearchController(searchService);
    }

}
