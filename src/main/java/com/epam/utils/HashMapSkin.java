package com.epam.utils;

import java.util.HashMap;
import java.util.Map;

public class HashMapSkin {
	
	private Map<String,String> results;

	
	public HashMapSkin() {		
		results = new HashMap<String, String>();
	}
	
	public void add(String key, String value){
		if(results.containsKey(key)){
			String actualValue = results.get(key);
			double existingTestScore = -1;
			double newTestScore = -1;
			try {
				newTestScore= Double.parseDouble(value.substring(0, value.length() - 1));
				existingTestScore = Double.parseDouble(actualValue.substring(0, actualValue.length() - 1));
			} catch (NumberFormatException e){
				if(newTestScore != -1) {
					results.put(key, value);
				}
				
				return;
			}
			if(existingTestScore < newTestScore ){
				results.put(key, value);
			}
		} else {
			results.put(key, value);
		}
	}
	
	public Map<String, String> getResults() {
		return results;
	}

	public void setResults(Map<String, String> results) {
		this.results = results;
	}

}
