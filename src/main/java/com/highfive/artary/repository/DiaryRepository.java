package com.highfive.artary.repository;

import com.highfive.artary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
