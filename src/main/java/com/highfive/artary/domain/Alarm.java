package com.highfive.artary.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;


import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(force = true)
@Data
@Table(name = "alarm")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "from_user")
    @NonNull
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    @NonNull
    private Diary diary;

    @NonNull
    @Column(name = "alarm_type")
    private AlarmType type;

    @NonNull
    @Column(name = "alarm_content")
    private String content;

    @Column(updatable=false, name="created_at")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "Boolean default false")
    private Boolean checked;

    @Builder
    public Alarm(User user, User fromUser, AlarmType type, String content, Boolean checked) {
        this.user = user;
        this.fromUser = fromUser;
        this.type = type;
        this.content = content;
        this.checked = checked;
    }
}
