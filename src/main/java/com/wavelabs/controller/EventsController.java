package com.wavelabs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class EventsController {
	@Autowired
	private Environment env;
	
	@CrossOrigin
	@RequestMapping("/events")
	public @ResponseBody String getEvents(){
		RestTemplate restTemplate = new RestTemplate();
		final String resourceURL = env.getProperty("eventUrl");
		ResponseEntity<String> response = restTemplate.getForEntity(resourceURL, String.class);
		
		return response.getBody();
	}

}
