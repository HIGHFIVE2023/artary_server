package com.highfive.artary.repository;

import com.highfive.artary.domain.FirstSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface FirstSentenceRepository extends JpaRepository<FirstSentence, Long> {

}
