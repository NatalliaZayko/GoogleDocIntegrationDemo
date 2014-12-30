package com.epam.steps;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

import com.epam.pageobject.pages.ClassMarketMainPage;
import com.epam.pageobject.pages.GooglePage;
import com.epam.utils.SpreadsheetUtils;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

public class Steps {
	
	private static ResourceBundle resource = ResourceBundle.getBundle("googleDocs");
	private static String spreadsheet_name = resource.getString("nameOfDocument");
	
	private static SpreadsheetService service = null;
	//private final static String SPREADSHEET_NAME = "Q4 Schedule";
	
	
	private static List<String> names = null;
	

	public static void authorize() {
		GooglePage googlePage = new GooglePage();
		googlePage.openPage();
		googlePage.login();
		service = SpreadsheetUtils.getService(googlePage.getAuthorizeCode());
		//Driver.closeDriver();

	}

	public static void getListNames(int week, String course)
			throws IOException, ServiceException {
		SpreadsheetEntry spreadsheet = SpreadsheetUtils.getSpreadsheetEntry(
				spreadsheet_name, service);
		ListFeed listFeed = SpreadsheetUtils.getListFeed(service, spreadsheet);
		names = SpreadsheetUtils.getNames(week, course, listFeed);		
		System.out.println(names);

	}

	public static void loginInClassMarket() {
		
		ClassMarketMainPage mainPage = new ClassMarketMainPage();
		mainPage.openPage();
		mainPage.login();
	}

}
