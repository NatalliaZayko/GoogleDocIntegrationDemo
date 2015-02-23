package com.epam.utils;

import java.util.HashMap;
import java.util.Map;

public class HashMapSkin {
	
	private Map<String,String> results;

	
	public HashMapSkin() {		
		results = new HashMap<String, String>();
	}
	
	public void add(String key, String value){
		
		
		if(!results.isEmpty()){
			if(results.containsKey(key)){
				String actualValue = results.get(key);
				double v1 = Double.parseDouble(value.substring(0, value.length() - 1));
				double v2 = Double.parseDouble(actualValue.substring(0, actualValue.length() - 1));
				if(v1 > v2){
					results.put(key, value);
				}
			} else {
				results.put(key, value);
			}
		}else {
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
