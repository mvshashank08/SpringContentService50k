package com.wavelabs.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.wavelabs.cs.util.ImageUriParser;

import io.nbos.capi.api.v0.models.RestMessage;

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
		
		String jsonStr = ImageUriParser.getJsonString(response.getBody());
		
		return jsonStr;
	}
	
	@CrossOrigin
	@RequestMapping("/event")
	public @ResponseBody ResponseEntity getDealById(@RequestParam String eventId){
		RestTemplate restTemplate = new RestTemplate();
		final String resourceURL = env.getProperty("eventUrl");
		
		ResponseEntity<String> response = restTemplate.getForEntity(resourceURL, String.class);
		RestMessage restMessage = new RestMessage();
		
		String jsonStr = response.getBody();
		JSONParser parser = new JSONParser();
		JSONObject obj;
		JSONArray items;
		try{
			obj = (JSONObject) parser.parse(jsonStr);
			items = (JSONArray)obj.get("items");
			for(int i = 0; i < items.size(); i++){
				JSONObject obj2 = (JSONObject)items.get(i);
				String id = (String)obj2.get("id");
				if(id.equals(eventId)){
					JSONObject image = ImageUriParser.parseImageUri((JSONObject)obj2.get("image"));
					obj2.put("image", image);
					items.set(i, obj2);
					return ResponseEntity.status(200).body(obj2);
				}
				
			}
			
		}catch(Exception e){}
		restMessage.message = "Event not found";
		restMessage.messageCode = "404";
		return ResponseEntity.status(404).body(restMessage);
	
	}

}
