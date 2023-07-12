package com.highfive.artary.dto.textGeneration;

import lombok.Data;

import java.util.List;

@Data
public class TextGenerationRequestDto {
    private int topK;
    private boolean includeProbs;
    private boolean includeTokens;
    private String restart;
    private boolean includeAiFilters;
    private int maxTokens;
    private double temperature;
    private String start;
    private List<String> stopBefore;
    private String text;
    private double repeatPenalty;
    private double topP;

}
