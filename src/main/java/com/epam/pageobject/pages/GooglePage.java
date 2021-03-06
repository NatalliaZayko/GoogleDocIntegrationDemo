package com.epam.pageobject.pages;

import java.util.Properties;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.epam.steps.Steps;
import com.epam.utils.SpreadsheetUtils;
import com.epam.utils.WebDriverWaitUtils;

public class GooglePage extends AbstractPage {
	private static String PAGE_URL = SpreadsheetUtils.getUrlForCode();


	private static String propertiesFileName = "users.properties";
	private static Properties resource = Steps.getPropertyFile(propertiesFileName);
	private String login_name = resource.getProperty("loginGoogleDocs");
	private String password = resource.getProperty("passwordGoogleDocs");
	
	
	public GooglePage() {
		PageFactory.initElements(webDriver, this);
	}

	@FindBy(xpath = "//*[@id='Email']")
	private WebElement loginField;

	@FindBy(xpath = "//*[@id='Passwd']")
	private WebElement passwordField;

	@FindBy(xpath = "//pre[@id='response']/*[@class='header']")
	private WebElement authorizeCodeField;

	@FindBy(xpath = "//*[@id='signIn']")
	private WebElement signInButton;

	@FindBy(xpath = "//img[@alt='Google']")
	private WebElement imgGoogle;
	
	@FindBy(xpath="//pre[@id='requestResponseContent']")
	private WebElement req;
	
	@FindBy(xpath="//*[@id='submit_approve_access']")
	private WebElement accessButton;

	@Override
	public void openPage() {
		webDriver.navigate().to(PAGE_URL);
	}

	public void login() {
		loginField.sendKeys(login_name);
		passwordField.sendKeys(password);
		signInButton.click();
	}
	

	public String getAuthorizeCode() {
		//accepting access to Google Sheets
		try {
			Thread.sleep(10000);
			System.out.println("Granting access for your developers.google.com application to GoogleSheets...");
			accessButton.click();
		} catch (NoSuchElementException e) {
			System.out.println("Access for your developers.google.com application to GoogleSheets has already been granted...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		WebDriverWaitUtils.waitForElementVisible(req, 10);
		String authorizeCode = authorizeCodeField.getText();
		authorizeCode = authorizeCode.split(" ")[1];
		authorizeCode = authorizeCode.substring(authorizeCode.indexOf("=") + 1);
		return authorizeCode;
	}

}
