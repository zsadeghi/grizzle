package me.theyinspire.proejcts.grizzle.service;

import me.theyinspire.projects.grizzle.repository.LyricsRepository;
import me.theyinspire.projects.grizzle.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ServiceConfiguration {

    @Bean
    public StringService stringService(@Value("${words.reverse}") String reverseMappingFile) throws IOException {
        return new StringService(reverseMappingFile);
    }

    @Bean
    public SearchService searchService(final TokenRepository tokenRepository, final StringService stringService,
                                       final LyricsRepository lyricsRepository) {
        return new SearchService(tokenRepository, lyricsRepository, stringService);
    }

}
