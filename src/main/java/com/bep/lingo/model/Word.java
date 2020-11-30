package com.bep.lingo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "words", schema = "lingo")
@Getter
@Setter
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long wordId;
    @Column(name = "word")
    private String word;

    public Word() {

    }

    public Word(Long wordId, String word) {
        this.wordId = wordId;
        this.word = word;
    }


}
