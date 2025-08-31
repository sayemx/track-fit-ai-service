package com.sayem.trackfit.aiservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.dto.FinishedExcercise;
import com.sayem.trackfit.aiservice.entity.DetailedRecommendation;
import com.sayem.trackfit.aiservice.entity.Recommendation;
import com.sayem.trackfit.aiservice.mapper.DetailedRecommendationMapper;
import com.sayem.trackfit.aiservice.service.IActivityAiService;
import com.sayem.trackfit.aiservice.service.IAiService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAiServiceImpl implements IActivityAiService{
	
	private final IAiService aiService;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
    private DetailedRecommendationMapper detailedRecommendationMapper;
	
	
	@Override
	public Recommendation generateRecommendation(Activity activity) {
		
		String aiResponse = aiService.getAnswer(activity);
		log.info("Ai Response: {}", aiResponse);
		
//		String jsonAiReponse = processAiReponse(activity, aiResponse);
//		
//		Recommendation recommendation = RecommendationMapper.mapToEntity(jsonAiReponse, activity.getId(), activity.getUserId(), activity.getType());
//		
//		System.out.println(recommendation);
//		
//		return recommendation;
		
		return null;
	}


	
	private String processAiReponse(Activity activity, String aiResponse) {
		
		mapper = new ObjectMapper();
		try {
			JsonNode rootNode = mapper.readTree(aiResponse);
			
			JsonNode textNode = rootNode.path("candidates")
					.get(0)
					.path("content")
					.path("parts")
					.get(0)
					.path("text");
			
			String jsonContent = textNode.asText()
					.replaceAll("```json\\n", "")
					.replaceAll("\\n```", "")
					.trim();
			
			log.info("Parsed Ai Response to JSON: {}", jsonContent);
			
			return jsonContent;
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}



	@Override
	public DetailedRecommendation generateDetailedRecommendation(Activity activity) {
		
		String aiResponse = aiService.getAnswer(activity);
		log.info("Ai Response: {}", aiResponse);
		
		if (aiResponse == null || aiResponse.trim().isEmpty()) {
            log.error("AI service returned null or empty response for activity: {}", activity.getId());
            return null;
        }
		
		// Map JSON response to DetailedRecommendation entity
        DetailedRecommendation recommendation = detailedRecommendationMapper
            .mapAiResponseToDetailedRecommendation(aiResponse, activity);
		
		return recommendation;
	}



	@Override
	public DetailedRecommendation generateDetailedRecommendation(FinishedExcercise finishedExcercise) {
		
		String aiResponse = aiService.getAnswer(finishedExcercise);
		log.info("Ai Response: {}", aiResponse);
		
		if (aiResponse == null || aiResponse.trim().isEmpty()) {
            log.error("AI service returned null or empty response for finishedExcercise: {}", finishedExcercise.getId());
            return null;
        }
		
		// Map JSON response to DetailedRecommendation entity
        DetailedRecommendation recommendation = detailedRecommendationMapper
            .mapAiResponseToDetailedRecommendation(aiResponse, finishedExcercise);
		
		return recommendation;
	}


	
}
