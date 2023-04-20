package com.highfive.artary.repository;

import com.highfive.artary.domain.Friend;
import com.highfive.artary.domain.FriendId;
import com.highfive.artary.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class FriendRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private FriendRepository friendRepository;

    @Test
    @DisplayName("친구추가")
    void addFriend() {
        // given
        User userA = createUser("userA", "A", "1234", "userA@artary.com");
        User userB = createUser("userB", "B", "1234", "userB@artary.com");

        // when
        Friend friend = createFriend(userA, userB, false);

        // then
        assertThat(friend.getId()).isNotNull();
        assertThat(friend.getToUserId()).isEqualTo(userA);
        assertThat(friend.getFromUserId()).isEqualTo(userB);
        assertThat(friend.getAreWeFriend()).isFalse();
    }

    @Test
    @DisplayName("친구수락")
    void acceptFriend() {
        // given
        User userA = createUser("userA", "A", "1234", "userA@artary.com");
        User userB = createUser("userB", "B", "1234", "userB@artary.com");
        Friend friend = createFriend(userA, userB, false);

        Friend findFriend = userA.getFriends()
                            .stream()
                            .filter(c -> c.getFromUserId().equals(userB))
                            .findFirst()
                            .get();

        // when
        findFriend.setAreWeFriend(true);

        // then
        assertThat(findFriend.getToUserId()).isEqualTo(userA);
        assertThat(findFriend.getFromUserId()).isEqualTo(userB);
        assertThat(findFriend.getAreWeFriend()).isTrue();
    }

    @Test
    @DisplayName("친구목록조회")
    void showFriend() {
        // given
        User userA = createUser("userA", "A", "1234", "userA@artary.com");
        User userB = createUser("userB", "B", "1234", "userB@artary.com");
        User userC = createUser("userC", "C", "1234", "userC@artary.com");
        User userD = createUser("userD", "D", "1234", "userD@artary.com");
        Friend friendAB = createFriend(userA, userB, true); // A-B 친구
        Friend friendAC = createFriend(userA, userC, false); // A-C 친구X
        Friend friendAD = createFriend(userA, userD, true); // A-D 친구

        // when
        List<Friend> friends = userA.getFriends()
                                .stream()
                                .filter(f -> f.getAreWeFriend())
                                .collect(Collectors.toList());

        // then
        assertThat(friends).hasSize(2);
        assertThat(friends).extracting(Friend::getFromUserId).contains(userB);
        assertThat(friends).extracting(Friend::getFromUserId).doesNotContain(userC);
        assertThat(friends).extracting(Friend::getFromUserId).contains(userD);
    }

    @Test
    @DisplayName("친구삭제")
    void deleteFriend() {
        // given
        User userA = createUser("userA", "A", "1234", "userA@artary.com");
        User userB = createUser("userB", "B", "1234", "userB@artary.com");
        Friend friend = createFriend(userA, userB, true);

        // when
        friendRepository.delete(friend);

        // then
        assertThat(friendRepository.findById(friend.getId())).isEmpty();
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

    private Friend createFriend(User toUser, User fromUser, boolean areWeFriend) {
        FriendId id = new FriendId(toUser.getId(), fromUser.getId());

        Friend friend = Friend.builder()
                .id(id)
                .toUserId(toUser)
                .fromUserId(fromUser)
                .areWeFriend(areWeFriend)
                .build();

        toUser.add(friend);
        fromUser.add(friend);

        friendRepository.save(friend);
        return friend;
    }
}