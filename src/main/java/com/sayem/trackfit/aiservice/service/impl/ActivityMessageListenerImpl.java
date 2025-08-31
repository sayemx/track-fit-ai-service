package com.sayem.trackfit.aiservice.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.dto.FinishedExcercise;
import com.sayem.trackfit.aiservice.entity.DetailedRecommendation;
import com.sayem.trackfit.aiservice.repository.DetailedRecommendationRepository;
import com.sayem.trackfit.aiservice.repository.RecommendationRepository;
import com.sayem.trackfit.aiservice.service.IActivityAiService;
import com.sayem.trackfit.aiservice.service.IActivityMessageListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListenerImpl implements IActivityMessageListener{
	
	private final IActivityAiService activityAiService;
	
	private final RecommendationRepository recommendationRepository;
	
	@Autowired
    private DetailedRecommendationRepository detailedRecommendationRepository;
	
	@Override
//	@RabbitListener(queues = "activity.queue")
	public void processActivity(Activity activity) {
		
		try {
		
			log.info("Rceived activity from the queue: {}", activity.getId());
			log.info("Invoking Genrate Recommendations");
	//		Recommendation recommendation = activityAiService.generateRecommendation(activity);
	//		if(recommendation != null) {
	//			recommendationRepository.save(recommendation);			
	//		}
			
			DetailedRecommendation detailedRecommendation = activityAiService.generateDetailedRecommendation(activity);
			
			log.info("Saving into Database");
			
			if (detailedRecommendation == null) {
	            log.error("Failed to create detailed recommendation entity for activity: {}", activity.getId());
	            return;
	        }
			
			// Save to database
	        log.info("Saving detailed recommendation to database for activity: {}", activity.getId());
	        DetailedRecommendation savedRecommendation = detailedRecommendationRepository.save(detailedRecommendation);
	        log.info("Successfully saved detailed recommendation with ID: {} for activity: {}", 
	            savedRecommendation.getId(), activity.getId());
        
		}catch (Exception e) {
            log.error("Error processing activity with ID: {} - {}", activity.getId(), e.getMessage(), e);
        }
		
	}

	@Override
	@RabbitListener(queues = "excercise.queue")
	public void processExcercise(FinishedExcercise finishedExcercise) {
		
		try {
			
			log.info("Rceived Excercise from the queue: {}", finishedExcercise.getName());
			log.info("Invoking Genrate Recommendations");
			
			DetailedRecommendation detailedRecommendation = activityAiService.generateDetailedRecommendation(finishedExcercise);
			
			
			if (detailedRecommendation == null) {
	            log.error("Failed to create detailed recommendation entity for activity: {}", finishedExcercise.getName());
	            return;
	        }
			
			log.info("Saving into Database");
			DetailedRecommendation savedRecommendation = detailedRecommendationRepository.save(detailedRecommendation);
	        log.info("Successfully saved detailed recommendation with ID: {} for activity: {}", 
	            savedRecommendation.getId(), finishedExcercise.getId());
        
		}catch (Exception e) {
            log.error("Error processing activity with ID: {} - {}", finishedExcercise.getId(), e.getMessage(), e);
        }
		
	}

}
