package com.highfive.artary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Data
@ToString(callSuper = true, exclude = "diary")
@Table(name = "summary")
public class Summary extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sticker_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "diary_id")
    @NonNull
    private Diary diary;

    @Column(name = "korean")
    private String koSummary;

    @Column(name = "english")
    private String engSummary;

    @Builder
    public Summary(Diary diary) {
        this.diary = diary;
    }
}
