package com.epam.steps;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.epam.driver.Driver;
import com.epam.pageobject.pages.ClassMarketMainPage;
import com.epam.pageobject.pages.ClassMarketTestsPage;
import com.epam.pageobject.pages.GooglePage;
import com.epam.utils.DateUtils;
import com.epam.utils.HashMapSkin;
import com.epam.utils.SpreadsheetUtils;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

public class Steps {

	private static ResourceBundle resource = ResourceBundle
			.getBundle("googleDocs");
	private static String spreadsheet_name = resource
			.getString("nameOfDocument");

	private static SpreadsheetService service = null;
	private static List<String> names = null;
	private static HashMapSkin results = null;

	public static void authorize() {
		GooglePage googlePage = new GooglePage();
		googlePage.openPage();
		googlePage.login();
		service = SpreadsheetUtils.getService(googlePage.getAuthorizeCode());
	

	}

	public static void getListNames(int week, String course)
			throws IOException, ServiceException {
		SpreadsheetEntry spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(
				spreadsheet_name, service);
		ListFeed listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);
		names = SpreadsheetUtils.getNames(week, course, listFeed);
	

	}
	
	
	public static void setResults(int week) throws IOException, ServiceException
	{
		SpreadsheetEntry spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(
				spreadsheet_name, service);
		ListFeed listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);		
		SpreadsheetUtils.setResults(results, listFeed, week);
		Driver.closeDriver();
	}

	public static HashMapSkin getResults(int week) throws IOException,
			ServiceException {
		SpreadsheetEntry spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(
				spreadsheet_name, service);
		ListFeed listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);
		ClassMarketTestsPage testPage = new ClassMarketTestsPage();
		Map<String, Calendar> dates = DateUtils.getDatesFromDoc(week, listFeed);
		results =testPage.searchResults(names, dates);
		return results;
				
		
	}

	public static void loginInClassMarket() {

		ClassMarketMainPage mainPage = new ClassMarketMainPage();
		mainPage.openPage();
		mainPage.login();
	}

	public static void openResults(String courseRootName, String courseChildName) {
		// TODO to do wait
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClassMarketTestsPage testsPage = new ClassMarketTestsPage();
		testsPage.openPage();
		testsPage.openResults(courseRootName, courseChildName);
	}

}
