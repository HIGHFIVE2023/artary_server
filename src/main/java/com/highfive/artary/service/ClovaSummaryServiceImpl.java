package com.highfive.artary.service;

import com.highfive.artary.domain.Summary;
import com.highfive.artary.domain.TemporaryDiary;
import com.highfive.artary.dto.ClovaSummary.ClovaSummaryRequestDto;
import com.highfive.artary.dto.ClovaSummary.ClovaSummaryResponseDto;
import com.highfive.artary.dto.ClovaSummary.DocumentObject;
import com.highfive.artary.dto.ClovaSummary.OptionObject;
import com.highfive.artary.repository.SummaryRepository;
import com.highfive.artary.repository.TemporaryDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
@RequiredArgsConstructor
public class ClovaSummaryServiceImpl implements ClovaSummaryService{

    private final TemporaryDiaryRepository diaryRepository;
    private final SummaryRepository summaryRepository;

    private static final String API_URL = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";
    @Value("${X-NCP-APIGW-API-KEY-ID}")
    private String clientId;
    @Value("${X-NCP-APIGW-API-KEY}")
    private String clientKey;

    private WebClient client = WebClient.builder()
            .baseUrl(API_URL)
            .build();

    @Override
    public String summarizeDiary(Long diary_id) {
        TemporaryDiary diary = diaryRepository.findById(diary_id).orElseThrow(() ->
                new IllegalArgumentException("해당 일기가 존재하지 않습니다."));

        Summary summary = Summary.builder()
                .diary(diary)
                .build();

        DocumentObject document = new DocumentObject();
        document.setContent(diary.getContent());
        document.setTitle(diary.getTitle());

        OptionObject option = new OptionObject();
        option.setLanguage("ko");
        option.setModel("general");
        option.setTone(0);
        option.setSummaryCount(1);

        ClovaSummaryRequestDto requestDto = ClovaSummaryRequestDto.builder()
                .document(document)
                .option(option)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientKey);

        String getSummary = client.post()
                .uri(API_URL)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(requestDto))
                .retrieve()
                .bodyToMono(ClovaSummaryResponseDto.class)
                .block().getSummary();

        summary.setKoSummary(getSummary);
        summaryRepository.save(summary);

        diary.setSummary(summary);
        diaryRepository.save(diary);

        return getSummary;
    }
}
