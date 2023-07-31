package com.highfive.artary.repository;

import com.highfive.artary.domain.TemporaryDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryDiaryRepository extends JpaRepository<TemporaryDiary, Long> {
}
