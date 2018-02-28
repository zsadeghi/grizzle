package me.theyinspire.projects.grizzle.importer.input;

import me.theyinspire.projects.grizzle.repository.ArtistRepository;
import me.theyinspire.projects.grizzle.repository.LyricsRepository;
import me.theyinspire.projects.grizzle.repository.TokenRepository;
import me.theyinspire.projects.grizzle.repository.TrackRepository;
import me.theyinspire.projects.grizzle.importer.input.reader.impl.ArtistInputReader;
import me.theyinspire.projects.grizzle.importer.input.reader.impl.LyricsInputReader;
import me.theyinspire.projects.grizzle.importer.input.reader.impl.TrackInputReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class InputConfiguration {

    @Bean
    public ArtistInputReader artistInputReader(@Value("${db.metadata}") final String file) throws SQLException {
        return new ArtistInputReader(file);
    }

    @Bean
    public TrackInputReader trackInputReader(@Value("${db.metadata}") final String file) throws SQLException {
        return new TrackInputReader(file);
    }

    @Bean
    public LyricsInputReader lyricsInputReader(@Value("${db.mxm}") final String file) throws SQLException {
        return new LyricsInputReader(file);
    }

    @Bean
    public InputConverter inputConverter(final ArtistRepository artistRepository,
                                         final LyricsRepository lyricsRepository,
                                         final TokenRepository tokenRepository,
                                         final TrackRepository trackRepository,
                                         final ArtistInputReader artists,
                                         final TrackInputReader tracks,
                                         final LyricsInputReader lyrics) {
        return new InputConverter(artistRepository, lyricsRepository, tokenRepository, trackRepository, artists, tracks,
                                  lyrics);
    }

}
