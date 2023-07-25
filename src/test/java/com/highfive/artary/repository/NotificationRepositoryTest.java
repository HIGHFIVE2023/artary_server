//package com.highfive.artary.repository;
//
//import com.highfive.artary.domain.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.assertj.core.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class NotificationRepositoryTest {
//
//    @Autowired
//    private EntityManager em;
//
//    @Autowired
//    private NotificationRepository notificationRepository;
//
//    private User userA, userB;
//    private Friend friend;
//    private Diary diary;
//    private Sticker sticker;
//
//    @BeforeEach
//    void setUp(){
//        userA = createUser("userA", "A", "1234", "userA@artary.com");
//        userB = createUser("userB", "B", "1234", "userB@artary.com");
//        friend = createFriend(userA, userB, true);
//        diary = createDiary(userA, "오늘의 일기","오늘은 무언가를 배웠다.","https://example.com/image.jpg", Emotion.HAPPY);
//        sticker = createSticker(userB, diary, StickerType.LOVE);
//    }
//
//    @Test
//    @DisplayName("스티커알림")
//    void stickerAlarm() {
//        // when
//        String message = String.format("%s님이 [%s] 글에 스티커를 남겼습니다.", sticker.getUser().getNickname(), sticker.getDiary().getTitle());
//        Notification notification = createAlarm(sticker.getDiary().getUser(), sticker.getUser(), NotificationType.DIARY, message, false);
//
//        // then
//        Notification userNotification = getStickerAlarm(sticker);
//        assertThat(userNotification).isEqualTo(notification);
//    }
//
//    @Test
//    @DisplayName("친구추가알림")
//    void friendAlarm() {
//        // when
//        String message = String.format("%s님의 친구신청입니다.", friend.getFromUserId().getNickname());
//        Notification notification = createAlarm(friend.getToUserId(), friend.getFromUserId(), NotificationType.FRIEND, message, false);
//
//        // then
//        Notification userNotification = getFriendAlarm(friend);
//        assertThat(userNotification).isEqualTo(notification);
//    }
//
//    @Test
//    @DisplayName("알림확인")
//    void checkAlarm() {
//        // given
//        String message = String.format("%s님의 친구신청입니다.", friend.getFromUserId().getNickname());
//        Notification notification = createAlarm(friend.getToUserId(), friend.getFromUserId(), NotificationType.FRIEND, message, false);
//
//        // when
//        Notification userNotification = getFriendAlarm(friend);
//        userNotification.setChecked(true);
//
//        // then
//        assertThat(notificationRepository.findById(notification.getId()).get().getChecked()).isTrue();
//    }
//
//    @Test
//    @DisplayName("알림조회")
//    void showAlarm() {
//        // given
//        String message1 = String.format("%s님이 [%s] 글에 스티커를 남겼습니다.", sticker.getUser().getNickname(), sticker.getDiary().getTitle());
//        Notification notification1 = createAlarm(sticker.getDiary().getUser(), sticker.getUser(), NotificationType.DIARY, message1, false);
//        String message2 = String.format("%s님의 친구신청입니다.", friend.getFromUserId().getNickname());
//        Notification notification2 = createAlarm(friend.getToUserId(), friend.getFromUserId(), NotificationType.FRIEND, message2, false);
//
//        // when
//        List<Notification> notifications = userA.getNotifications()
//                .stream()
//                .filter(notification -> !notification.getChecked())
//                .collect(Collectors.toList());
//
//        // then
//        assertThat(notifications).hasSize(2);
//    }
//
//    @Test
//    @DisplayName("알림삭제")
//    void deleteAlarm() {
//        // given
//        String message = String.format("%s님의 친구신청입니다.", friend.getFromUserId().getNickname());
//        Notification notification = createAlarm(friend.getToUserId(), friend.getFromUserId(), NotificationType.FRIEND, message, false);
//
//        // when
//        notificationRepository.delete(notification);
//
//        // then
//        assertThat(notificationRepository.findById(notification.getId())).isEmpty();
//    }
//
//    private User createUser(String name, String nickname, String password, String email) {
//        User user = User.builder()
//                .name(name)
//                .nickname(nickname)
//                .password(password)
//                .email(email)
//                .build();
//        em.persist(user);
//        return user;
//    }
//
//    private Diary createDiary(User user, String title, String content, String image, Emotion emotion) {
//        Diary diary = Diary.builder()
//                .user(user)
//                .title(title)
//                .content(content)
//                .image(image)
//                .emotion(emotion)
//                .build();
//        em.persist(diary);
//        return diary;
//    }
//
//    private Sticker createSticker(User user, Diary diary, StickerType type) {
//        Sticker sticker = Sticker.builder()
//                .user(user)
//                .diary(diary)
//                .type(type)
//                .build();
//        diary.addSticker(sticker);
//        em.persist(sticker);
//        return sticker;
//    }
//
//    private Friend createFriend(User toUser, User fromUser, boolean areWeFriend) {
//        FriendId id = new FriendId(toUser.getId(), fromUser.getId());
//
//        Friend friend = Friend.builder()
//                .id(id)
//                .toUserId(toUser)
//                .fromUserId(fromUser)
//                .areWeFriend(areWeFriend)
//                .build();
//
//        toUser.addFriend(friend);
//        fromUser.addFriend(friend);
//
//        em.persist(friend);
//        return friend;
//    }
//
//    private Notification createAlarm(User user, NotificationType type, String content, String url) {
//        Notification notification = Notification.builder()
//                .user(user)
//                .type(type)
//                .content(content)
//                .url(url)
//                .checked(false)
//                .build();
//
//        user.addAlarm(notification);
//
//        notificationRepository.save(notification);
//        return notification;
//    }
//
//    private Notification getFriendAlarm(Friend friend) {
//        Notification notification = friend.getToUserId().getNotifications()
//                .stream()
//                .filter(a -> a.getType().equals(NotificationType.FRIEND))
//                .filter(a -> a.getFromUser().equals(friend.getFromUserId()))
//                .findFirst()
//                .get();
//
//        return notification;
//    }
//
//    private Notification getStickerAlarm(Sticker sticker) {
//        Notification notification = sticker.getDiary().getUser().getNotifications()
//                .stream()
//                .filter(a -> a.getType().equals(NotificationType.DIARY))
//                .filter(a -> a.getFromUser().equals(sticker.getUser()))
//                .findFirst()
//                .get();
//
//        return notification;
//    }
//}