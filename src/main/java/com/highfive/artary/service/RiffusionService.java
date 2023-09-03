package com.highfive.artary.service;

import com.highfive.artary.dto.riffusion.AudioResponseDto;
import com.highfive.artary.dto.riffusion.RiffusionInputDto;
import com.highfive.artary.dto.riffusion.RiffusionRequestDto;
import com.highfive.artary.dto.riffusion.RiffusionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiffusionService {

    private static final String API_URL = "https://api.replicate.com/v1/predictions";

//    @Value("${REPLICATE_API_TOKEN}")
//    private String token;
    private String token = "r8_SMYE9S1Dgv3Hmby58M2zH3wwnf0qlnD2OOGuo";

    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
            .codecs(configure -> configure.defaultCodecs().maxInMemorySize(-1))
            .build();

    private WebClient client = WebClient.builder()
            .exchangeStrategies(exchangeStrategies)
            .defaultHeader("Authorization", "Token " + token)
            .build();

    public String getAudio(String prompt) {
        RiffusionInputDto inputDto = RiffusionInputDto.builder()
                .prompt_a(prompt)
                .denoising(0.75)
                .prompt_b("funky synth solo")
                .alpha(0.5)
                .num_inference_steps(50)
                .seed_image_id("vibes")
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
}
