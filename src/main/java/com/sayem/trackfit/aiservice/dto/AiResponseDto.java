package com.sayem.trackfit.aiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class AiResponseDto {
    private String summary;
    private AnalysisDto analysis;
    private RecommendationsDto recommendations;
    private List<String> improvements;
    private List<String> suggestions;
    private PredictionsDto predictions;
    private String proTip;
    
    @Data
    public static class AnalysisDto {
        @JsonProperty("effortLevel")
        private String effortLevel;
        private String consistency;
        @JsonProperty("calorieEfficiency")
        private String calorieEfficiency;
        @JsonProperty("keyObservations")
        private List<String> keyObservations;
    }
    
    @Data
    public static class RecommendationsDto {
        @JsonProperty("fitnessAdvice")
        private List<String> fitnessAdvice;
        @JsonProperty("nutritionAdvice")
        private List<String> nutritionAdvice;
        @JsonProperty("recoveryTips")
        private List<String> recoveryTips;
    }
    
    @Data
    public static class PredictionsDto {
        private String trend;
        @JsonProperty("nextBestActivity")
        private String nextBestActivity;
    }
}