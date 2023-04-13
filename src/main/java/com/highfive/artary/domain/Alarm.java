package com.highfive.artary.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@Table(name = "alarm")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    @NonNull
    private User toUserId;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="from_user_id")
    private User fromUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    @NonNull
    private Diary diary;

    @NonNull
    @Column(name = "alarm_type")
    private String alarmType;

    @NonNull
    @Column(name = "alarm_content")
    private String alarmContent;


    @Column(updatable=false, name="created_at")
    private LocalDateTime createdAt;

    private Boolean checked;
}
