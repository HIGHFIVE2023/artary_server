package com.highfive.artary.dto;

import com.highfive.artary.domain.Friend;
import com.highfive.artary.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class FriendDto {
    private User toUserId;
    private User fromUserId;
    private Boolean areWeFriend;

    public FriendDto(Friend friend) {
       this.toUserId = friend.getToUserId();
       this.fromUserId = friend.getFromUserId();
       this.areWeFriend = friend.getAreWeFriend();
    }
}
