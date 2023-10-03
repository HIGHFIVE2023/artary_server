package com.highfive.artary.dto.stablediffusion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StableDiffusionRequestDto {

    private String key;
    private String model_id;
    private String prompt;
    private String negative_prompt;
    private int width;
    private int height;
    private int samples;
    private int num_inference_steps;
    private String seed;
    private double guidance_scale;
    private String safety_checker;
    private String webhook;
    private String track_id;
    private String scheduler;
}
