package com.sayem.trackfit.aiservice.service;

import java.util.List;

import com.sayem.trackfit.aiservice.entity.DetailedRecommendation;
import com.sayem.trackfit.aiservice.entity.Recommendation;

public interface IRecommendationService {

	List<Recommendation> getUserRecommendation(String userId);

	Recommendation getActivityRecommendation(String activityId);

	List<DetailedRecommendation> getExcerciseUserRecommendation(String userId);

}
