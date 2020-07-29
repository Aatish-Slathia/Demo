package com.pearson.teach.init;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.pearson.teach.library.ExcelLibrary;
import com.pearson.teach.listeners.TestListener;
import com.pearson.teach.util.WebActionUtil;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.github.bonigarcia.wdm.WebDriverManager;
/***********************************************************************
* @author 				:		Aatish Slathia
* @description			: 		Implemented Application Precondition and Postconditions
* @Variables			: 	  	Declared and Initialised WebDriver, Instance for WebActionUtil Page,Extent Reports,Excel Library
* @BeforeClass          :       Launched the Browser with Disabling Browser Notifications
* @BeforeMethod			: 		Signed In To the Application and Entered the Dashboard Screen
* @AfterMethod          :       Sign Out From the Application
* @AfterClass           :       Close the Browser
*/
public class BaseTest implements IAutoConst {

	public static WebDriver driver;
	public long ETO = 10, ITO = 10;
	public WebActionUtil WebActionUtil;
	//public ExcelLibrary excelLibrary = new ExcelLibrary();
	public boolean skipTest = false;
	public static ExtentHtmlReporter extent;
	public static ExtentTest test;
	public static ExtentTest logger;
	public static ExtentReports ex;
	public static Properties prop;
	
	/**
	 * @author:Shreya Ugavekar
	 * 
	 * Description: Launch the Browser specified and disable the Browser notifications
	 */


	
	@BeforeClass
	public void launchApp() throws Exception {
		
		try {
			prop = new Properties();
			FileInputStream fis = new FileInputStream(CONFIGPATH);
			prop.load(fis);

			} catch (FileNotFoundException e) {
			e.printStackTrace();
			}
		

			String browserName = prop.getProperty("Path");
			
			Thread.sleep(3000);
		System.out.println(prop);

		if (browserName.equalsIgnoreCase("firefox")) {
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.addPreference("dom.webnotifications.enabled", false);
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver(firefoxOptions);
		} else if (browserName.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		} else {
//			String path = System.getProperty("Path");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-notifications");
			options.addArguments("--disable-infobars");
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		driver.manage().timeouts().implicitlyWait(ITO, TimeUnit.SECONDS);
		WebActionUtil = new WebActionUtil(driver, ETO);
		try
		{
		prop = new Properties();
		FileInputStream fis = new FileInputStream(CONFIGPATH);
		prop.load(fis);

		} catch (FileNotFoundException e) {
		e.printStackTrace();
		}

		String appURL= prop.getProperty("App_URL");
		driver.get(appURL);
		driver.manage().window().maximize();

	}

	@AfterClass
	public void closeApp() {
		/* Close the browser */
		try {
			if (driver != null) {
				driver.quit();
				WebActionUtil.info("Closing Browser");
				WebActionUtil.info("Killing the Tasks created by the Automation Run");
				 Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
			} else {
		WebActionUtil.fail("@AfterClass driver instance is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			WebActionUtil.fail(e.getMessage());
		}

	}
}

	
	
