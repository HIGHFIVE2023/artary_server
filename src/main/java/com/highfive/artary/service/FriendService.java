package com.highfive.artary.service;

import com.highfive.artary.domain.User;
import com.highfive.artary.dto.FriendDto;

import java.util.ArrayList;
import java.util.List;

public interface FriendService {

    // 친구 검색
    User searchFriend(String email);

    // 친구 목록 조회
    List<User> findAll(Long user_id);

    // 친구 요청 조회
    List<FriendDto> getRequests(Long user_id);

    // 친구 추가
    void addFriend(Long fromUserId, Long toUserId);

    // 친구 수락
    void replyFriendRequest(Long fromUserId, Long toUserId, Boolean areWeFriend);

    // 친구 삭제
    void deleteFriend(Long fromUserId, Long toUserId);

    // 회원 탈퇴한 친구 삭제
    void withdrawalFriend(Long userId);

    // 친구인지 확인
    Boolean checkFriend(Long userId, Long friendId);

    // 친구 요청 확인
    List<FriendDto> checkRequest(Long userId, Long friendId);
}
