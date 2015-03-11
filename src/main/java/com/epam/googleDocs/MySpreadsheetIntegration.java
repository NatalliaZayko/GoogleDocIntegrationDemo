package com.epam.googleDocs;

import com.epam.steps.Steps;
import com.google.gdata.util.*;

import java.io.IOException;
import java.net.MalformedURLException; 
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MySpreadsheetIntegration {

	public static void main(String[] args) throws AuthenticationException,
			MalformedURLException, IOException, ServiceException,
			GeneralSecurityException, InterruptedException {
		Scanner in = new Scanner(System.in);
		System.out.println("Week:");
		int week = in.nextInt();
		in.close();	
		
		
		
		Steps.authorize();
		
		Steps.loginInClassMarket();
		
		//getting set of tests to go through to get results 
		Set<String> setOfModules = Steps.getSetOfTestsByWeek(week);
		Set<String> setOfTests = new HashSet<String>();
		
		Map<String, String> associations = Steps.getAssociations();
		
		for (String string : setOfModules) {
			String test = associations.get(string);
			
			if (test != null) {
				test.trim();
			}
			
			setOfTests.add(test);
		}
		
		String stringToRemove = null;
		setOfTests.remove(stringToRemove);
		
		System.out.println("Getting results from classmarker.com ...");
		System.out.println();
		//getting results
		for (String test : setOfTests) {
			String module = null;
			for (Map.Entry<String, String> entry : associations.entrySet()) {
				if(entry.getValue().equals(test)) {
					module = entry.getKey();
				}
			}
			System.out.println("Getting results for module: " + module);
			
			Steps.getListNames(week, module);
			String testRootName = test.substring(0,test.indexOf("/"));
			String testChildName = test.substring(test.indexOf("/") + 1);
			Steps.openResults(testRootName,testChildName);		
			Steps.getResults(week);
			Steps.setResults(week);
		}
		Steps.closeBrowser();
		System.out.println("Done...");
	}
}
