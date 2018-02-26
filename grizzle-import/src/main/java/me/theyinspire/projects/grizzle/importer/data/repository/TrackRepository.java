package me.theyinspire.projects.grizzle.importer.data.repository;

import me.theyinspire.projects.grizzle.importer.data.model.Artist;
import me.theyinspire.projects.grizzle.importer.data.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, String> {

    List<Track> findByArtist(Artist artist);

}
