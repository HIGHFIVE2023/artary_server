package com.highfive.artary.controller;

import com.highfive.artary.domain.User;
import com.highfive.artary.dto.FriendDto;
import com.highfive.artary.repository.UserRepository;
import com.highfive.artary.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("friend")
public class FriendController {


    private final FriendService friendService;

    private final UserRepository userRepository;

    // 친구 목록 조회
    @GetMapping
    public ResponseEntity<?> getFriendsList(@AuthenticationPrincipal String email) {
        Long userId = findIdByEmail(email);

        List<User> friendList = friendService.findAll(userId);

        return new ResponseEntity<>(friendList, HttpStatus.OK);
    }

    // 친구 요청 목록
    @GetMapping("/requests")
    public ResponseEntity<?> getFriendsRequests(@AuthenticationPrincipal String email) {
        Long userId = findIdByEmail(email);

        List<FriendDto> friendDtoList = friendService.getRequests(userId);

        return new ResponseEntity<>(friendDtoList, HttpStatus.OK);
    }

    // 친구 검색 (이메일로)
    @GetMapping("/search/{email}")
    public ResponseEntity<?> searchFriend(@PathVariable String email) {
        User searchFriend = friendService.searchFriend(email);

        return new ResponseEntity<>(searchFriend, HttpStatus.OK);
    }

    // 친구 추가
    @PostMapping("/{friend_email}")
    public ResponseEntity addFriend(@PathVariable String friend_email, @AuthenticationPrincipal String email) {
        Long toUserId = findIdByEmail(friend_email);
        Long fromUserId = findIdByEmail(email);

        friendService.addFriend(fromUserId, toUserId);

        return ResponseEntity.ok().body("{\"message\": \"친구 요청 성공\"}");
    }

    // 친구 요청 응답 (수락 or 거절)
    @PostMapping("/{friend_email}/{are_we_friend}")
    public ResponseEntity replyFriendRequest(@PathVariable String friend_email, @PathVariable Boolean are_we_friend, @AuthenticationPrincipal String email) {
        Long toUserId = findIdByEmail(friend_email);
        Long fromUserId = findIdByEmail(email);

        friendService.replyFriendRequest(fromUserId, toUserId, are_we_friend);

        return ResponseEntity.ok().body("{\"message\": \"친구 요청 응답 성공\"}");
    }

    // 친구 삭제
    @DeleteMapping("/{friend_email}")
    public ResponseEntity deleteFriend(@PathVariable String friend_email, @AuthenticationPrincipal String email) {
        Long toUserId = findIdByEmail(friend_email);
        Long fromUserId = findIdByEmail(email);

        friendService.deleteFriend(fromUserId, toUserId);

        return ResponseEntity.ok().body("{\"message\": \"친구 삭제 성공\"}");
    }

    // 친구인지 확인
    @GetMapping("/checkFriend/{friend_email}")
    @ResponseBody
    public Boolean checkFriend(@PathVariable String friend_email, @AuthenticationPrincipal String email) {
        Long userId = findIdByEmail(email);
        Long friendId = findIdByEmail(friend_email);

        return friendService.checkFriend(userId, friendId);
    }

    // 요청 확인
    @GetMapping("/checkRequest/{friend_email}")
    public ResponseEntity<?> checkRequest(@PathVariable String friend_email, @AuthenticationPrincipal String email) {
        Long userId = findIdByEmail(email);
        Long friendId = findIdByEmail(friend_email);

        List<FriendDto> friendDtoList = friendService.checkRequest(userId, friendId);

        return new ResponseEntity<>(friendDtoList, HttpStatus.OK);
    }

    private Long findIdByEmail(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")).getId();

        return userId;
    }
}
