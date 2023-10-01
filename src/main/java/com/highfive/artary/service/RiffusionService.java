package com.highfive.artary.service;

import com.highfive.artary.config.RiffusionConfig;
import com.highfive.artary.dto.riffusion.AudioResponseDto;
import com.highfive.artary.dto.riffusion.RiffusionInputDto;
import com.highfive.artary.dto.riffusion.RiffusionRequestDto;
import com.highfive.artary.dto.riffusion.RiffusionResponseDto;
import com.highfive.artary.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiffusionService {

    private static final String API_URL = "https://api.replicate.com/v1/predictions";

    private final RiffusionConfig riffusionConfig;
    private WebClient client;
    ExchangeStrategies exchangeStrategies;


    @PostConstruct
    private void init() {

        String token = riffusionConfig.getToken();
        exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configure -> configure.defaultCodecs().maxInMemorySize(-1))
                .build();

        client = WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .defaultHeader("Authorization", "Token " + token)
                .build();
    }

    public String getPrompt(int emotion){
        String prompt_b = "";
        switch (emotion) {
            case 0:
                prompt_b = "upbeat and joyful melodies with a lively rhythm";
                break;
            case 1:
                prompt_b = "aggressive heavy metal with roaring guitars";
                break;
            case 2:
                prompt_b = "soulful violin melodies with emotive harmonies";
                break;
            case 3:
                prompt_b = "deep, smooth synthwave with a dream-like atmosphere";
                break;
        }

        return prompt_b;
    }

    public String setSeed(int emotion) {
        String seed_image = "";
        switch (emotion) {
            case 0:
                seed_image = "vibes";
                break;
            case 1:
                seed_image = "marim";
                break;
            case 2:
                seed_image = "agile";
                break;
            case 3:
                seed_image = "motorway";
                break;
        }

        return seed_image;
    }

    public String getAudio(String prompt, int emotion) {
        String prompt_b = getPrompt(emotion);
        String seed_image = setSeed(emotion);

        RiffusionInputDto inputDto = RiffusionInputDto.builder()
                .prompt_a(prompt)
                .denoising(0.75)
                .prompt_b(prompt_b)
                .alpha(0.75)
                .num_inference_steps(50)
                .seed_image_id(seed_image)
                .build();

        RiffusionRequestDto requestDto = RiffusionRequestDto.builder()
                .version("8cf61ea6c56afd61d8f5b9ffd14d7c216c0a93844ce2d82ac1c9ecc9c7f24e05")
                .input(inputDto)
                .build();

        try {
            RiffusionResponseDto responseDto = client.post()
                    .uri(API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestDto)
                    .retrieve()
                    .bodyToMono(RiffusionResponseDto.class)
                    .block();

            if (responseDto.getUrls() != null) {
                String getUrl = responseDto.getUrls().getGet();
                try {
                    AudioResponseDto audioResponseDto = client.get()
                            .uri(getUrl)
                            .retrieve()
                            .bodyToMono(AudioResponseDto.class)
                            .block();

                    while (audioResponseDto.getStatus().equals("processing") && audioResponseDto.getCompleted_at() == null) {
                        Thread.sleep(1000);
                        audioResponseDto = client.get()
                                .uri(getUrl)
                                .retrieve()
                                .bodyToMono(AudioResponseDto.class)
                                .block();
                    }
                    if (audioResponseDto != null && audioResponseDto.getOutput() != null) {
                        return audioResponseDto.getOutput().getAudio();
                    } else {
                        throw new RuntimeException("Failed to retrieve audio response");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            } else {
                throw new RuntimeException("Invalid response received.");
            }
        } catch (WebClientResponseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getAudioWithRetry(String prompt, int maxRetries, int emotion) {
        for (int retry = 0; retry < maxRetries; retry++) {
            try {
                String audio = getAudio(prompt, emotion);
                return audio;
            } catch (Exception e) {

                log.error("Error while fetching audio from Riffusion API: {}", e.getMessage());

                if (retry < maxRetries - 1) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        throw new RuntimeException("Failed to retrieve audio after " + maxRetries + " retries");
    }


}
