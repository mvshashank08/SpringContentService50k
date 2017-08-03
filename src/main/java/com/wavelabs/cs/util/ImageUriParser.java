package com.wavelabs.cs.util;

import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ImageUriParser {
	public static String getJsonString(String jsonStr){
		
		JSONParser parser = new JSONParser();
		JSONObject obj;
		JSONArray items;
		try{
			obj = (JSONObject) parser.parse(jsonStr);
			items = (JSONArray)obj.get("items");
			for(int i = 0; i < items.size(); i++){
				JSONObject obj2 = (JSONObject)items.get(i);
				JSONObject image = parseImageUri((JSONObject)obj2.get("image"));
				obj2.put("image", image);
				items.set(i, obj2);
			}
			obj.put("items", items);
			StringWriter out = new StringWriter();
		    obj.writeJSONString(out);
		    jsonStr = out.toString();
			
		}catch(Exception e){}
		
		return jsonStr;
	}
	
	public static JSONObject parseImageUri(JSONObject image) throws UnknownHostException{
		
		String path = (String)image.get("path");
		InetAddress ip = InetAddress.getLocalHost();
		path = "http://"+ip.getHostAddress()+":8080/site/binaries"+path;
		//System.out.println(path);
		image.put("path", path);
		return image;
	}
}
