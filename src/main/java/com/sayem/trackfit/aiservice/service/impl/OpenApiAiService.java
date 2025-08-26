package com.sayem.trackfit.aiservice.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.service.IAiService;

@Service
@Primary
public class OpenApiAiService implements IAiService {

    private final ChatClient chatClient;

    public OpenApiAiService(ChatClient.Builder builder) {
        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        this.chatClient = builder.defaultAdvisors(
                MessageChatMemoryAdvisor.builder(chatMemory).build()
        ).build();
    }

    @Override
    public String getAnswer(Activity activity) {
        Prompt prompt = generatePropmtForActivityForForOpenApi(activity);
        return chatClient.prompt(prompt)
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

	private Prompt generatePropmtForActivityForForOpenApi(Activity activity) {
	        
	        // Build the prompt string directly without StringTemplate placeholders
	        String additionalMetricsJson = "{}";
	        try {
	            if (activity.getAdditionalMetrics() != null) {
	                additionalMetricsJson = new ObjectMapper().writeValueAsString(activity.getAdditionalMetrics());
	            }
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	        }
	
	        String promptText = "You are an AI fitness assistant that analyzes user activity data.\n"
	            + "\n"
	            + "### Input Data (Activity DTO)\n"
	            + "{\n"
	            + "  \"id\": \"" + activity.getId() + "\",\n"
	            + "  \"userId\": \"" + activity.getUserId() + "\",\n"
	            + "  \"duration\": " + activity.getDuration() + ",       // in minutes\n"
	            + "  \"caloriesBurned\": " + activity.getCaloriesBurned() + ", // in kcal\n"
	            + "  \"additionalMetrics\": " + additionalMetricsJson + ", \n"
	            + "  \"activityType\": \"" + activity.getType() + "\"\n"
	            + "}\n"
	            + "\n"
	            + "### Task\n"
	            + "1. Analyze the given activity data.\n"
	            + "2. Provide insights on the user's performance.\n"
	            + "3. Suggest personalized fitness advice.\n"
	            + "4. Predict possible health or fitness trends based on the activity.\n"
	            + "5. Keep the response concise and structured.\n"
	            + "\n"
	            + "### Output Format\n"
	            + "Respond ONLY in valid JSON with the following structure:\n"
	            + "{\n"
	            + "  \"summary\": \"Brief summary of the activity\",\n"
	            + "  \"analysis\": {\n"
	            + "    \"effortLevel\": \"low | medium | high\",\n"
	            + "    \"consistency\": \"poor | average | good | excellent\",\n"
	            + "    \"calorieEfficiency\": \"calories burned per minute\",\n"
	            + "    \"keyObservations\": [\"list of insights\"]\n"
	            + "  },\n"
	            + "  \"recommendations\": {\n"
	            + "    \"fitnessAdvice\": [\"list of actionable advice\"],\n"
	            + "    \"nutritionAdvice\": [\"list of nutrition suggestions\"],\n"
	            + "    \"recoveryTips\": [\"list of recovery suggestions\"]\n"
	            + "  },\n"
	            + "  \"improvements\": [\"list of areas to improve\"],\n"
	            + "  \"suggestions\": [\"list of general advice (fitness, nutrition, mindset, etc.)\"],\n"
	            + "  \"predictions\": {\n"
	            + "    \"trend\": \"expected improvement or decline\",\n"
	            + "    \"nextBestActivity\": \"recommended type of workout\"\n"
	            + "  }\n"
	            + "}";
	
	        return new Prompt(promptText);
	   }

}
