package com.highfive.artary.dto.riffusion;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RiffusionInputDto {
    private String prompt_a;
    private String prompt_b;
    private double denoising;
    private double alpha;
    private int num_inference_steps;
    private String seed_image_id;
}
