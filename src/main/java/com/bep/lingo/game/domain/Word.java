package com.bep.lingo.game.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "words", schema = "lingo")
@Getter
@Setter
public class Word {
    @Id
    @Column(name = "word")
    private String word;

    public Word() {

    }

    public Word(String word) {
        this.word = word;
    }
}
