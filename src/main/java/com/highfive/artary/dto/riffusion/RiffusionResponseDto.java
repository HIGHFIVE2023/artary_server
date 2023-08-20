package com.highfive.artary.dto.riffusion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiffusionResponseDto {
    private String id;
    private RiffusionRequestDto requestDto;
    private String logs;
    private String error;
    private String status;
    private String created_at;
    private Urls urls;

    @Getter
    @Setter
    public static class Urls {
        private String cancel;
        private String get;
    }
}
