package com.highfive.artary.domain;

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
    @Column(name = "summary_id")
    private Long id;

    @OneToOne(mappedBy = "summary", fetch = FetchType.LAZY)
    private TemporaryDiary diary;

    @Column(name = "korean")
    private String koSummary;

    @Column(name = "english")
    private String engSummary;

    @Builder
    public Summary(TemporaryDiary diary) {
        this.diary = diary;
    }
}
