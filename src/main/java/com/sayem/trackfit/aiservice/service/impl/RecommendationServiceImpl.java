package com.sayem.trackfit.aiservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sayem.trackfit.aiservice.entity.DetailedRecommendation;
import com.sayem.trackfit.aiservice.entity.Recommendation;
import com.sayem.trackfit.aiservice.repository.DetailedRecommendationRepository;
import com.sayem.trackfit.aiservice.repository.RecommendationRepository;
import com.sayem.trackfit.aiservice.service.IRecommendationService;
import com.sayem.trackfit.aiservice.service.exception.RecommendationNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements IRecommendationService {
	
	private final RecommendationRepository recommendationRepository;
	private final DetailedRecommendationRepository detailedRecommendationRepository;

	@Override
	public List<Recommendation> getUserRecommendation(String userId) {
		
		List<Recommendation> recommendations = recommendationRepository.findByUserId(userId);
		
		return recommendations;
	}

	@Override
	public Recommendation getActivityRecommendation(String activityId) {
		
		Recommendation rcommendation = recommendationRepository.findByActivityId(activityId)
			.orElseThrow(() -> new RecommendationNotFoundException(activityId));
		
		return rcommendation;
	}

	@Override
	public List<DetailedRecommendation> getExcerciseUserRecommendation(String userId) {
		
		List<DetailedRecommendation> recommendations = detailedRecommendationRepository.findByUserIdOrderByCreatedAtDesc(userId);
		
		return recommendations;
	}

}
