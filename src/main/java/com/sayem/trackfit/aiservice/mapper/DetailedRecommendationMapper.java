package com.sayem.trackfit.aiservice.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.dto.AiResponseDto;
import com.sayem.trackfit.aiservice.entity.DetailedRecommendation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DetailedRecommendationMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DetailedRecommendation mapAiResponseToDetailedRecommendation(String aiResponse, Activity activity) {
        try {
            // Parse AI response to DTO
            AiResponseDto aiResponseDto = objectMapper.readValue(aiResponse, AiResponseDto.class);
            
            return DetailedRecommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .summary(aiResponseDto.getSummary())
                    
                    // Analysis section
                    .effortLevel(aiResponseDto.getAnalysis() != null ? aiResponseDto.getAnalysis().getEffortLevel() : null)
                    .consistency(aiResponseDto.getAnalysis() != null ? aiResponseDto.getAnalysis().getConsistency() : null)
                    .calorieEfficiency(aiResponseDto.getAnalysis() != null ? aiResponseDto.getAnalysis().getCalorieEfficiency() : null)
                    .keyObservations(aiResponseDto.getAnalysis() != null ? 
                        safeList(aiResponseDto.getAnalysis().getKeyObservations()) : new ArrayList<>())
                    
                    // Recommendations section
                    .fitnessAdvice(aiResponseDto.getRecommendations() != null ? 
                        safeList(aiResponseDto.getRecommendations().getFitnessAdvice()) : new ArrayList<>())
                    .nutritionAdvice(aiResponseDto.getRecommendations() != null ? 
                        safeList(aiResponseDto.getRecommendations().getNutritionAdvice()) : new ArrayList<>())
                    .recoveryTips(aiResponseDto.getRecommendations() != null ? 
                        safeList(aiResponseDto.getRecommendations().getRecoveryTips()) : new ArrayList<>())
                    
                    // Other fields
                    .improvements(safeList(aiResponseDto.getImprovements()))
                    .suggestions(safeList(aiResponseDto.getSuggestions()))
                    
                    // Predictions
                    .trend(aiResponseDto.getPredictions() != null ? aiResponseDto.getPredictions().getTrend() : null)
                    .nextBestActivity(aiResponseDto.getPredictions() != null ? 
                        aiResponseDto.getPredictions().getNextBestActivity() : null)
                    
                    // Safety (empty for now, can be added later)
                    .safety(new ArrayList<>())
                    
                    // Metadata
                    .createdAt(LocalDateTime.now())
                    .rawAiResponse(aiResponse)
                    .build();
                    
        } catch (Exception e) {
            log.error("Error parsing AI response JSON: {}", e.getMessage(), e);
            log.debug("Raw AI response: {}", aiResponse);
            
            // Return a fallback recommendation if JSON parsing fails
            return createFallbackRecommendation(aiResponse, activity, e.getMessage());
        }
    }
    
    private DetailedRecommendation createFallbackRecommendation(String aiResponse, Activity activity, String errorMessage) {
        return DetailedRecommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .summary("AI processing completed but response could not be parsed fully")
                .effortLevel("unknown")
                .consistency("unknown")
                .calorieEfficiency("N/A")
                .keyObservations(List.of("Unable to parse detailed analysis due to: " + errorMessage))
                .fitnessAdvice(List.of("Continue with your regular exercise routine"))
                .nutritionAdvice(List.of("Maintain a balanced diet and stay hydrated"))
                .recoveryTips(List.of("Ensure adequate rest between workout sessions"))
                .improvements(List.of("Data parsing needs improvement"))
                .suggestions(List.of("Maintain consistency in your fitness activities"))
                .trend("Data unavailable due to parsing error")
                .nextBestActivity("Continue current activity type")
                .safety(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .rawAiResponse(aiResponse)
                .build();
    }
    
    private List<String> safeList(List<String> list) {
        return list != null ? list : new ArrayList<>();
    }
}