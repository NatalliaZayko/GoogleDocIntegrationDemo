package com.epam.pageobject.pages;

import org.openqa.selenium.WebDriver;

import com.epam.driver.Driver;

public abstract class AbstractPage {
	protected WebDriver webDriver = Driver.getDriver();
	
	public AbstractPage() {
	}
	
	abstract public void openPage();
	

}
