package com.sayem.trackfit.aiservice.service;

import com.sayem.trackfit.aiservice.dto.Activity;

public interface IActivityMessageListener {

	void processActivity(Activity activity);

}
