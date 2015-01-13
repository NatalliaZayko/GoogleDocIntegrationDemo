package com.epam.pageobject.pages;

import java.util.ResourceBundle;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.epam.utils.SpreadsheetUtils;
import com.epam.utils.WebDriverWaitUtils;

public class GooglePage extends AbstractPage {
	private static String PAGE_URL = SpreadsheetUtils.getUrlForCode();

	private ResourceBundle resource = ResourceBundle.getBundle("users");
	private String login_name = resource.getString("loginGoogleDocs");
	private String password = resource.getString("passwordGoogleDocs");

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

		WebDriverWaitUtils.waitForElementVisible(req, 10);
		String authorizeCode = authorizeCodeField.getText();
		authorizeCode = authorizeCode.split(" ")[1];
		authorizeCode = authorizeCode.substring(authorizeCode.indexOf("=") + 1);
		return authorizeCode;
	}

}
