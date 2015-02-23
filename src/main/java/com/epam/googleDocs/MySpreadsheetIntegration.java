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
			GeneralSecurityException {
		Scanner in = new Scanner(System.in);
		//System.out.println("Course:");
		//String course = in.nextLine();		
		System.out.println("Week:");
		int week = in.nextInt();
		in.close();	
		
		
		
		Steps.authorize();
		
		Steps.loginInClassMarket();
		Set<String> setOfModules = Steps.getSetOfTestsByWeek(week);
		Set<String> setOfTests = new HashSet<String>();
		
		Map<String, String> associations = Steps.getAssociations();
		
		for (String string : setOfModules) {
			setOfTests.add(associations.get(string));
		}
		
		for (String test : setOfTests) {
			Steps.getListNames(week, test);
			String testRootName = test.substring(0,test.indexOf("/"));
			String testChildName = test.substring(test.indexOf("/") + 1);
			Steps.openResults(testRootName,testChildName);		
			Steps.getResults(week);
			Steps.setResults(week);
		}
		Steps.closeBrowser();
		
	}
}
