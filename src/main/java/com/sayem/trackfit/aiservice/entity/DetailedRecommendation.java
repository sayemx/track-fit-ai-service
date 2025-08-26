package com.sayem.trackfit.aiservice.entity;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "recommendations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailedRecommendation {
    
    @Id
    private String id;
    
    private String activityId;
    private String userId;
    private String activityType;
    
    // AI Response fields
    private String summary;
    
    // Analysis section
    private String effortLevel;
    private String consistency;
    private String calorieEfficiency;
    private List<String> keyObservations;
    
    // Recommendations section
    private List<String> fitnessAdvice;
    private List<String> nutritionAdvice;
    private List<String> recoveryTips;
    
    // Other recommendations
    private List<String> improvements;
    private List<String> suggestions;
    
    // Predictions
    private String trend;
    private String nextBestActivity;
    
    // Safety (for future use)
    private List<String> safety;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    // Store raw AI response for debugging/backup
    private String rawAiResponse;
}