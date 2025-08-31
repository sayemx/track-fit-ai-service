package com.sayem.trackfit.aiservice.service;

import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.dto.FinishedExcercise;

public interface IActivityMessageListener {

	void processActivity(Activity activity);
	
	void processExcercise(FinishedExcercise finishedExcercise);

}
