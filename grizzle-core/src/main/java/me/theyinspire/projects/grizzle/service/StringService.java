package me.theyinspire.projects.grizzle.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class StringService {

    private final Map<String, String> reverseMapping;

    public StringService(final String reverseMappingFile) throws IOException {
        final FileSystem system = FileSystems.getDefault();
        final Path path = system.getPath("grizzle-core/src/main/resources/mxm_reverse_mapping.txt");
        reverseMapping = Files.readAllLines(path).stream()
                              .filter(s -> s.contains("<SEP>"))
                              .map(s -> s.split("<SEP>"))
                              .collect(Collectors.toMap(array -> array[1], array -> array[0]));
    }

    public String stem(String text) {
        return reverseMapping.getOrDefault(text, text);
    }

    public String[] tokenize(String text) {
        return text.trim().split("([\\s`~!@#$%^&*()_+\\-={}|\\[\\]\\\\;':\",./<>?]+)");
    }

    public String[] normalize(String text) {
        return Arrays.stream(tokenize(text))
                .map(this::stem)
                .toArray(String[]::new);
    }

}
