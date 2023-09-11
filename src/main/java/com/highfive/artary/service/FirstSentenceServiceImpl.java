package com.highfive.artary.service;

import com.highfive.artary.domain.FirstSentence;
import com.highfive.artary.dto.textGeneration.FirstSentenceRequestDto;
import com.highfive.artary.dto.textGeneration.TextGenerationRequestDto;
import com.highfive.artary.dto.textGeneration.TextGenerationResponseDto;
import com.highfive.artary.repository.FirstSentenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.Collections;


@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class FirstSentenceServiceImpl implements FirstSentenceService {

    private final FirstSentenceRepository firstSentenceRepository;

    private static final String API_URL = "https://clovastudio.apigw.ntruss.com/testapp/v1/tasks/4cdwq7fg/completions/LK-D2";

    @Value("${clova.studio.key}")
    private String apiKey;

    @Value("${clova.studio.api.gateway.key}")
    private String apiGatewayKey;


    private WebClient webClient;

    @PostConstruct
    public void initializeWebClient() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        webClient = WebClient.builder()
                .baseUrl(API_URL)
                .exchangeStrategies(exchangeStrategies)
                .defaultHeader("X-NCP-CLOVASTUDIO-API-KEY", apiKey)
                .defaultHeader("X-NCP-APIGW-API-KEY", apiGatewayKey)
                .build();
    }

    @Override
    public FirstSentence save(FirstSentenceRequestDto requestDto) {
        FirstSentence firstSentence = FirstSentence.builder()
                .inputWords(requestDto.getInputWords())
                .build();

        return firstSentenceRepository.save(firstSentence);
    }


    @Override
    public String generateText(Long userInput) {

        FirstSentence firstSentence = firstSentenceRepository.getById(userInput);

        String text = firstSentence.getInputWords();

        TextGenerationRequestDto requestBody = new TextGenerationRequestDto();
        requestBody.setIncludeAiFilters(true);
        requestBody.setIncludeTokens(false);
        requestBody.setMaxTokens(300);
        requestBody.setRepeatPenalty(5.0);
        requestBody.setRestart("");
        requestBody.setStart("");
        requestBody.setStopBefore(Collections.singletonList("<|empty list|>"));
        requestBody.setTemperature(0.85);
        requestBody.setText(text);
        requestBody.setTopK(4);
        requestBody.setTopP(0.8);

        TextGenerationResponseDto.Result result = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TextGenerationResponseDto.class)
                .block()
                .getResult();

        String generatedSentence = result.getText();

        firstSentence.setGeneratedSentence(generatedSentence);

        firstSentenceRepository.save(firstSentence);

        if (generatedSentence != null) {
            log.info(generatedSentence);
            firstSentence.setGeneratedSentence(generatedSentence);
            firstSentenceRepository.save(firstSentence);
            return generatedSentence;
        } else {
            log.error("Failed to generate text");
            return null;
        }
    }
}