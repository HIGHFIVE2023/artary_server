package com.highfive.artary.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    @NonNull
    private User toUserId;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="from_user_id")
    private User fromUserId;

    @NonNull
    @CreatedDate
    @Column(updatable=false, name="created_at")
    private LocalDateTime createdAt;

    @NonNull
    @Column(name="are_we_friend")
    private Boolean areWeFriend;

}
