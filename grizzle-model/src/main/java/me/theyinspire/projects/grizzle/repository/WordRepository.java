package me.theyinspire.projects.grizzle.repository;

import me.theyinspire.projects.grizzle.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {

    Word findByWord(String word);

}
