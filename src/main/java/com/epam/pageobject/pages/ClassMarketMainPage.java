package com.epam.pageobject.pages;

import java.util.Properties;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.epam.steps.Steps;
import com.epam.utils.WebDriverWaitUtils;

public class ClassMarketMainPage extends AbstractPage {
	private final static String PAGE_URL = "http://www.classmarker.com/";
	
	private static String propertiesFileName = "users.properties";
	private static Properties resource = Steps.getPropertyFile(propertiesFileName);
	
//	private static String currentDir = System.getProperty("user.dir");
//	private ResourceBundle resource = ResourceBundle.getBundle( currentDir + "\\users");
	private String login_name = resource.getProperty("loginClassMarket");
	private String password = resource.getProperty("passwordClassMarket");

	@FindBy(xpath = "//*[@id='un']")
	private WebElement loginField;

	@FindBy(xpath = "//*[@id='pw']")
	private WebElement passwordField;

	@FindBy(xpath = "//input[@class='login-button']")
	private WebElement buttonLogin;
	
	@FindBy(xpath="//span[@class='first']")
	private WebElement logoDashboard;

	public ClassMarketMainPage() {
		PageFactory.initElements(webDriver, this);
	}

	@Override
	public void openPage() {
		webDriver.navigate().to(PAGE_URL);
	}

	public void login() {
		
		loginField.sendKeys(login_name);
		passwordField.sendKeys(password);		
		buttonLogin.click();
		WebDriverWaitUtils.waitForElementVisible(logoDashboard, 10);

	}

}
