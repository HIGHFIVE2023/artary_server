package com.highfive.artary.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "temporary_diary")
public class TemporaryDiary extends BaseDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "t_diary_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "summary_id")
    private Summary summary;

    @Builder
    public TemporaryDiary(User user, String title, String content, String image, Emotion emotion) {
        super(user, title, content, image, emotion);
    }
}
