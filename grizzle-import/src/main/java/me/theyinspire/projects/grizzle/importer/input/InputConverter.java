package me.theyinspire.projects.grizzle.importer.input;

import me.theyinspire.projects.grizzle.importer.input.reader.impl.ArtistInputReader;
import me.theyinspire.projects.grizzle.importer.input.reader.impl.LyricsInputReader;
import me.theyinspire.projects.grizzle.importer.input.reader.impl.TrackInputReader;
import me.theyinspire.projects.grizzle.importer.web.dto.ErrorResult;
import me.theyinspire.projects.grizzle.model.*;
import me.theyinspire.projects.grizzle.repository.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class InputConverter {

    private final ArtistRepository artistRepository;
    private final LyricsRepository lyricsRepository;
    private final TokenRepository tokenRepository;
    private final TrackRepository trackRepository;
    private final WordRepository wordRepository;
    private final ArtistInputReader artists;
    private final TrackInputReader tracks;
    private final LyricsInputReader lyrics;
    private final AtomicReference<Object> last;
    private final AtomicBoolean stopped;
    private ConverterStatus status;

    public InputConverter(final ArtistRepository artistRepository,
                          final LyricsRepository lyricsRepository,
                          final TokenRepository tokenRepository,
                          final TrackRepository trackRepository,
                          final WordRepository wordRepository,
                          final ArtistInputReader artists,
                          final TrackInputReader tracks,
                          final LyricsInputReader lyrics) {
        this.lyricsRepository = lyricsRepository;
        this.tokenRepository = tokenRepository;
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.wordRepository = wordRepository;
        this.artists = artists;
        this.tracks = tracks;
        this.lyrics = lyrics;
        this.status = Objects.equals(total(), converted()) ? ConverterStatus.DONE : ConverterStatus.IDLE;
        this.last = new AtomicReference<>();
        stopped = new AtomicBoolean(false);
    }

    public void start(Boolean rerun) {
        stopped.set(false);
        if (status.isWorking()) {
            return;
        }
        status = ConverterStatus.PREPARING;
        try {
            if (rerun) {
                deleteAll();
            }
            importArtists(rerun);
            importTracks(rerun);
            importLyrics(rerun);
        } catch (Exception e) {
            System.err.println(last.get());
            e.printStackTrace();
            final ErrorResult result = new ErrorResult();
            result.setTarget(last.get());
            result.setError(e);
            last.set(result);
            status = ConverterStatus.ERROR;
            return;
        }
        status = ConverterStatus.DONE;
    }

    private void importLyrics(final Boolean rerun) {
        checkStopped();
        status = ConverterStatus.CONVERTING_LYRICS;
        lyrics.stream().forEach(lyricsRecord -> {
            checkStopped();
            last.set(lyricsRecord);
            final Lyrics lyrics = lyricsRecord.getData();
            if (!rerun && lyrics.getId() != null && lyricsRepository.exists(lyrics.getId())) {
                return;
            }
            final Set<Token> tokens = lyrics.getTokens();
            lyrics.setTokens(Collections.emptySet());
            final Lyrics savedLyrics = lyricsRepository.saveAndFlush(lyrics);
            tokenRepository.deleteByLyrics(savedLyrics);
            for (Token token : tokens) {
                token.setLyrics(savedLyrics);
                final Word word = token.getWord();
                final Word existingWord = wordRepository.findByWord(word.getWord());
                if (existingWord == null) {
                    token.setWord(wordRepository.saveAndFlush(word));
                } else {
                    token.setWord(existingWord);
                }
                last.set(token);
                tokenRepository.saveAndFlush(token);
            }
        });
    }

    private void importTracks(final Boolean rerun) {
        checkStopped();
        status = ConverterStatus.CONVERTING_TRACKS;
        tracks.stream().forEach(trackRecord -> {
            checkStopped();
            last.set(trackRecord);
            final Track track = trackRecord.getData();
            if (!rerun && trackRepository.exists(track.getId())) {
                return;
            }
            trackRepository.saveAndFlush(track);
        });
    }

    private void importArtists(final Boolean rerun) {
        checkStopped();
        status = ConverterStatus.CONVERTING_ARTISTS;
        artists.stream().forEach(artistRecord -> {
            checkStopped();
            last.set(artistRecord);
            final Artist artist = artistRecord.getData();
            if (!rerun && artistRepository.exists(artist.getId())) {
                return;
            }
            artistRepository.saveAndFlush(artist);
        });
    }

    private void deleteAll() {
        checkStopped();
        tokenRepository.deleteAllInBatch();
        checkStopped();
        lyricsRepository.deleteAllInBatch();
        checkStopped();
        trackRepository.deleteAllInBatch();
        checkStopped();
        artistRepository.deleteAllInBatch();
    }

    private void checkStopped() {
        if (stopped.get()) {
            throw new InputConversionAbortedException();
        }
    }

    public ConverterStatus getStatus() {
        return status.isWorking() ? status : Objects.equals(total(), converted()) ? ConverterStatus.DONE : status;
    }

    public Long total() {
        return artists.total() + tracks.total() + lyrics.total();
    }

    public Long converted() {
        return artistRepository.count() + lyricsRepository.count() + trackRepository.count();
    }

    public Object getLast() {
        return last.get();
    }

    public void stop() {
        if (!status.isWorking()) {
            return;
        }
        stopped.set(true);
    }

}
