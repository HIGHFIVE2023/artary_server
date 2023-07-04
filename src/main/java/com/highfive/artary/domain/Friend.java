package com.highfive.artary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Check;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "friend",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"to_user_id", "from_user_id"})
    })
@Check(constraints = "to_user_id <> from_user_id")
public class Friend {

    @EmbeddedId
    private FriendId id;

    @JsonIgnore
    @MapsId("toUserId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    @NonNull
    private User toUserId;

    @JsonIgnore
    @MapsId("fromUserId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    @NonNull
    private User fromUserId;

    @CreatedDate
    @Column(updatable=false, name="created_at")
    @NonNull
    private LocalDateTime createdAt;

    @Column(name="are_we_friend")
    @NonNull
    private Boolean areWeFriend;

    @Builder
    public Friend(FriendId id, User toUserId, User fromUserId, boolean areWeFriend) {
        this.id = id;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.areWeFriend = areWeFriend;
    }
}
