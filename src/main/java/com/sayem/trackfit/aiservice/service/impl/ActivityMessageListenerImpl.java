package com.sayem.trackfit.aiservice.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.entity.Recommendation;
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
	

	@Override
	@RabbitListener(queues = "activity.queue")
	public void processActivity(Activity activity) {
		log.info("Rceived activity from the queue: {}", activity.getId());
		log.info("Invoking Genrate Recommendations");
		Recommendation recommendation = activityAiService.generateRecommendation(activity);
		
		log.info("Saving into Database");
		if(recommendation != null) {
			recommendationRepository.save(recommendation);			
		}
	}

}
