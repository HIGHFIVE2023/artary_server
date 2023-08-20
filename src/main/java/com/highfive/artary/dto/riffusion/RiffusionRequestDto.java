package com.highfive.artary.dto.riffusion;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RiffusionRequestDto {
    private String version;
    private RiffusionInputDto input;
}
