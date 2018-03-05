package me.theyinspire.projects.grizzle.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(
        name = "word",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "word")
        }
)
public class Word {

    private Long id;
    private String word;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @Column(length = 2048)
    public String getWord() {
        return word;
    }

    public void setWord(final String word) {
        this.word = word;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Word word1 = (Word) o;
        return Objects.equals(word, word1.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                '}';
    }

}
