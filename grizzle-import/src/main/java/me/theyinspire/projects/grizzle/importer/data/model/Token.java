package me.theyinspire.projects.grizzle.importer.data.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Token {

    private Long id;
    private Lyrics lyrics;
    @Column(length = 2048)
    private String word;
    private Integer count;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @ManyToOne
    public Lyrics getLyrics() {
        return lyrics;
    }

    public void setLyrics(final Lyrics lyrics) {
        this.lyrics = lyrics;
    }

    public String getWord() {
        return word;
    }

    public void setWord(final String word) {
        this.word = word;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(final Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Token token = (Token) o;
        return Objects.equals(word, token.word) &&
                Objects.equals(count, token.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, count);
    }


    @Override
    public String toString() {
        return "Token{" +
                "word='" + word + '\'' +
                ", count=" + count +
                '}';
    }

}
