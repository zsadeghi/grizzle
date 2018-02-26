package me.theyinspire.projects.grizzle.importer.data.repository;

import me.theyinspire.projects.grizzle.importer.data.model.Lyrics;
import me.theyinspire.projects.grizzle.importer.data.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LyricsRepository extends JpaRepository<Lyrics, Long> {

    Lyrics findByTrack(Track track);

}
