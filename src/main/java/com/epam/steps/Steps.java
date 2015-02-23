package com.epam.steps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Map.Entry;

import com.epam.driver.Driver;
import com.epam.pageobject.pages.ClassMarketMainPage;
import com.epam.pageobject.pages.ClassMarketTestsPage;
import com.epam.pageobject.pages.GooglePage;
import com.epam.utils.DateUtils;
import com.epam.utils.HashMapSkin;
import com.epam.utils.SpreadsheetUtils;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

public class Steps {

	private static ResourceBundle resource = ResourceBundle.getBundle("googleDocs");
	private static String spreadsheet_name = resource.getString("nameOfDocument");
	private static String associations_spreadsheet = 
			resource.getString("nameOfAssociationsDocument");

	private static SpreadsheetService service = null;
	private static List<String> names = null;
	private static HashMapSkin results = null;
	private static Calendar startDate = null;
	private static Calendar finishDate = null;
	private static ListFeed listFeed = null;
	private static SpreadsheetEntry spreadsheet = null;

	public static void authorize() throws IOException, ServiceException {
		GooglePage googlePage = new GooglePage();
		googlePage.openPage();
		googlePage.login();
		service = SpreadsheetUtils.getService(googlePage.getAuthorizeCode());
		spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(spreadsheet_name, service);
		listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);
	}

	public static void getListNames(int week, String course)
			throws IOException, ServiceException {
		SpreadsheetEntry spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(
				spreadsheet_name, service);
		ListFeed listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);
		names = SpreadsheetUtils.getNames(week, course, listFeed);
	}

	public static void setResults(int week) throws IOException,
			ServiceException {
		SpreadsheetUtils.setResults(results, listFeed, week);
		
	}

	public static HashMapSkin getResults(int week) throws IOException,
			ServiceException {
		SpreadsheetEntry spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(spreadsheet_name, service);
		ListFeed listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);
		ClassMarketTestsPage testPage = new ClassMarketTestsPage();
		
		Map<String, Calendar> dates = DateUtils.getDatesFromDoc(week, listFeed);

		for (Entry<String, Calendar> entry : dates.entrySet()) {
			if (entry.getKey().equals("startDate")) {
				startDate = entry.getValue();
			}
			if (entry.getKey().equals("finishDate")) {
				finishDate = entry.getValue();
			}

		}
		results = testPage.searchResults(names, startDate, finishDate);
		
		
		return results;

	}

	public static void loginInClassMarket() {

		ClassMarketMainPage mainPage = new ClassMarketMainPage();
		mainPage.openPage();
		mainPage.login();
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
			String module = row.getCustomElements().getValue("modulename");
			String test = row.getCustomElements().getValue("testfolder");
			associations.put(module.toLowerCase(), test);
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
		
		
		for (Map.Entry<String, String> entry : associations.entrySet()) {
			System.out.println(entry.getKey() + "      " + entry.getValue());
		}
		
		
		return associations;
	}
}
