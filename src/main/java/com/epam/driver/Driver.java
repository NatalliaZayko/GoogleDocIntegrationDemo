package com.epam.driver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Driver {
	private WebDriver webDriver;
	private static Driver instance = null;
	
	private Driver(){
		webDriver = new FirefoxDriver();
		webDriver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
		webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		webDriver.manage().window().maximize();
	}
	
	
	public static WebDriver getDriver(){
		if(instance == null){
			instance = new Driver();
		}
		return instance.webDriver;
		
	}
	
	
	public static void closeDriver(){
		instance.webDriver.quit();
		instance = null;
	}
	
}
