package com.highfive.artary.repository;

import com.highfive.artary.domain.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
