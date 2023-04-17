package com.highfive.artary.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Check;


import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@Table(name = "alarm",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"to_user_id", "from_user_id"})
    })
@Check(constraints = "to_user_id <> from_user_id")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "to_user_id")
    @NonNull
    private User toUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    @NonNull
    private User fromUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    @NonNull
    private Diary diary;

    @NonNull
    @Column(name = "alarm_type")
    private String type;

    @NonNull
    @Column(name = "alarm_content")
    private String content;

    @Column(updatable=false, name="created_at")
    private LocalDateTime createdAt;

    private Boolean checked;
}
