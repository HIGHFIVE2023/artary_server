package com.highfive.artary.service;


import com.highfive.artary.domain.FirstSentence;
import com.highfive.artary.dto.textGeneration.FirstSentenceRequestDto;

public interface FirstSentenceService {

    FirstSentence save(FirstSentenceRequestDto requestDto);

    String generateText(Long inputId);
}
