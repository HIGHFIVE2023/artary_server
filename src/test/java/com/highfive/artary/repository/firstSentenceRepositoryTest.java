package com.highfive.artary.repository;

import com.highfive.artary.domain.firstSentence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class firstSentenceRepositoryTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private firstSentenceRepository firstSentenceRepository;

    @Test
    @DisplayName("첫문장 생성하기")
    public void whenGenerateSentence_thenReturnGeneratedSentence() {
        firstSentence firstSentence = new firstSentence();
        firstSentence.setInputWords("apple banana cherry");

        // generateSentence() 메소드를 실행하여 generatedSentence을 생성한다
        firstSentence.generateSentence();

        firstSentenceRepository.save(firstSentence);

        // generateSentence() 메소드를 실행하여 generatedSentence을 생성한다
        String generatedSentence = firstSentence.getGeneratedSentence();

        // 생성된 generatedSentence이 null이 아니어야 한다
        assertThat(generatedSentence).isNotNull();
    }

}