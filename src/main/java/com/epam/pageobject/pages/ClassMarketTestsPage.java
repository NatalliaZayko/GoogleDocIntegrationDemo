package com.epam.pageobject.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.epam.utils.HashMapSkin;
import com.epam.utils.WebDriverWaitUtils;

public class ClassMarketTestsPage extends AbstractPage {

	private static String PAGE_URL = "http://www.classmarker.com/a/tests/";
	private final static String PARENT_COURSE_XPATH = "//.[@class='test-name name'][text()='%s']";
	private final static String BUTTON_RESULT_OF_COURSE = "//div[ancestor::li/div/p[text()='%s']]/a[text()='Results']";
	//private final static String RESULT = "//div[@class='col-span-2 graph'][ancestor::li//a[text()='%s']]/span[@class='value']";
	
	private final static String RESULT_ROW = "//*[@id='content']/div/div[@class='table']/ul/li";
	private final static String NAME = ".//div/div[1]/p/a";
	private final static String RESULT = ".//span[@class='value']";

	private HashMapSkin hashMapSkin = new HashMapSkin();

	private final static String LINK_NEXT = "//a[contains(text(),'Next')]";
	@FindBy(xpath = LINK_NEXT)
	private WebElement linkNext;
	

	public ClassMarketTestsPage() {
		PageFactory.initElements(webDriver, this);

	}

	public void openResults(String courseRootName, String courseChildName) {

		WebElement parentCourse = webDriver.findElement(By.xpath(String.format(
				PARENT_COURSE_XPATH, courseRootName)));
		parentCourse.click();
		WebElement childCourse = webDriver.findElement(By.xpath(String.format(
				BUTTON_RESULT_OF_COURSE, courseChildName)));
		childCourse.click();

	}

	
	public HashMapSkin searchResults(List<String> names) {
		
		List<WebElement> resultRows = webDriver.findElements(By.xpath(RESULT_ROW));
		
		for (String name : names) {
			String nameOnGoogleSheets = name.toLowerCase().trim();
			String nameOnClassMarker = null;
			
			for (WebElement result : resultRows) {
				WebElement nameElement = result.findElement(By.xpath(NAME));
				nameOnClassMarker = nameElement.getText().toLowerCase().trim();
				
				if (nameOnGoogleSheets.equals(nameOnClassMarker)) {
					String percent = result.findElement(By.xpath(RESULT)).getText();
					hashMapSkin.add(name, percent);
				}
			}
		}

		if (WebDriverWaitUtils.isElementPresent(LINK_NEXT, 5)) {

			linkNext.click();		
			searchResults(names);

		}

		return hashMapSkin;

	}
	


	@Override
	public void openPage() {
		webDriver.navigate().to(PAGE_URL);
	}

}
