package com.highfive.artary.repository;

import com.highfive.artary.domain.Friend;
import com.highfive.artary.domain.FriendId;
import com.highfive.artary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    List<Friend> findAllByFromUserIdAndAreWeFriendOrToUserIdAndAreWeFriend(User fromUser, Boolean areWeFriend1, User toUser, Boolean areWeFriend2);
    List<Friend> findAllByFromUserIdAndToUserIdAndAreWeFriend(User fromUser, User toUser, Boolean areWeFriend);

    List<Friend> findAllByToUserIdAndAreWeFriend(User toUser, Boolean areWeFriend);
}
