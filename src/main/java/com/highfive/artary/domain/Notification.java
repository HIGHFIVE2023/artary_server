package com.highfive.artary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@Table(name = "alarm")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @NonNull
    @Column(name = "alarm_type")
    private NotificationType type;

    private String url;

    @NonNull
    @Column(name = "alarm_content")
    private String content;

    @Column(updatable=false, name="created_at")
    private LocalDateTime createdAt;

    @Column(columnDefinition = "Boolean default false")
    private Boolean checked;

    @Builder
    public Notification(User user, NotificationType type, String content, String url, Boolean checked) {
        this.user = user;
        this.type = type;
        this.content = content;
        this.url = url;
        this.createdAt = LocalDateTime.now();
        this.checked = checked;
    }
}
