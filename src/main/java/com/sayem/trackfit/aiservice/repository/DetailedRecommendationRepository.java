package com.sayem.trackfit.aiservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.sayem.trackfit.aiservice.entity.DetailedRecommendation;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetailedRecommendationRepository extends MongoRepository<DetailedRecommendation, String> {
    
    List<DetailedRecommendation> findByUserId(String userId);
    
    List<DetailedRecommendation> findByActivityId(String activityId);
    
    List<DetailedRecommendation> findByUserIdAndActivityType(String userId, String activityType);
    
    Optional<DetailedRecommendation> findByActivityIdAndUserId(String activityId, String userId);
}