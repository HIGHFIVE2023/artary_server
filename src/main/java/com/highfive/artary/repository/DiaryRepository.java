package com.highfive.artary.repository;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Page<Diary> findByUserOrderByIdDesc(User user, Pageable pageable);
}
