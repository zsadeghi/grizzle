package me.theyinspire.projects.grizzle.importer.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Artist {

    private String id;
    private String musicBrainzId;
    private String name;
    private Double familiarity;
    private Double hotttnesss;

    @Id
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getMusicBrainzId() {
        return musicBrainzId;
    }

    public void setMusicBrainzId(final String musicBrainzId) {
        this.musicBrainzId = musicBrainzId;
    }

    @Column(length = 2048)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Double getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(final Double familiarity) {
        this.familiarity = familiarity;
    }

    public Double getHotttnesss() {
        return hotttnesss;
    }

    public void setHotttnesss(final Double hotttnesss) {
        this.hotttnesss = hotttnesss;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id='" + id + '\'' +
                ", musicBrainzId='" + musicBrainzId + '\'' +
                ", name='" + name + '\'' +
                ", familiarity=" + familiarity +
                ", hotttnesss=" + hotttnesss +
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
        final Artist artist = (Artist) o;
        return Objects.equals(id, artist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
