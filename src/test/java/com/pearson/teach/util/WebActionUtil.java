package com.pearson.teach.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.pearson.teach.listeners.TestListener;

/**
 * @author shreya.u@testyantra.com,vivek.d@testyantra.com,aatish.s@testyantra.com
 * @description All the utility methods required in the Project
 */

public class WebActionUtil {

	public WebDriver driver;
	WebDriverWait wait;
	public long ETO = 10;
	public JavascriptExecutor jsExecutor;
	public Actions action;
	ITestResult result;

	public static Logger logger = LogManager.getLogger(WebActionUtil.class);

	public WebActionUtil(WebDriver driver, long ETO) {
		this.driver = driver;
		this.ETO = ETO;
		wait = new WebDriverWait(driver, ETO);
		jsExecutor = (JavascriptExecutor) driver;
		action = new Actions(driver);
	}

	/**
	 * @author Shreya Ugavekar
	 * @description Method for the pass updation in extent Report,Log file,TestNG
	 *              Report
	 */

	public void pass(String message) {
		TestListener.test.pass(MarkupHelper.createLabel(message, ExtentColor.GREEN));
	}

	public void info(String message) {
		Reporter.log(message, true);
		logger.info(message);
		TestListener.test.info(message);
	}

	/**
	 * @author Shreya Ugavekar
	 * @description Method for the Warning updation in extent Report,Log file,TestNG
	 *              Reprt
	 */
	public void warn(String message) {

		logger.warn(message);
		Reporter.log(message, true);
	}

	/**
	 * @author Shreya Ugavekar
	 * @description Method for the error/Failure updation in extent Report,Log
	 *              file,TestNG Reprt
	 */

	public void fail(String message) {
		Reporter.log(message, true);
		TestListener.test.fail(MarkupHelper.createLabel(message, ExtentColor.RED));
	}
	public void error(String message) {

		logger.error(message);
		Reporter.log(message, true);
		TestListener.test.error(message);
	}
	/**
	 * @author Shreya Ugavekar,Vivek Dogra
	 * @description Wait for the Page To Load using the Expected Conditions
	 * @param seconds
	 */
	public void waitTillPageLoad(long seconds) {
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		jsExecutor = (JavascriptExecutor) driver;
		// Wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = wd -> ((JavascriptExecutor) driver)
				.executeScript("return document.readyState").toString().equals("complete");
		// Get JS is Ready
		boolean jsReady = (Boolean) jsExecutor.executeScript("return document.readyState").toString()
				.equals("complete");
		// Wait Javascript until it is Ready!
		if (!jsReady) {
			System.out.println("JS in NOT Ready!");
			// Wait for Javascript to load
			wait.until(jsLoad);
		} else {
			sleep(2);
		}
	}

