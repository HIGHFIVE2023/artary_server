package com.highfive.artary.domain;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "diary")
public class Diary extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @NonNull
    private String title;

    @NonNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NonNull
    private String picture;

    @NonNull
    private String emotion;

    @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sticker> stickers = new ArrayList<>();
}
