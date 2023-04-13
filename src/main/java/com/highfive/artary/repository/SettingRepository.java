package com.highfive.artary.repository;

import com.highfive.artary.domain.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting, Long> {
}