	/**
	 * @author Aatish Slathia
	 * @description Method to get Duration
	 * @return
	 */
	public static String formatDuration(final long millis) {
		long seconds = (millis / 1000) % 60;
		long minutes = (millis / (1000 * 60)) % 60;
		long hours = millis / (1000 * 60 * 60);

		StringBuilder b = new StringBuilder();
		b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) : String.valueOf(hours));
		b.append(":");
		b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) : String.valueOf(minutes));
		b.append(":");
		b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));
		return b.toString();
	}

	/**
	 * @author Aatish Slathia
	 * @description Method to get the CurrentDateAndTime
	 * @return
	 */
	public static String getCurrentDateTime() {
		DateFormat customFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
		Date currentDate = new Date();
		return customFormat.format(currentDate);
	}

	private static ExtentReports extent;

	public static ExtentReports getInstance() {
		if (extent == null)
			createInstance("./Reports/HtmlReport.html");
		return extent;
	}

	/**
	 * @author Aatish Slathia
	 * @description Method to create the instance of ExtentReport
	 * @param fileName
	 * @return
	 */
	public static ExtentReports createInstance(String fileName) {
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setDocumentTitle(fileName);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setReportName(fileName);

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

		return extent;
	}

	/**
	 * @author Aatish Slathia
	 * @description Method to get screenshot
	 * @param path
	 * @param driver
	 * @return
	 */
	public static String getScreenShot(String path, WebDriver driver) {

		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destinationPath = path + ".png";
		File destination = new File(destinationPath);
		try {
			FileUtils.copyFile(src, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return destinationPath;
	}

	/**
	 * @author Aatish Slathia
	 * @description Method to attach screen shot to report
	 * @param path
	 * @param driver
	 */
	public static void attachScreenShotToReport(String path, WebDriver driver) {
		try {
			TestListener.test.addScreenCaptureFromPath(getScreenShot(path, driver));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Aatish Slathia
	 * @description delete extent folder if it exist
	 * @param path
	 */
	public static void deleteDir(String pathToDeleteFolder) {
		File extefolder = new File(pathToDeleteFolder);
		if ((extefolder.exists())) {
			deleteFolderDir(extefolder);
		}

	}

	/**
	 * @author Aatish Slathia
	 * @description Method to delete directory.
	 * @param path
	 */
	private static void deleteFolderDir(File folderToDelete) {
		File[] folderContents = folderToDelete.listFiles();
		if (folderContents != null) {
			for (File folderfile : folderContents) {
				if (!Files.isSymbolicLink(folderfile.toPath())) {
					deleteFolderDir(folderfile);
				}
			}

		}
		folderToDelete.delete();
	}

	/**
	 * @author Shreya Ugavekar
	 * @description Click on the Element
	 * @param element elementName
	 */
	public void clickOnElement(WebElement element, String elementName) {

		if (isElementClickable(element, elementName)) {
			pass("Click on " + elementName);
			element.click();
		} 
	else {
			fail(elementName + "------ is Not Clickable");
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(element)) == null);
		}
	}

	/**
	 * @author Shreya Ugavekar
	 * @description Check whether the Element is displayed with expected conditions
	 * @param element elementName
	 */
	public boolean isElementClickable(WebElement element, String elementName) {

		pass("Verifying Element is Clickable or Not");
		try {
			wait.until(ExpectedConditions.visibilityOf(element));
			wait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			fail(elementName + " is not Clickable ");
			return false;
		}
	}

	/**
	 * @author Shreya Ugavekar
	 * @description Verify the Element Text
	 * @param element expectedText
	 */
	public void verifyElementText(WebElement element, String expectedText) {
		String actualText = element.getText();
		try {
			Assert.assertEquals(actualText, expectedText);
			pass(actualText + " is matching with " + expectedText);
		} catch (Exception e) {
			fail(actualText + " is not matching with " + expectedText);
			Assert.fail(actualText + " is not matching with " + expectedText);
		}
	}

	/**
	 * @author Shreya Ugavekar
	 * @description Verify the page Title
	 * @param expectedTitle
	 */
	public void verifyTheTitle(String expectedTitle) {
		String actualTitle = driver.getTitle();
		pass(":" + actualTitle);
		Assert.assertEquals(actualTitle, expectedTitle);
		pass("Compare 'Actual title' with the 'Expected Title' ");
		pass(actualTitle + " is matching with " + expectedTitle);
	}
	/**
	 * @author Shreya Ugavekar
	 * @description To Enter the Text to the Text filed 
	 * @param element value elementName
	 */
	public void typeText(WebElement element, String value, String elementName) {
		try {
			pass("Enter the value into " + elementName);
			element.sendKeys(value);
			pass("User is able to type " + value + " into " + elementName);
		} catch (AssertionError error) {
			fail(" User is not able to type " + value + " into " + elementName);
			Assert.fail("Unable to type on " + elementName);
		} catch (Exception e) {
			fail(" User is not able to type " + value + "into " + elementName);
			Assert.fail("Unable to type in " + elementName);
		}
	}
	/**
	 * @author Shreya Ugavekar
	 * @description Click on the Element using JavaSCript 
	 * @param element  elementName
	 */
	public void clickOnElementUsingJS(WebElement element, String elementName) {
		try {
			if (isElementClickable(element, elementName)) {
				pass("User is able to click " + " into " + elementName);
				jsExecutor.executeScript("arguments[0].click();", element);
			}
		} catch (NoSuchElementException e) {
			pass("User is not able to click " + " into " + elementName);
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOf(element)) == null);
		}
	}
	/**
	 * @author Shreya Ugavekar
	 * @description Double Click On Element
	 * @param element  elementName
	 */
	public void doubleClickOnElement(WebElement element, String elementName) {
		try {
			pass("User is able to click " + " into " + elementName);
			action.doubleClick(element).perform();
		} catch (Exception e) {
			fail(" User is not able to double click on  " + elementName);
			Assert.fail("Unable to Double click on  " + elementName);
		}
	}
	/**
	 * @author Shreya Ugavekar
	 * @description sleep
	 * @param seconds
	 */
	public static void sleep(long seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @author Shreya Ugavekar
	 * @description Clear the Text
	 * @param element elementName
	 */
	public void clearText(WebElement element, String elementName) {
		pass("Clear the Text Present in" + elementName);
		element.clear();
		pass("Cleared the Text Present in" + elementName);
	}
	/**
	 * @author Shreya Ugavekar
	 * @description is Element Displayed Or Not
	 * @param element 
	 */
	public void isElementDisplayedOrNot(WebElement element) {
		sleep(3);
		boolean actual = element.isDisplayed();
		Assert.assertEquals(actual, true, "Element is Not Displayed");
	}
	/**
	 * @author Shreya Ugavekar
	 * @description Scroll to the Element
	 * @param elementName 
	 */
	public void scrollToElement(WebElement element, String elementName)  {
		waitTillPageLoad(ETO);
		pass("-------------Scrolling till the Element------------");
		try {
			jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
		fail("-------------Scroll Till the Element Has Failed ------------");
		}

	}
	/**
	 * @author vivek Dogra
	 * @description Scroll to the End of the page
	 */
	public void scrollToEndOfThePage() {
		sleep(2);
		try {
			jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			pass("Successfully Scrolled till End of the Page");
		}
		catch (Exception e) {
			fail("Unable to scroll to the End of the Page ");
			Assert.fail("Unable to scroll to the End of the Page " );
			
		}
	}
	/**
	 * @author Vivek Dogra
	 * @description Switch To Tab
	 * @param tabindex
	 */
	public void switchToTab(int tabindex) {
		try {
		ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(tabindex));
		pass("Switch to Tab Completed");
		}catch (Exception e) {
			fail("Swicthing to the Tab Failed");
			Assert.fail("Swicthing to the Tab Failed" );
			
		}
	}
	/**
	 * @author Shreya Ugavekar
	 * @description Click Using Action
	 * @param element elementName
	 */
	public void actionClick(WebElement element, String elementName) {
		try {
			action.click(element).build().perform();
			pass("Click on Element Using the action class");
		} catch (Exception e) {
			fail("Unable to click on the element ");
			Assert.fail("Unable to click on the element " );
		}
	}
	/**
	 * @author vivek Dogra
	 * @description Scroll to the End of the page
	 */
	public void scrollTillEndOfThePage() {
		sleep(2);
		try {
			jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			pass("Successfully Scrolled till End of the Page");
		}
		catch (Exception e) {
			fail("Unable to scroll to the End of the Page ");
			Assert.fail("Unable to scroll to the End of the Page " );
			
		}
	}
	
	/**
	 * @author Vivek Dogra
	 * @description Select Drop Down
	 */
	public void selectDropDown(WebElement element ,String value ,String elementName) {
		try {
			Select drpDown = new Select(element);
			drpDown.selectByVisibleText(value);
			pass("Successfully Select the Value");
		} catch (Exception e) {
			fail("Unable to Select the Value ");
			Assert.fail("Unable to Select the Value " );
		}	
	}
	
	/**
	 * @author Shreya Ugavekar
	 * @description Wait for the Page Title 
	 * @param seconds ePageTitle
	 */
	public void waitForThePageTitle(long seconds,String ePageTitle) {
		try {
		 wait= new WebDriverWait(driver, seconds);
		 wait.until(ExpectedConditions.titleContains(ePageTitle));
		 pass("Waited for the Page Title"+ePageTitle);
		}catch (Exception e)
		{
			fail("Unable to Wait for the page according to the specified Title ");
		}
	}
}