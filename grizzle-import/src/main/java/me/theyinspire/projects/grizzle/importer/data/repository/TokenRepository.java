package me.theyinspire.projects.grizzle.importer.data.repository;

import me.theyinspire.projects.grizzle.importer.data.model.Lyrics;
import me.theyinspire.projects.grizzle.importer.data.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Set<Token> findByLyrics(Lyrics lyrics);

    long deleteByLyrics(Lyrics lyrics);

}
