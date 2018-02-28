package me.theyinspire.projects.grizzle.repository;

import me.theyinspire.projects.grizzle.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String> {



}
