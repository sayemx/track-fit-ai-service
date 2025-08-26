package com.sayem.trackfit.aiservice.service;

import com.sayem.trackfit.aiservice.dto.Activity;
import com.sayem.trackfit.aiservice.entity.Recommendation;

public interface IActivityAiService {
	
	Recommendation generateRecommendation(Activity activity);
	
}
