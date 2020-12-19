package com.bep.lingo.game.data;

import com.bep.lingo.game.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//https://www.callicoder.com/spring-boot-jpa-hibernate-postgresql-restful-crud-api-example/
@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    @Query(value = "SELECT word FROM lingo.words WHERE LENGTH(word) = 5 ORDER BY random() LIMIT 1", nativeQuery = true)
    Word getFiveLetterWord();
    @Query(value = "SELECT word FROM lingo.words WHERE LENGTH(word) = 6 ORDER BY random() LIMIT 1", nativeQuery = true)
    Word getSixLetterWord();
    @Query(value = "SELECT word FROM lingo.words WHERE LENGTH(word) = 7 ORDER BY random() LIMIT 1", nativeQuery = true)
    Word getSevenLetterWord();
}
