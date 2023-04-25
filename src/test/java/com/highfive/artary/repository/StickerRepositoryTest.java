package com.highfive.artary.repository;

import com.highfive.artary.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class StickerRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private StickerRepository stickerRepository;

    @Autowired
    private DiaryRepository diaryRepository;

    private User userA, userB;
    private Friend friend;
    private Diary diary;
    private Sticker sticker;

    @BeforeEach
    void setUp(){
        userA = createUser("userA", "A", "1234", "userA@artary.com");
        userB = createUser("userB", "B", "1234", "userB@artary.com");
        friend = createFriend(userA, userB, true);
        diary = createDiary(userA, "오늘의 일기","오늘은 무언가를 배웠다.","https://example.com/image.jpg", Emotion.HAPPY);
    }

    @Test
    @DisplayName("스티커 생성")
    void createStickerTest() {
        sticker = createSticker(userB, diary, StickerCategory.LOVE);

        stickerRepository.save(sticker);

        Optional<Sticker> result = stickerRepository.findById(sticker.getId());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(sticker);
    }

    @Test
    @DisplayName("스티커로 다이어리 찾기")
    void stickerDiaryTest() {
        // given
        sticker = createSticker(userB, diary, StickerCategory.LOVE);
        diaryRepository.save(diary);

        // when
        Diary foundDiary = sticker.getDiary();

        // then
        assertThat(foundDiary.getId()).isEqualTo(diary.getId());
    }

    @Test
    @DisplayName("스티커 Url 확인")
    void stickerUrlTest() {
        sticker = createSticker(userB, diary, StickerCategory.LOVE);
        String stickerUrl = sticker.getType().getUrl();
        assertNotNull(stickerUrl);

        Diary foundDiary = sticker.getDiary();
        assertNotNull(foundDiary);

        String diaryStickerUrl = foundDiary.getStickerUrl();
        assertNotNull(diaryStickerUrl);

        log.info("Sticker Url: {}", stickerUrl);
        log.info("Diary Sticker Url: {}", diaryStickerUrl);

        assertEquals(stickerUrl, diaryStickerUrl);
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

    private Sticker createSticker(User user, Diary diary, StickerCategory type) {
        Sticker sticker = Sticker.builder()
                .user(user)
                .diary(diary)
                .type(type)
                .build();
        diary.addSticker(sticker);
        em.persist(sticker);
        return sticker;
    }


    private Friend createFriend(User toUser, User fromUser, boolean areWeFriend) {
        FriendId id = new FriendId(toUser.getId(), fromUser.getId());

        Friend friend = Friend.builder()
                .id(id)
                .toUserId(toUser)
                .fromUserId(fromUser)
                .areWeFriend(areWeFriend)
                .build();

        toUser.addFriend(friend);
        fromUser.addFriend(friend);

        em.persist(friend);
        return friend;
    }
}