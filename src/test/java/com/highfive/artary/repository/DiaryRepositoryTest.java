package com.highfive.artary.repository;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.Emotion;
import com.highfive.artary.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class DiaryRepositoryTest {
    @Autowired
    private EntityManager em;

    @Autowired
    private DiaryRepository diaryRepository;

    @Test
    @DisplayName("일기 작성")
    void createDiaryTest() {
        // given
        User userA = createUser("userA", "A", "1234", "userA@artary.com");
        User userB = createUser("userB", "B", "1234", "userB@artary.com");
        User userC = createUser("userC", "C", "1234", "userC@artary.com");

        Diary diary = createDiary(userA, "오늘의 일기","오늘은 무언가를 배웠다.","https://example.com/image.jpg",Emotion.HAPPY);

        // when
        diaryRepository.save(diary);

        // then
        Optional<Diary> result = diaryRepository.findById(diary.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(diary);

    }

    @Test
    @DisplayName("일기 수정")
    void modifyDiaryTest() {
        // given
        User userA = createUser("userA", "A", "1234", "userA@artary.com");

        Diary diary = createDiary(userA, "오늘의 일기","오늘은 무언가를 배웠다.","https://example.com/image.jpg",Emotion.HAPPY);
        diaryRepository.save(diary);

        // when
        diary.setContent("내용 수정");
        diary.setImage("https://example.com/modified-image.jpg");
        diaryRepository.save(diary);

        // then
        Optional<Diary> result = diaryRepository.findById(diary.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getContent()).isEqualTo("내용 수정");
        assertThat(result.get().getImage()).isEqualTo("https://example.com/modified-image.jpg");
    }

    @Test
    @DisplayName("일기 삭제")
    void deleteDiaryTest() {
        // given
        User userA = createUser("userA", "A", "1234", "userA@artary.com");
        Diary diary = createDiary(userA, "오늘의 일기","오늘은 무언가를 배웠다.","https://example.com/image.jpg",Emotion.HAPPY);
        diaryRepository.save(diary);

        // when
        diaryRepository.delete(diary);

        // then
        Optional<Diary> result = diaryRepository.findById(diary.getId());
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Not null 확인")
    void createEmptyDiaryTest() {
        // given
        User userA = createUser("userA", "A", "1234", "userA@artary.com");

        // when
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> createDiary(userA, null, "오늘은 무언가를 배웠다.", "https://example.com/image.jpg", Emotion.HAPPY));

        // then
        assertThat(exception).isInstanceOf(ConstraintViolationException.class);
    }


    private User createUser(String name, String nickname, String password, String email) {
        User user = User.builder()
                .name(name)
                .nickname(nickname)
                .password(password)
                .email(email)
                .build();
        em.persist(user);
        return user;
    }

    private Diary createDiary(User user, String title, String content, String image, Emotion emotion) {
        Diary diary = Diary.builder()
                .user(user)
                .title(title)
                .content(content)
                .image(image)
                .emotion(emotion)
                .build();
        em.persist(diary);

        return diary;
    }

}