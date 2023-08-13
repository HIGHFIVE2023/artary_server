package com.highfive.artary.service;

import com.highfive.artary.domain.Friend;
import com.highfive.artary.domain.FriendId;
import com.highfive.artary.domain.NotificationType;
import com.highfive.artary.domain.User;
import com.highfive.artary.dto.FriendDto;
import com.highfive.artary.repository.FriendRepository;
import com.highfive.artary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final FriendRepository friendRepository;

    @Autowired
    private final NotificationService notificationService;

    @Override
    public User searchFriend(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return user;
    }

    @Override
    public List<User> findAll(Long user_id) {
//        List<FriendDto> friendDtos = new ArrayList<>();
        User user = findUserById(user_id);
        List<Friend> friends = friendRepository.findAllByFromUserIdAndAreWeFriendOrToUserIdAndAreWeFriend(user, true, user, true);

        List<User> friendsInfos = new ArrayList<>();

        for (Friend friend : friends) {
            User friendInfo = friend.getToUserId() == user ? friend.getFromUserId() : friend.getToUserId();
            friendsInfos.add(friendInfo);
        }

//        for (Friend friend : friends) {
//            FriendDto friendDto = new FriendDto(friend);
//            friendDtos.add(friendDto);
//        }

        return friendsInfos;
    }

    @Override
    public List<FriendDto> getRequests(Long user_id) {
        List<FriendDto> friendDtos = new ArrayList<>();
        User user = findUserById(user_id);

        List<Friend> friends = friendRepository.findAllByToUserIdAndAreWeFriend(user, false);

        for (Friend friend : friends) {
            FriendDto friendDto = new FriendDto(friend);
            friendDtos.add(friendDto);
        }

        return friendDtos;
    }

    @Override
    public void addFriend(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("자기 자신한테는 친구 신청이 안됨.");
        }

        User fromUser = findUserById(fromUserId);
        User toUser = findUserById(toUserId);

        if (checkFriend(fromUserId, toUserId)) {
            throw new IllegalArgumentException("이미 친구입니다.");
        }

        FriendId friendId = new FriendId(toUserId, fromUserId);
        Friend friendRequest = Friend.builder()
                .id(friendId)
                .toUserId(toUser)
                .fromUserId(fromUser)
                .areWeFriend(false)
                .build();

        fromUser.addFriend(friendRequest);

        // 알림 생성
        String content = fromUser.getNickname() + "님이 친구 신청을 했습니다.";
        String url = "http://localhost:3000/mypage";

        notificationService.send(toUser, NotificationType.FRIEND, content, url);

        userRepository.save(fromUser);
    }

    @Override
    public void replyFriendRequest(Long fromUserId, Long toUserId, Boolean areWeFriend) {
        FriendId friendId = new FriendId(fromUserId, toUserId);
        Friend friend = friendRepository.findById(friendId).orElseThrow(() ->
                new IllegalArgumentException("해당 친구 신청이 존재하지 않습니다."));

        if (areWeFriend == true) {
            friend.setAreWeFriend(true);
            friendRepository.save(friend);
        } else {
            friendRepository.delete(friend);
        }

        // 알림 생성
        User fromUser = findUserById(fromUserId);
        User toUser = findUserById(toUserId);
        String reply = areWeFriend ? "수락" : "거절";
        String content = fromUser.getNickname() + "님이 친구 신청을 " + reply + "했습니다.";
        String url = "http://localhost:3000/mypage";

        notificationService.send(toUser, NotificationType.FRIEND, content, url);
    }

    @Override
    public void deleteFriend(Long fromUserId, Long toUserId) {
        FriendId friendId1 = new FriendId(toUserId, fromUserId);
        FriendId friendId2 = new FriendId(fromUserId, toUserId);

        Optional<Friend> friendOptional = friendRepository.findById(friendId1);
        if (!friendOptional.isPresent()) {
            friendOptional = friendRepository.findById(friendId2);
        }

        Friend friend = friendOptional.orElseThrow(() ->
                new IllegalArgumentException("해당 친구가 존재하지 않습니다."));

        friendRepository.delete(friend);

        // 알림 생성
        User fromUser = findUserById(fromUserId);
        User toUser = findUserById(toUserId);

        String content = fromUser.getNickname() + "님이 친구를 삭제했습니다.";
        String url = "http://localhost:3000/mypage";

        notificationService.send(toUser, NotificationType.FRIEND, content, url);
    }

    // 회원 탈퇴한 사용자의 친구 삭제
    @Override
    public void withdrawalFriend(Long userId) {
        User user = findUserById(userId);

        List<Friend> friendList = friendRepository.findAllByFromUserIdOrToUserId(user, user);
        List<FriendId> friendIds = friendList.stream().map(Friend::getId).collect(Collectors.toList());

        for (FriendId friendId : friendIds) {
            friendRepository.deleteById(friendId);
        }
    }

    @Override
    public Boolean checkFriend(Long userId, Long friendId) {
        List<Friend> friends = findFriendsList(userId, friendId, true);

        if (!friends.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<FriendDto> checkRequest(Long userId, Long friendId) {
        List<Friend> friends = findFriendsList(userId, friendId, false);

        List<FriendDto> friendDtos = new ArrayList<>();

        for (Friend friend : friends) {
            FriendDto friendDto = new FriendDto(friend);
            friendDtos.add(friendDto);
        }

        return friendDtos;
    }

    private User findUserById(Long user_id) {
        User user = userRepository.findById(user_id).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        return user;
    }

    private List<Friend> findFriendsList(Long user1_id, Long user2_id, boolean areWeFriend) {
        User user1 = findUserById(user1_id);
        User user2 = findUserById(user2_id);

        List<Friend> friendsList = friendRepository.findAllByFromUserIdAndToUserIdAndAreWeFriend(user1, user2, areWeFriend);
        friendsList.addAll(friendRepository.findAllByFromUserIdAndToUserIdAndAreWeFriend(user2, user1, areWeFriend));

        return friendsList;
    }
}
