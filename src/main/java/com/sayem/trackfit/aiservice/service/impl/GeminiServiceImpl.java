package com.sayem.trackfit.aiservice.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.service.IAiService;

@Service
//@Primary
public class GeminiServiceImpl implements IAiService {
	
	private final WebClient webClient;
	
	@Value("${gemini.api.url}")
	private String geminiApiUrl;
	
	@Value("${gemini.api.key}")
	private String geminiApiKey;
	
	public GeminiServiceImpl(WebClient.Builder webclientBuilder) {
		this.webClient = webclientBuilder.build();
	}
	
	@Override
	public String getAnswer(Activity activity) {
		
		String prompt = generatePropmtForActivityForForGemini(activity);
		
		Map<String, Object> requestBody = Map.of(
					"contents", new Object[] {
												Map.of("parts", new Object[] {
													Map.of("text", prompt)
												})
					});
		
		
		String response = webClient.post()
				.uri(geminiApiUrl + "?key=" + geminiApiKey)
				.header("Content-Type", "application/json")
				.bodyValue(requestBody)
				.retrieve()
				.bodyToMono(String.class)
				.block();
		
		return response;
	}
	
	private String generatePropmtForActivityForForGemini(Activity activity) {

        return String.format("""
                You are an AI fitness assistant that analyzes user activity data.

				### Input Data (Activity DTO)
				{
				  "id": "{{%s}}",
				  "userId": "{{%s}}",
				  "duration": {{%d}},       // in minutes
				  "caloriesBurned": {{%d}}, // in kcal
				  "additionalMetrics": {{%s}}, 
				  "activityType": {{%s}}
				}
				
				### Task
				1. Analyze the given activity data.
				2. Provide insights on the user’s performance.
				3. Suggest personalized fitness advice.
				4. Predict possible health or fitness trends based on the activity.
				5. Keep the response concise and structured.
				
				### Output Format
				Respond ONLY in valid JSON with the following structure:
				
				{
				  "summary": "Brief summary of the activity",
				  "analysis": {
				    "effortLevel": "low | medium | high",
				    "consistency": "poor | average | good | excellent",
				    "calorieEfficiency": "calories burned per minute",
				    "keyObservations": ["list of insights"]
				  },
				  "recommendations": {
				    "fitnessAdvice": ["list of actionable advice"],
				    "nutritionAdvice": ["list of nutrition suggestions"],
				    "recoveryTips": ["list of recovery suggestions"]
				  },
				  "improvements": ["list of areas to improve"],
				  "suggestions": ["list of general advice (fitness, nutrition, mindset, etc.)"],
				  "predictions": {
				    "trend": "expected improvement or decline",
				    "nextBestActivity": "recommended type of workout"
				  }
				}

                """, activity.getId(),
                	 activity.getUserId(),
                	 activity.getDuration(),
                	 activity.getCaloriesBurned(),
                	 activity.getAdditionalMetrics(),
                	 activity.getType()
                	 );

	}
}
