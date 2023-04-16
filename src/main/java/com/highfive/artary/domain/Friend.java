package com.highfive.artary.domain;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "to_user_id")
    @NonNull
    private User toUserId;

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
    public Friend(User toUserId, User fromUserId, Boolean areWeFriend) {
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
        this.areWeFriend = areWeFriend;
    }
}
