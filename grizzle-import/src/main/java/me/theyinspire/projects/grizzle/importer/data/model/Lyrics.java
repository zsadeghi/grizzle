package me.theyinspire.projects.grizzle.importer.data.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class Lyrics {

    private Long id;
    private Track track;
    private Integer musixMatchId;
    private Set<Token> tokens;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    @OneToOne
    public Track getTrack() {
        return track;
    }

    public void setTrack(final Track track) {
        this.track = track;
    }

    public Integer getMusixMatchId() {
        return musixMatchId;
    }

    public void setMusixMatchId(final Integer musixMatchId) {
        this.musixMatchId = musixMatchId;
    }

    @Transient
    public Set<Token> getTokens() {
        return tokens;
    }

    public void setTokens(final Set<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "Lyrics{" +
                "track=" + track +
                ", musixMatchId=" + musixMatchId +
                ", tokens=" + tokens +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Lyrics lyrics = (Lyrics) o;
        return Objects.equals(track, lyrics.track) &&
                Objects.equals(musixMatchId, lyrics.musixMatchId) &&
                Objects.equals(tokens, lyrics.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(track, musixMatchId, tokens);
    }

}
