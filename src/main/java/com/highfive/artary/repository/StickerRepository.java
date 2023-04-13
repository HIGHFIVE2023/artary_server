package com.highfive.artary.repository;

import com.highfive.artary.domain.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StickerRepository extends JpaRepository<Sticker, Long> {
}
