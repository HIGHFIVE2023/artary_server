package com.highfive.artary.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@Table(name = "first_sentence")
public class FirstSentence extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "first_sentence_id")
    private Long id;

    @Column(name = "input_words")
    private String inputWords;

    @Column(name = "generated_sentence")
    private String generatedSentence;

    @Builder
    public FirstSentence(String inputWords) {
        this.inputWords = inputWords;
    }
}
