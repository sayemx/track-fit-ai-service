package com.sayem.trackfit.aiservice.service.impl;

import java.util.Map;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.entity.Recommendation;
import com.sayem.trackfit.aiservice.mapper.RecommendationMapper;
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


	
}
