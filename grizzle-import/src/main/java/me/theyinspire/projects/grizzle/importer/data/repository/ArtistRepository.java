package me.theyinspire.projects.grizzle.importer.data.repository;

import me.theyinspire.projects.grizzle.importer.data.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String> {



}
