package com.highfive.artary.repository;

import com.highfive.artary.domain.Friend;
import com.highfive.artary.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
        assertThat(friend.getAreWeFriend()).isEqualTo(false);
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

    private Friend createFriend(User toUser, User fromUser, Boolean areWeFriend) {
        Friend friend = Friend.builder()
                .toUserId(toUser)
                .fromUserId(fromUser)
                .areWeFriend(areWeFriend)
                .build();
        friendRepository.save(friend);
        return friend;
    }
}