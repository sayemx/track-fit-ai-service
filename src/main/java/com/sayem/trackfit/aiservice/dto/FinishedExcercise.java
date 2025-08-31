package com.sayem.trackfit.aiservice.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FinishedExcercise {
	
	private String id;
	
	private String userId;
	
	private ActivityType name;
	
	private int duration;
	
	private int calories;
	
	private LocalDateTime date;
	
	private String state;
}
