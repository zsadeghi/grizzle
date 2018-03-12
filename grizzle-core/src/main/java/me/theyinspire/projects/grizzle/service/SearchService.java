package me.theyinspire.projects.grizzle.service;

import edu.washington.cs.knowitall.morpha.MorphaStemmer;
import me.theyinspire.projects.grizzle.model.Lyrics;
import me.theyinspire.projects.grizzle.repository.LyricsRepository;
import me.theyinspire.projects.grizzle.repository.TokenRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StopWatch;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SearchService {

    private final TokenRepository tokenRepository;
    private final LyricsRepository lyricsRepository;
    private final StringService stringService;
    private final Map<String, List<Long>> queryCache;
    private final Map<String, List<Long>> scoredCache;

    public SearchService(final TokenRepository tokenRepository,
                         final LyricsRepository lyricsRepository,
                         final StringService stringService) {
        this.tokenRepository = tokenRepository;
        this.lyricsRepository = lyricsRepository;
        this.stringService = stringService;
        queryCache = new ConcurrentHashMap<>();
        scoredCache = new ConcurrentHashMap<>();
    }

    public List<Lyrics> search(String query) {
        return recall(query)
                .stream()
                .map(lyricsRepository::findOne)
                .collect(Collectors.toList());
    }

    public ResultPage<Lyrics> rank(String query, boolean includeTokens, int page) {
        final long time = System.currentTimeMillis();
        final int pageSize = 10;
        final String hash = String.join("\\0", Arrays.stream(query.toLowerCase().trim().split("\\s+"))
                                                     .sorted()
                                                     .distinct()
                                                     .collect(Collectors.toList()));
        final List<Long> allItems = scoredCache.computeIfAbsent(hash, s -> {
            final String[] tokens = stringService.tokenize(query);
            final String[] stems = Arrays.stream(tokens)
                                         .map(MorphaStemmer::stem)
                                         .toArray(String[]::new);
            final List<Long> lyricsIds = recall(query);
            final int totalItems = lyricsIds.size();
            final String[] normalized = stringService.normalize(query.toLowerCase());
            final List<Long> documentFrequencies = Arrays.stream(normalized).parallel()
                                                         .map(tokenRepository::getDocumentFrequency)
                                                         .map(BigInteger::longValue)
                                                         .collect(Collectors.toList());
            final List<List<Long>> termFrequencies = lyricsIds.stream().parallel()
                                                              .map(id -> Arrays.stream(normalized)
                                                                               .map(token -> tokenRepository
                                                                                       .getTermFrequency(token, id))
                                                                               .map(value -> value == null
                                                                                       ? BigInteger.ZERO
                                                                                       : value)
                                                                               .map(BigInteger::longValue)
                                                                               .collect(Collectors.toList())
                                                                  )
                                                              .collect(Collectors.toList());
            final List<Indexed<Long>> indexedLyricsIds = new ArrayList<>(totalItems);
            for (int i = 0; i < totalItems; i++) {
                indexedLyricsIds.add(new Indexed<>(i, lyricsIds.get(i)));
            }
            return indexedLyricsIds
                    .stream().parallel()
                    .map(indexed -> new ScoredDocument(indexed.getValue(), score(indexed.getValue(),
                                                                                 documentFrequencies,
                                                                                 termFrequencies
                                                                                         .get(indexed.getIndex()),
                                                                                 stems
                                                                                )
                    ))
                    .sorted(Comparator.reverseOrder())
                    .map(ScoredDocument::getId)
                    .collect(Collectors.toList());
        });
        final int totalItems = allItems.size();
        final List<Lyrics> content = allItems.stream()
                                             .skip((page - 1) * pageSize)
                                             .limit(pageSize)
                                             .map(lyricsRepository::findOne)
                                             .peek(lyrics -> {
                                                 if (includeTokens) {
                                                     lyrics.setTokens(
                                                             tokenRepository.findByLyrics(lyrics)
                                                                            .stream()
                                                                            .peek(token -> token.setLyrics(null))
                                                                            .collect(Collectors.toSet())
                                                                     );
                                                 }
                                             })
                                             .collect(Collectors.toList());
        return new ResultPage<>(content, page, (int) Math.ceil((double) totalItems / pageSize), pageSize, totalItems,
                                System.currentTimeMillis() - time);
    }

    private double score(Long id, List<Long> documentFrequencies, List<Long> termFrequencies, String[] stems) {
        final String trackTitle = lyricsRepository.findTrackTitle(id);
        final Map<String, Long> titleStem = Arrays.stream(trackTitle.toLowerCase().trim().split("\\s+"))
                                                  .map(MorphaStemmer::stem)
                                                  .collect(Collectors.groupingBy(
                                                          Function.identity(),
                                                          Collectors.counting()));

        double score = 0D;
        for (int i = 0; i < documentFrequencies.size(); i++) {
            score += termFrequencies.get(i) * (10.0D / documentFrequencies.get(i));
        }
        for (String stem : stems) {
            score += titleStem.getOrDefault(stem, 0L) * 2;
        }
        final Double artistFamiliarity = lyricsRepository.findArtistFamiliarity(id);
        score += artistFamiliarity * 5;
        return score;
    }

    private List<Long> recall(final String query) {
        return queryCache.computeIfAbsent(query.trim().toLowerCase().replaceAll("\\s+", " "), input -> {
            System.out.println("Starting search for " + input);
            final String[] tokens = stringService.normalize(input.toLowerCase());
            final List<WordStat> stats = prepareWordStats(tokens);
            System.out.println(stats);
            final long totalLyrics = lyricsRepository.count();
            final List<Page<BigInteger>> readers = new ArrayList<>();
            final List<Integer> cursors = new ArrayList<>();
            Long max = Long.MIN_VALUE;
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            final int searchSpan = getSearchSpan(stats, totalLyrics, readers, cursors);
            final List<Long> intersection = new LinkedList<>();
            while (true) {
                int remainingReaders = 0;
                for (int i = 0; i < readers.size(); i++) {
                    if (readers.get(i) == null) {
                        continue;
                    }
                    int high = getCrossingIndex(readers, cursors, max, i);
                    if (handleCrossingIndex(stats, readers, cursors, i, high)) {
                        break;
                    }
                    if (readers.get(i) != null) {
                        remainingReaders++;
                    }
                }
                if (remainingReaders < searchSpan) {
                    break;
                }
                // Now that all cursors are updated, we will check for an intersection.
                max = findMaximum(readers, cursors, max);
                max = checkIntersection(readers, cursors, max, intersection);
            }
            stopWatch.stop();
            System.out.println("Intersection size:" + intersection.size());
            System.out.println("Intersection is: " + intersection);
            System.out.println(stopWatch.prettyPrint());
            return intersection;
        });
    }

    private Long checkIntersection(final List<Page<BigInteger>> readers, final List<Integer> cursors, Long max,
                                   final List<Long> intersection) {
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
        return max;
    }

    private Long findMaximum(final List<Page<BigInteger>> readers, final List<Integer> cursors, Long max) {
        for (int i = 0; i < readers.size(); i++) {
            if (readers.get(i) == null) {
                continue;
            }
            final Long id = readers.get(i).getContent().get(cursors.get(i)).longValue();
            if (id > max) {
                max = id;
            }
        }
        return max;
    }

    private boolean handleCrossingIndex(final List<WordStat> stats, final List<Page<BigInteger>> readers,
                                        final List<Integer> cursors, final int i, final int high) {
        if (high >= readers.get(i).getContent().size()) {
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
                return true;
            }
        } else {
            cursors.set(i, high);
        }
        return false;
    }

    private int getCrossingIndex(final List<Page<BigInteger>> readers, final List<Integer> cursors, final Long max,
                                 final int i) {
        int low = cursors.get(i);
        int high = readers.get(i).getContent().size() - 1;
        while (low != high) {
            int mid = (low + high) / 2;
            if (readers.get(i).getContent().get(mid).longValue() < max) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        while (high < readers.get(i).getContent().size() && readers.get(i).getContent().get(high).longValue() < max) {
            high++;
        }
        return high;
    }

    private int getSearchSpan(final List<WordStat> stats, final long totalLyrics,
                              final List<Page<BigInteger>> readers, final List<Integer> cursors) {
        int searchSpan = 0;
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
        return searchSpan;
    }

    private List<WordStat> prepareWordStats(final String[] tokens) {
        return Arrays.stream(tokens).parallel()
                     .map(token -> new WordStat(token, tokenRepository.getDocumentFrequency(token).longValue()))
                     .sorted()
                     .collect(Collectors.toList());
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

    private static class ScoredDocument implements Comparable<ScoredDocument> {

        private final Long id;
        private final Double score;

        private ScoredDocument(final Long id, final Double score) {
            this.id = id;
            this.score = score;
        }

        public Long getId() {
            return id;
        }

        public Double getScore() {
            return score;
        }

        @Override
        public int compareTo(final ScoredDocument o) {
            return Double.compare(score, o.score);
        }

        @Override
        public String toString() {
            return "ScoredDocument{" +
                    "id=" + id +
                    ", score=" + score +
                    '}';
        }
    }

    private static class Indexed<E> {

        private final int index;
        private final E value;

        private Indexed(final int index, final E value) {
            this.index = index;
            this.value = value;
        }

        private int getIndex() {
            return index;
        }

        private E getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Indexed{" +
                    "index=" + index +
                    ", value=" + value +
                    '}';
        }
    }

}
