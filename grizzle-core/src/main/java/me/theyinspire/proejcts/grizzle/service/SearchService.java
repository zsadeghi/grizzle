package me.theyinspire.proejcts.grizzle.service;

import me.theyinspire.projects.grizzle.model.Track;
import me.theyinspire.projects.grizzle.repository.LyricsRepository;
import me.theyinspire.projects.grizzle.repository.TokenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StopWatch;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchService {

    private final TokenRepository tokenRepository;
    private final LyricsRepository lyricsRepository;
    private final StringService stringService;

    public SearchService(final TokenRepository tokenRepository,
                         final LyricsRepository lyricsRepository,
                         final StringService stringService) {
        this.tokenRepository = tokenRepository;
        this.lyricsRepository = lyricsRepository;
        this.stringService = stringService;
    }

    public List<Track> search(String query) {
        System.out.println("Starting search for " + query);
        final String[] tokens = stringService.normalize(query.toLowerCase());
        final List<WordStat> stats = Arrays.stream(tokens)
                                           .map(token -> new WordStat(token, tokenRepository
                                                   .countDistinctByWord_Word(token)))
                                           .sorted()
                                           .collect(Collectors.toList());
        System.out.println(stats);
        final long totalLyrics = lyricsRepository.count();
        final List<Page<BigInteger>> readers = new ArrayList<>();
        final List<Integer> cursors = new ArrayList<>();
        Long max = Long.MIN_VALUE;
        int searchSpan = 0;
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        for (WordStat stat : stats) {
            final long documentFrequency = stat.getFrequency();
            final double ratio = ((double) documentFrequency) / totalLyrics;
            if (ratio > 0.4) {
                readers.add(null);
                cursors.add(0);
                continue;
            }
            searchSpan++;
            final Page<BigInteger> page = tokenRepository.findLyricsIds(stat.getWord(), new PageRequest(0, 10000));
            if (!page.hasContent()) {
                readers.add(null);
            } else {
                readers.add(page);
            }
            cursors.add(0);
        }
        final List<Long> intersection = new LinkedList<>();
        while (true) {
            int remainingReaders = 0;
            for (int i = 0; i < readers.size(); i++) {
                if (readers.get(i) == null) {
                    continue;
                }
                int low = cursors.get(i);
                final int size = readers.get(i).getContent().size();
                int high = size - 1;
                while (low != high) {
                    int mid = (low + high) / 2;
                    if (readers.get(i).getContent().get(mid).longValue() < max) {
                        low = mid + 1;
                    } else {
                        high = mid;
                    }
                }
                while (high < size && readers.get(i).getContent().get(high).longValue() < max) {
                    high++;
                }
                if (high >= size) {
                    // And the reader has a next page ...
                    if (readers.get(i).hasNext()) {
                        // Replenish the list
                        final Pageable pageable = readers.get(i).nextPageable();
                        readers.set(i, tokenRepository.findLyricsIds(stats.get(i).getWord(), pageable));
                        // and reset the cursor
                        cursors.set(i, 0);
                    } else {
                        // otherwise, set it to null to ignore it later on.
                        readers.set(i, null);
                        break;
                    }
                } else {
                    cursors.set(i, high);
                }
                if (readers.get(i) != null) {
                    remainingReaders++;
                }
            }
            if (remainingReaders < searchSpan) {
                break;
            }
            // Now that all cursors are updated, we will check for an intersection.
            for (int i = 0; i < readers.size(); i++) {
                if (readers.get(i) == null) {
                    continue;
                }
                final Long id = readers.get(i).getContent().get(cursors.get(i)).longValue();
                if (id > max) {
                    max = id;
                }
            }
            boolean isCommon = true;
            for (int i = 0; i < readers.size(); i++) {
                final Page<BigInteger> reader = readers.get(i);
                if (reader == null) {
                    continue;
                }
                if (!max.equals(reader.getContent().get(cursors.get(i)).longValue())) {
                    isCommon = false;
                }
            }
            if (isCommon) {
                intersection.add(max);
                max++;
            }
        }
        stopWatch.stop();
        System.out.println("Intersection size:" + intersection.size());
        System.out.println("Intersection is: " + intersection);
        System.out.println(stopWatch.prettyPrint());
        return null;
    }

    private static class WordStat implements Comparable<WordStat> {

        private final String word;
        private final Long frequency;

        private WordStat(final String word, final Long frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        private String getWord() {
            return word;
        }

        private Long getFrequency() {
            return frequency;
        }

        @Override
        public String toString() {
            return "WordStat{" +
                    "word='" + word + '\'' +
                    ", frequency=" + frequency +
                    '}';
        }

        @Override
        public int compareTo(final WordStat o) {
            return Long.compare(frequency, o.frequency);
        }
    }

}
