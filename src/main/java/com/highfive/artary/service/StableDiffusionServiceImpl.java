package com.highfive.artary.service;

import com.highfive.artary.domain.Diary;
import com.highfive.artary.dto.stablediffusion.StableDiffusionRequestDto;
import com.highfive.artary.dto.stablediffusion.StableDiffusionResponseDto;
import com.highfive.artary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class StableDiffusionServiceImpl implements StableDiffusionService {

    private final DiaryRepository diaryRepository;

    private static final String API_URL = "https://stablediffusionapi.com/api/v3/text2img";
    @Value("${STABLE-DIFFUSION-KEY}")
    private String key;

    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
            .build();

    private WebClient client = WebClient.builder()
            .baseUrl(API_URL)
            .exchangeStrategies(exchangeStrategies)
            .build();

    @Override
    public String getTextToImage(Long diary_id) {
        Diary diary = diaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 일기가 존재하지 않습니다."));

        String prompt = "water color painting of \'" + diary.getSummary().getEngSummary()
                + "\', minimalism, vaporwave aesthetic, Cute, Energetic, Dreamcore style, vibrant colors";

        StableDiffusionRequestDto requestDto = StableDiffusionRequestDto.builder()
                .key(key)
                .prompt(prompt)
                .width(512)
                .height(512)
                .samples(1)
                .num_inference_steps(20)
                .guidance_scale(7.5)
                .safety_checker("yes")
                .build();

        String imageUrl = client.post()
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(StableDiffusionResponseDto.class)
                .block()
                .getOutput()
                .get(0);

        byte[] imageBytes = client.get()
                .uri(imageUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        diary.setImage(imageUrl);
        diaryRepository.save(diary);

        return imageUrl;
    }

}
