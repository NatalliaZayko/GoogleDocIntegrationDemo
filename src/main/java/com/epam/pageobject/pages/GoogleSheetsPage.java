package com.epam.pageobject.pages;

import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.epam.driver.Driver;
import com.epam.steps.Steps;
import com.epam.utils.WebDriverWaitUtils;

public class GoogleSheetsPage {
	//private static String currentDir = System.getProperty("user.dir");
	//private static ResourceBundle resource = ResourceBundle.getBundle(currentDir + "\\googleDocs");
	
	private static String propertiesFileName = "googleDocs.properties";
	private static Properties resource = Steps.getPropertyFile(propertiesFileName);
	
	private static String document = resource.getProperty("nameOfDocument");
	
	private final static String USER_SHEETS = "https://docs.google.com/spreadsheets/u/0/";
	private static WebDriver webDriver = Driver.getDriver();
	
	private final String DOCUMENT = "//div[@title='" + document + "']";
	
	private final static String DOC_TITLE = "//*[@id='docs-title-inner']";
	@FindBy(xpath = DOC_TITLE)
	WebElement docTitle;
	
	private final static String TOOLS_BUTTON = "//*[@id='docs-tools-menu']";
	@FindBy(xpath = TOOLS_BUTTON)
	WebElement toolsButton;
	
	public GoogleSheetsPage() {
		PageFactory.initElements(webDriver, this);
	}

	
	public void openPage() {
		webDriver.navigate().to(USER_SHEETS);
	}
	
	
	
	public void executeGScript() throws InterruptedException {
		String script = "function myFunction() {"
				+ "var sheet = SpreadsheetApp.getActiveSheet();"
				+ "var range = sheet.getDataRange();"
				+ "var data = range.getValues();"
				+ "var lastColumnIndex = sheet.getLastColumn();"
				
				+ "var testScoreColumns = [];"
				+ "for (var j = 0; j < lastColumnIndex; j++) {"
				+ "	 if(data[0][j] == 'tests score') {"
				+ "    testScoreColumns.push(j);"
				+ "  }"
				+ "}"
				
				+ "for (var j = 0; j < testScoreColumns.length; j++) {"
				+ "  for (var i = 1; i < data.length; i++) {"
				+ "    var columnToProcess = testScoreColumns[j];"
				+ "    var testScore = data[i][columnToProcess];"
				+ "    var passMark = 0.7;"
				+ "    if(testScore != 'no test' && testScore != 'No test' && testScore != 'No Test' && testScore != '-' && testScore != '') {"
				+ "      var cellToColor = range.offset(i, columnToProcess, 1, 1);"
				+ "    	 if(testScore < passMark ) {"
				+ "       cellToColor.setBackground('#C74538');"
				+ "      } else {"
				+ "       cellToColor.setBackground('#4DC74D');"
				+ "    }"
				+ "  }"
				+ " }"
				+ "}"
				+ "}";
		
		
		
		openPage();
		webDriver.findElement(By.xpath(DOCUMENT)).click();
		WebDriverWaitUtils.waitForElementVisible(docTitle, 10);
		WebDriverWaitUtils.waitForElementVisible(toolsButton, 10);
		toolsButton.click();
		Thread.sleep(3000);
//		Actions actions = new Actions(webDriver);
//		actions.sendKeys(Keys.ARROW_DOWN);
//		Thread.sleep(3000);
//		actions.sendKeys(Keys.ARROW_DOWN);
//		Thread.sleep(3000);
//		actions.sendKeys(Keys.ARROW_DOWN);
//		Thread.sleep(3000);
//		actions.sendKeys(Keys.ENTER);
//		Thread.sleep(3000);
		docTitle.click();
	}
}
