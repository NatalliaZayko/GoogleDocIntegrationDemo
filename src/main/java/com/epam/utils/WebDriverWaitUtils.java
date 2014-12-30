package com.epam.utils;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.epam.driver.Driver;


public class WebDriverWaitUtils {
	private static WebDriver webDriver = Driver.getDriver();
	
	
	private final static int DEFAULT_IMPLICITY_WAIT = 15;

	public static boolean isElementPresent(final String elementLocator) {
		webDriver.manage().timeouts()
				.implicitlyWait(DEFAULT_IMPLICITY_WAIT, TimeUnit.SECONDS);
		final int numberOfElements = webDriver.findElements(
				By.xpath(elementLocator)).size();

		return (numberOfElements > 0);
	}

	public static boolean isElementNotPresent(final String elementLocator) {
		boolean status = true;
		webDriver.manage().timeouts()
				.implicitlyWait(DEFAULT_IMPLICITY_WAIT, TimeUnit.SECONDS);
		if (webDriver.findElements(By.xpath(elementLocator)).size() != 0) {
			status = !webDriver.findElement(By.xpath(elementLocator))
					.isDisplayed();
		}

		return status;
	}

	public static boolean isElementPresent(final String elementLocator, int time) {
		webDriver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
		final int numberOfElements = webDriver.findElements(
				By.xpath(elementLocator)).size();
		
		return (numberOfElements > 0);
	}

	public static boolean isElementNotPresent(final String elementLocator, int time) {
		boolean status = true;
		webDriver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
		if (webDriver.findElements(By.xpath(elementLocator)).size() != 0) {
			status = !webDriver.findElement(By.xpath(elementLocator))
					.isDisplayed();
		}
		return status;
	}

	public static void waitForElementVisible(WebElement webElement, int time) {
		WebDriverWait driverWait = new WebDriverWait(webDriver, time);
		driverWait.until(ExpectedConditions.visibilityOf(webElement));
	}

	public static void waitForElementInvisibility(final String elementLocator, int time) {
		WebDriverWait driverWait = new WebDriverWait(webDriver, time);
		driverWait.until(ExpectedConditions.invisibilityOfElementLocated(By
				.xpath(elementLocator)));

	}

}
