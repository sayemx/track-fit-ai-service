package com.sayem.trackfit.aiservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sayem.trackfit.aiservice.entity.DetailedRecommendation;
import com.sayem.trackfit.aiservice.entity.Recommendation;
import com.sayem.trackfit.aiservice.service.IRecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
	
	private final IRecommendationService recommendationService;
	
	
	@GetMapping("/activity/user/{userId}")
	public ResponseEntity<List<Recommendation>> getActivityUserRecommendation(@PathVariable String userId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(recommendationService.getUserRecommendation(userId));
	}
	
	@GetMapping("/activity/{activityId}")
	public ResponseEntity<Recommendation> getActivityRecommendation(@PathVariable String activityId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(recommendationService.getActivityRecommendation(activityId));
	}
	
	@GetMapping("/excercise/user/{userId}")
	public ResponseEntity<List<DetailedRecommendation>> getUserRecommendation(@PathVariable String userId) {
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(recommendationService.getExcerciseUserRecommendation(userId));
	}

}
