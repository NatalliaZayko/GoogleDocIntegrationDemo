package com.epam.utils;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class SpreadsheetUtils {

	private static ResourceBundle resource = ResourceBundle
			.getBundle("googleCredentials");
	private static String client_id = resource.getString("client_id");
	private static String client_secret = resource.getString("client_secret");
	private static String redirect_url = resource.getString("redirect_url");
	private final static String SCOPE = "https://spreadsheets.google.com/feeds https://docs.google.com/feeds ";;
	private final static String SPREADSHEETFEED_URL = "https://spreadsheets.google.com/feeds/spreadsheets/private/full";
	private static ListFeed listFeed;
	
	
	private static GoogleAuthorizationCodeFlow getFlow() {

		HttpTransport httpTransport = null;
		try {
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

		return new GoogleAuthorizationCodeFlow(httpTransport, jsonFactory,
				client_id, client_secret, Collections.singleton(SCOPE));
	}

	public static String getUrlForCode() {
		GoogleAuthorizationCodeFlow flow = getFlow();
		String authorizationUrl = flow.newAuthorizationUrl()
				.setRedirectUri(redirect_url).build();
		return authorizationUrl;
	}

	public static SpreadsheetService getService(String authorizeCode) {
		GoogleAuthorizationCodeFlow flow = getFlow();
		GoogleTokenResponse response = null;
		try {
			response = flow.newTokenRequest(authorizeCode)
					.setRedirectUri(redirect_url).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GoogleCredential credential = new GoogleCredential()
				.setFromTokenResponse(response);

		SpreadsheetService service = new SpreadsheetService(
				"SpreadSheetService");
		service.setOAuth2Credentials(credential);

		return service;

	}

	public static List<SpreadsheetEntry> getAllSpreadsheeds(
			SpreadsheetService service) throws IOException, ServiceException {
		URL SPREADSHEET_FEED_URL = new URL(SPREADSHEETFEED_URL);
		SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL,
				SpreadsheetFeed.class);
		List<SpreadsheetEntry> spreadsheets = feed.getEntries();
		return spreadsheets;
	}

	public static SpreadsheetEntry getSpreadsheetEntry(String spreadsheetTitle,
			SpreadsheetService service) throws IOException, ServiceException {
		List<SpreadsheetEntry> spreadsheets = getAllSpreadsheeds(service);
		for (SpreadsheetEntry spreadsheet : spreadsheets) {
			if (spreadsheet.getTitle().getPlainText().equals(spreadsheetTitle)) {
				return spreadsheet;
			}

		}
		return null;
	}

	public static ListFeed getListFeed(SpreadsheetService service,
			SpreadsheetEntry spreadsheet) throws IOException, ServiceException {

		WorksheetEntry worksheet = spreadsheet.getWorksheets().get(0);
		URL listFeedUrl = worksheet.getListFeedUrl();
		listFeed = service.getFeed(listFeedUrl, ListFeed.class);
		return listFeed;

	}

	public static List<String> getNames(int week, String course,
			ListFeed listFeed) throws IOException, ServiceException {

		List<String> names = new ArrayList<String>();
		List<ListEntry> rows = listFeed.getEntries();
		for (ListEntry row : rows) {
			Set<String> tags = row.getCustomElements().getTags();
			String weekTag = getTagByNumber(week, tags, "week");
			String testsscoreTag = getTagByNumber(week, tags, "testsscore");
			for (String tag : tags) {
				if (tag.equals(weekTag)) {
					if (row.getCustomElements().getValue(tag) != null
							&& row.getCustomElements().getValue(tag).equals(course)) 
							{
								
//this check is not needed because 
//it doesn't allow to refresh test score
//						if (row.getCustomElements().getValue(testsscoreTag) == null) {
								
							String name = row.getCustomElements().getValue(
									"mentee");
							names.add(name);
//						}
					}
				}
			}

		}

		return names;
	}

	public static String getTagByNumber(int week, Set<String> tags, String partOftagName) {
		int i = 0;
		for (String tag : tags) {
			if (tag.contains(partOftagName)) {
				if ((i + 1) == week) {

					return tag;
				}
				
				i++;
			}
		}
		return null;
	}

	public static void setResults(HashMapSkin results, ListFeed listFeed,
			int week) throws IOException, ServiceException {

		List<ListEntry> rows = listFeed.getEntries();

		for (String key : results.getResults().keySet()) {
			for (ListEntry row : rows) {
				Set<String> tags = row.getCustomElements().getTags();
				
				String testsscoreTag = getTagByNumber(week, tags, "testsscore");
				for (String tag : tags) {
					if (tag.equals(testsscoreTag)) {
						if (row.getCustomElements().getValue("mentee") != null
								&& row.getCustomElements().getValue("mentee").equals(key)) {
							
							row.getCustomElements().setValueLocal(tag, 
									results.getResults().get(key));
							row.update();
							
						}
					}
				}
			}
		}
	}
	
	
	
	//--------------
	//gets Set of lectures by week number
	//it's used to define all mentees for one lecture 
	public static Set<String> getSetOfTestsByWeek (int week) {
		//HashSet<String> lectures = new HashSet<String>();
		Set<String> lectures = new HashSet<String>();
		List<ListEntry> rows = listFeed.getEntries();
		
		for (ListEntry row : rows) {
			Set<String> tags = row.getCustomElements().getTags();
			String weekTag = getTagByNumber(week, tags, "week");
			String lecture = row.getCustomElements().getValue(weekTag);
			if(lecture != null) {
				lectures.add(lecture.toLowerCase());
			}
		}
		
		return lectures;
	}
	
}
