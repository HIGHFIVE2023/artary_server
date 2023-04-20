package com.highfive.artary.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendId implements Serializable {
    @Column(name = "to_user_id")
    private Long toUserId;

    @Column(name = "from_user_id")
    private Long fromUserId;
}
