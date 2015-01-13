package com.epam.googleDocs;

import com.epam.steps.Steps;

import com.google.gdata.util.*;

import java.io.IOException;
import java.net.MalformedURLException; 
import java.security.GeneralSecurityException;
import java.util.Scanner;

public class MySpreadsheetIntegration {

	public static void main(String[] args) throws AuthenticationException,
			MalformedURLException, IOException, ServiceException,
			GeneralSecurityException {
		Scanner in = new Scanner(System.in);
		System.out.println("Course:");
		String course = in.nextLine();		
		System.out.println("Week:");
		int week = in.nextInt();
		in.close();	
		
		String courseRootName = course.substring(0,course.indexOf("/"));
		String courseChildName = course.substring(course.indexOf("/")+1);
		
		Steps.authorize();
		Steps.getListNames(week, course);
		Steps.loginInClassMarket();
		Steps.openResults(courseRootName,courseChildName);		
		Steps.getResults(week);
		Steps.setResults(week);
		
		
	}
}
