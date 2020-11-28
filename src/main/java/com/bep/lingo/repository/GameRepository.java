package com.bep.lingo.repository;

import com.bep.lingo.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

//https://www.callicoder.com/spring-boot-jpa-hibernate-postgresql-restful-crud-api-example/
@Repository
public interface GameRepository extends JpaRepository<Word, Long> {
    @Query(value = "SELECT word_id, word FROM lingo.words WHERE LENGTH(word) = 5 ORDER BY random() LIMIT 1", nativeQuery = true)
    Word getFiveLetterWord();
}
