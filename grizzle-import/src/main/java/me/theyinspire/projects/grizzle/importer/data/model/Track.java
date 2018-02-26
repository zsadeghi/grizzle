package me.theyinspire.projects.grizzle.importer.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Track {

    private String id;
    @Column(length = 2048)
    private String title;
    private String songId;
    @Column(length = 2048)
    private String album;
    private Artist artist;
    private Double duration;
    private Integer year;
    private Integer sevenDigitalId;

    @Id
    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(final String songId) {
        this.songId = songId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(final String album) {
        this.album = album;
    }

    @ManyToOne
    public Artist getArtist() {
        return artist;
    }

    public void setArtist(final Artist artist) {
        this.artist = artist;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(final Double duration) {
        this.duration = duration;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(final Integer year) {
        this.year = year;
    }

    public Integer getSevenDigitalId() {
        return sevenDigitalId;
    }

    public void setSevenDigitalId(final Integer sevenDigitalId) {
        this.sevenDigitalId = sevenDigitalId;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", songId='" + songId + '\'' +
                ", album='" + album + '\'' +
                ", artist=" + artist +
                ", duration=" + duration +
                ", year=" + year +
                ", sevenDigitalId=" + sevenDigitalId +
                '}';
    }

}
