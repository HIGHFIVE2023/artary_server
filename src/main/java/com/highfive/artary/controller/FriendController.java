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

    @Autowired
    private final FriendService friendService;

    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> getFriendsList(@AuthenticationPrincipal String email) {
        Long userId = findIdByEmail(email);

        List<FriendDto> friendDtoList = friendService.findAll(userId);

        return new ResponseEntity<>(friendDtoList, HttpStatus.OK);
    }

    @GetMapping("/search/{email}")
    public ResponseEntity<?> searchFriend(@PathVariable String email) {
        User searchFriend = friendService.searchFriend(email);

        return new ResponseEntity<>(searchFriend, HttpStatus.OK);
    }

    @PostMapping("/{friend_email}")
    public ResponseEntity addFriend(@PathVariable String friend_email, @AuthenticationPrincipal String email) {
        Long toUserId = findIdByEmail(friend_email);
        Long fromUserId = findIdByEmail(email);

        friendService.addFriend(fromUserId, toUserId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/{friend_email}/{are_we_friend}")
    public ResponseEntity replyFriendRequest(@PathVariable String friend_email, @PathVariable Boolean are_we_friend, @AuthenticationPrincipal String email) {
        Long toUserId = findIdByEmail(friend_email);
        Long fromUserId = findIdByEmail(email);

        friendService.replyFriendRequest(fromUserId, toUserId, are_we_friend);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{friend_email}")
    public ResponseEntity deleteFriend(@PathVariable String friend_email, @AuthenticationPrincipal String email) {
        Long toUserId = findIdByEmail(friend_email);
        Long fromUserId = findIdByEmail(email);

        friendService.deleteFriend(fromUserId, toUserId);

        return new ResponseEntity(HttpStatus.OK);
    }

    private Long findIdByEmail(String email) {
        Long userId = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")).getId();

        return userId;
    }
}
