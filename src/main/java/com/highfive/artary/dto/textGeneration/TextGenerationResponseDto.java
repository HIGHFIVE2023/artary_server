package com.highfive.artary.dto.textGeneration;

import lombok.Data;

import java.util.List;

@Data
public class TextGenerationResponseDto {
    private List<AIFilter> aiFilters;
    private int inputLength;
    private List<String> inputTokens;
    private int outputLength;
    private List<String> outputTokens;
    private String stopReason;
    private String text;
    private float probs;
}
