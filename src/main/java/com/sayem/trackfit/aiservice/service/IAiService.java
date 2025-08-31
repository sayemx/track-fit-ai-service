package com.sayem.trackfit.aiservice.service;

import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.dto.FinishedExcercise;

public interface IAiService {
	
	String getAnswer(Activity activity);
	
	String getAnswer(FinishedExcercise finishedExcercise);

}
