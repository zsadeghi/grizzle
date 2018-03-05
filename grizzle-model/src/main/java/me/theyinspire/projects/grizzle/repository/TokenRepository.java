package me.theyinspire.projects.grizzle.repository;

import me.theyinspire.projects.grizzle.model.Lyrics;
import me.theyinspire.projects.grizzle.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Set<Token> findByLyrics(Lyrics lyrics);

    long deleteByLyrics(Lyrics lyrics);

    Set<Token> findByWordLike(String word);

}
