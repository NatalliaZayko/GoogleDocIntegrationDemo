package com.epam.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;

public class DateUtils {

	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),
				Locale.getDefault());
		calendar.setTime(new java.util.Date());
		int currentYear = calendar.get(Calendar.YEAR);
		return currentYear;

	}

	public static Map<String, Calendar> getDatesFromDoc(int week,
			ListFeed listFeed) {
		Map<String, Calendar> dates = new HashMap<String, Calendar>();
		List<ListEntry> rows = listFeed.getEntries();
		Set<String> tags = rows.get(0).getCustomElements().getTags();
		String weekTag = SpreadsheetUtils.getTagByNumber(week, tags, "week");
		String date = weekTag.split("week")[1];

		int currentYear = getCurrentYear();

		int monthFirst = Integer.parseInt(date.subSequence(0, 2).toString()) - 1;
		int dayFirst = Integer.parseInt(date.subSequence(2, 4).toString());
		Calendar startDate = Calendar.getInstance(TimeZone.getDefault(),
				Locale.getDefault());
		// startDate.set(currentYear, monthFirst, dayFirst);
		startDate.set(2014, monthFirst, dayFirst);
		int monthLast = Integer.parseInt(date.subSequence(5, 7).toString()) - 1;
		int dayLast = Integer.parseInt(date.subSequence(7, 9).toString());
		Calendar finishDate = Calendar.getInstance(TimeZone.getDefault(),
				Locale.getDefault());
		// finishDate.set(currentYear, monthLast, dayLast);
		finishDate.set(2014, monthLast, dayLast);
		dates.put("startDate", startDate);
		dates.put("finishDate", finishDate);

		return dates;
	}

	public static Calendar parserDate(String date) {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),
				Locale.getDefault());
		String[] parts = date.split(" ");
		String day = parts[1];
		String month = parts[2];
		String year = parts[3];
		year = "20" + year.substring(year.indexOf("'") + 1);
		int yearInt = Integer.parseInt(year);
		int dayInt = Integer.parseInt(day);
		SimpleDateFormat format = new SimpleDateFormat("MMM");
		Date numberOfMonth;
		try {
			numberOfMonth = format.parse(month);
			format.applyPattern("MM");
			int monthInt = Integer.parseInt(format.format(numberOfMonth)) - 1;
			calendar.set(yearInt, monthInt, dayInt);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return calendar;
	}

	@SuppressWarnings("deprecation")
	public static boolean isDateInTheRange(Calendar startDate,
			Calendar finishDate, Calendar dateOfPassing) {

		if (dateOfPassing.after(startDate)
				|| ((dateOfPassing.getTime().getYear() == startDate.getTime()
						.getYear())
						&& (dateOfPassing.getTime().getMonth() == startDate
								.getTime().getMonth()) && (dateOfPassing
						.getTime().getDay() == startDate.getTime().getDay()))) {
			if (dateOfPassing.before(finishDate)
					|| ((dateOfPassing.getTime().getYear() == finishDate
							.getTime().getYear())
							&& (dateOfPassing.getTime().getMonth() == finishDate
									.getTime().getMonth()) && (dateOfPassing
							.getTime().getDay() == finishDate.getTime()
							.getDay()))) {
				return true;
			}

		}
		return false;
	}

	public static String getDateForXpath(Calendar calendar) {
		String DATE_FORMAT = "dd MMM yy";
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
				DATE_FORMAT);
		String dateForXpath = sdf.format(calendar.getTime()).substring(0, 7)
				+ "\'" + sdf.format(calendar.getTime()).substring(7);
		return dateForXpath;
	}
}
