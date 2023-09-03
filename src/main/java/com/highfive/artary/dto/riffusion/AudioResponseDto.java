package com.highfive.artary.dto.riffusion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AudioResponseDto {
    private String id;
    private String version;
    private Input input;
    private String logs;
    private Output output;
    private String error;
    private String status;
    private String created_at;
    private String started_at;
    private String completed_at;
    private Metrics metrics;
    private Urls urls;

    @Getter
    @Setter
    public static class Input {
        private double alpha;
        private double denoising;
        private int num_inference_steps;
        private String prompt_a;
        private String prompt_b;
        private String seed_image_id;
    }

    @Getter
    @Setter
    public static class Output {
        private String audio;
        private String spectrogram;
    }

    @Getter
    @Setter
    public static class Metrics {
        private double predict_time;
    }

    @Getter
    @Setter
    public static class Urls {
        private String cancel;
        private String get;
    }
}
