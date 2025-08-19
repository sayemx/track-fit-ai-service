package com.sayem.trackfit.aiservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;

@Document(collection = "recommendations")
@Data
@Builder
public class Recommendation {
	
	@Id
	private String id;
	
	private String activityId;
	
	private String userId;
	
	private String activityType;
	
	private String recommendations;
	
	private List<String> improvements;
	
	private List<String> suggestions;
	
	private List<String> safety;
	
	@CreatedDate
	private LocalDateTime cratedAt;

}
