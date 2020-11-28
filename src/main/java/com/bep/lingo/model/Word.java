package com.bep.lingo.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "words", schema = "lingo")
@Data
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
