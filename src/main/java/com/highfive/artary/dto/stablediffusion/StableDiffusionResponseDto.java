package com.highfive.artary.dto.stablediffusion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StableDiffusionResponseDto {

    private String status;
    private double generationTime;
    private int id;
    private List<String> output;
    private Map<String, Object> meta;

}
