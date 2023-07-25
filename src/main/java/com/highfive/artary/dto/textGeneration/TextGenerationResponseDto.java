package com.highfive.artary.dto.textGeneration;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TextGenerationResponseDto {
    private Status status;
    private Result result;

    @Data
    public static class Status {
        private String code;
        private String message;
        private Object messageVariables;
    }

    @Data
    public static class Result {
        private String text;
        private String stopReason;
        private int inputLength;
        private int outputLength;
        private List<String> inputTokens;
        private List<String> outputTokens;
        private boolean ok;
        private List<Double> probs;
        private List<AiFilter> aiFilter;
    }

    @Data
    public static class AiFilter {
        private String groupName;
        private String name;
        private int score;
    }
}
