package com.highfive.artary.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highfive.artary.domain.Summary;
import com.highfive.artary.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class PapagoTranslationServiceImpl implements PapagoTranslationService {

    private final SummaryRepository summaryRepository;

    @Value("${papago.client.id}")
    private String clientId;
    @Value("${papago.client.secret}")
    private String clientSecret;

    @Override
    public String translateSummary(Long diary_id) {
        Summary summary = summaryRepository.findByDiaryId(diary_id);
        String koSummary = summary.getKoSummary();

        try {
            String text = URLEncoder.encode(koSummary, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
            // post request
            String postParams = "source=ko&target=en&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 오류 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            String getTranslation = parseTranslationResponse(response.toString());
            summary.setEngSummary(getTranslation);
            summaryRepository.save(summary);

            return getTranslation;

        } catch (Exception e) {
            System.out.println(e);
            return e.getMessage();
        }
    }

    private String parseTranslationResponse(String response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode translatedText = jsonNode.get("message").get("result").get("translatedText");

        return translatedText.asText();
    }
}
