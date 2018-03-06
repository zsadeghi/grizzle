package me.theyinspire.projects.grizzle.repository;

import me.theyinspire.projects.grizzle.model.Lyrics;
import me.theyinspire.projects.grizzle.model.Token;
import me.theyinspire.projects.grizzle.projection.TokenLyricsId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.Set;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Set<Token> findByLyrics(Lyrics lyrics);

    long deleteByLyrics(Lyrics lyrics);

    Set<Token> findByWordLike(String word);

    Page<TokenLyricsId> findByWord_WordOrderByLyrics_IdAsc(String word, Pageable pageable);

    @Query(
            nativeQuery = true,
            value = "SELECT lyrics_id FROM token_temp INNER JOIN word w ON token_temp.word_id = w.id"
                    + "  WHERE w.word LIKE ?1 ORDER BY lyrics_id ASC \n#pageable#\n",
            countQuery = "SELECT COUNT(*) FROM token_temp INNER JOIN word w ON token_temp.word_id = w.id"
                    + " WHERE w.word LIKE ?1"
    )
    Page<BigInteger> findLyricsIds(String word, Pageable pageable);

    long countDistinctByWord_Word(String word);

}
