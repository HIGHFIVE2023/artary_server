package com.highfive.artary.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "first_sentence")
public class firstSentence extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "first_sentence_id")
    private Long id;

    @NonNull
    @Column(name = "input_words")
    private String inputWords;

    @Column(name = "generated_sentence")
    private String generatedSentence;

    public void generateSentence() {
        // 외부 API를 호출하여 inputWords를 사용하여 generatedSentence을 생성하는 로직
        // 임시로 입력 값에 artary- 를 붙이는 것으로 한다.
        this.setGeneratedSentence("artary - " + this.inputWords);
    }
}
