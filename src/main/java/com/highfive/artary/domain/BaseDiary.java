package com.highfive.artary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "user")
@MappedSuperclass
public abstract class BaseDiary extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @NotEmpty
    private String title;

    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String image;

    @NonNull
    private Emotion emotion;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "summary_id")
    private Summary summary;

    public BaseDiary(User user, String title, String content, String image, Emotion emotion) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.image = image;
        this.emotion = emotion;
    }

    public void update(String title, String content, String image, Emotion emotion) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.emotion = emotion;
    }
}
