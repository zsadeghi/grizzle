package me.theyinspire.projects.grizzle.repository;

import me.theyinspire.projects.grizzle.model.Lyrics;
import me.theyinspire.projects.grizzle.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LyricsRepository extends JpaRepository<Lyrics, Long> {

    Lyrics findByTrack(Track track);

    @Query(
            nativeQuery = true,
            value = "SELECT t.title FROM lyrics l INNER JOIN track t ON (l.track_id = t.id) WHERE l.id = ?1"
    )
    String findTrackTitle(Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT a.familiarity"
                    + " FROM lyrics l"
                    + " INNER JOIN track t ON l.track_id = t.id"
                    + " INNER JOIN artist a ON t.artist_id = a.id"
                    + " WHERE l.id = ?1"
    )
    Double findArtistFamiliarity(Long id);

}
