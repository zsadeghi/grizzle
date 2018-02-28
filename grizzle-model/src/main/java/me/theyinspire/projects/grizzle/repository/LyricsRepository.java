package me.theyinspire.projects.grizzle.repository;

import me.theyinspire.projects.grizzle.model.Lyrics;
import me.theyinspire.projects.grizzle.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LyricsRepository extends JpaRepository<Lyrics, Long> {

    Lyrics findByTrack(Track track);

}
