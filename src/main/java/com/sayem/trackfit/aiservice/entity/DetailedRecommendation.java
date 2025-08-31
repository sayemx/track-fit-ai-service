package com.sayem.trackfit.aiservice.entity;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "detail_recommendations")
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
    private int duration;
    private int caloryBurned;
    
    private String summary;
    
    private String effortLevel;
    private String consistency;
    private String calorieEfficiency;
    private List<String> keyObservations;
    
    private List<String> fitnessAdvice;
    private List<String> nutritionAdvice;
    private List<String> recoveryTips;
    
    private List<String> improvements;
    private List<String> suggestions;
    
    private String trend;
    private String nextBestActivity;
    
    private List<String> safety;
    private String proTip;
    
    @CreatedDate
    private LocalDateTime createdAt;
    
    @JsonIgnore
    private String rawAiResponse;
}