package com.epam.steps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.epam.driver.Driver;
import com.epam.pageobject.pages.ClassMarketMainPage;
import com.epam.pageobject.pages.ClassMarketTestsPage;
import com.epam.pageobject.pages.GooglePage;
import com.epam.utils.HashMapSkin;
import com.epam.utils.SpreadsheetUtils;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

public class Steps {

	private static String propertiesFileName = "googleDocs.properties";
	private static Properties resource = Steps.getPropertyFile(propertiesFileName);
	private static String spreadsheet_name = resource.getProperty("nameOfDocument");
	private static String associations_spreadsheet = resource.getProperty("nameOfAssociationsDocument");
	
	private static SpreadsheetService service = null;
	private static List<String> names = null;
	private static HashMapSkin results = null;
	private static ListFeed listFeed = null;
	private static SpreadsheetEntry spreadsheet = null;

	public static void authorize() throws IOException, ServiceException {
		GooglePage googlePage = new GooglePage();
		googlePage.openPage();
		System.out.println();
		System.out.println("Logging on Google...");
		googlePage.login();
		System.out.println("Logged on Google...");
		System.out.println();
		
		service = SpreadsheetUtils.getService(googlePage.getAuthorizeCode());
		System.out.println("Getting authorization code on developers.google.com ...");
		
		spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(spreadsheet_name, service);
		listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);
		System.out.println("Connection to Google Sheets established...");
		System.out.println();
	}

	public static void getListNames(int week, String course)
			throws IOException, ServiceException {
		SpreadsheetEntry spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(
				spreadsheet_name, service);
		ListFeed listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);
		names = SpreadsheetUtils.getNames(week, course, listFeed);
		System.out.println("Getting results for:");
		for (String string : names) {
			System.out.println(string);
		}
		System.out.println();
	}

	public static void setResults(int week) throws IOException,
			ServiceException {
		System.out.println();
		System.out.println("Setting results to your GoogleSheets document...");
		System.out.println();
		SpreadsheetUtils.setResults(results, listFeed, week);
	}

	public static HashMapSkin getResults(int week) throws IOException, ServiceException {
		ClassMarketTestsPage testPage = new ClassMarketTestsPage();
		results = testPage.searchResults(names);
		
		System.out.println("The results are:");
		Map<String, String> showResultsMap = results.getResults();
		for (Map.Entry<String, String> entry : showResultsMap.entrySet()) {
			System.out.println(String.format("%-50s%10s", entry.getKey(), entry.getValue()));
		}
		
		return results;
	}

	public static void loginInClassMarket() {

		ClassMarketMainPage mainPage = new ClassMarketMainPage();
		mainPage.openPage();
		System.out.println("Logging on classmarker.com  ...");
		mainPage.login();
		System.out.println("Logged on classmarker.com  ...");
		System.out.println();
	}

	public static void openResults(String courseRootName, String courseChildName) {
		ClassMarketTestsPage testsPage = new ClassMarketTestsPage();
		testsPage.openPage();
		testsPage.openResults(courseRootName, courseChildName);
	}
	
	
	//-------
	public static Set<String> getSetOfTestsByWeek(int week) throws IOException, ServiceException {
		
		return SpreadsheetUtils.getSetOfTestsByWeek(week);
	}
	
	public static void closeBrowser () {
		Driver.closeDriver();
	}

	public static Map<String, String> getAssociations() throws IOException, ServiceException {
		
		SpreadsheetEntry spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(
				associations_spreadsheet, service);
		ListFeed listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);
		
		HashMap<String, String> associations = new HashMap<String, String>();
		
		List<ListEntry> rows = listFeed.getEntries();
		
		for (ListEntry row : rows) {
			String module = row.getCustomElements().getValue("modulename").trim();
			String test = row.getCustomElements().getValue("testfolder");
			if (test != null) {
				test.trim();
			}
			associations.put(module, test);
		}
		
		List<String> entriesToRemove = new ArrayList<String>();
		for (Map.Entry<String, String> entry : associations.entrySet()) {
			if(entry.getValue() == null) {
				entriesToRemove.add(entry.getKey());
			}
		}
		
		for (String string : entriesToRemove) {
			associations.remove(string);
		}
		
		
		return associations;
	}

	
	
	public static Properties getPropertyFile(String file) {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = new FileInputStream(file);
			prop.load(input);
			
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return prop;
	}
	
}
