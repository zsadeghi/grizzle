package me.theyinspire.projects.grizzle.importer.input.reader;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface InputReader<E> extends Iterable<E> {

    long total();

    long current();

    default Stream<Record<E>> stream() {
        return StreamSupport.stream(spliterator(), false).map(data -> new Record<>(current(), data));
    }

}
