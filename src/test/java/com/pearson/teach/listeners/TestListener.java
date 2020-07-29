package com.pearson.teach.listeners;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.pearson.teach.init.BaseTest;
import com.pearson.teach.util.WebActionUtil;

public class TestListener implements ITestListener {

	public static ExtentTest test;

	public static String screenShotPath;
	public static String ocrScreenShotPath;
	public static String apiResponseFilePath;
	public static String testName;
	public static String className;
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static long startTime;
	public static long endtTime;

	public static int iPassCount = 0;
	public static int iFailCount = 0;
	public static int iSkippedCount = 0;
	public static int iTotal_Executed;

	public static String sStartTime;
	public static String sEndTime;
	public static long lTotalExecutionTime;

	String udid, deviceName, platformVersion;

	public static ThreadLocal parentTest = new ThreadLocal();
	public static ArrayList<String> sStartMethodTime = new ArrayList<String>();
	public static ArrayList<String> sStatus = new ArrayList<String>();

	public static String currentDirPath = System.getProperty("user.dir");
	public static String homeDirPath = System.getProperty("user.home");
	BufferedReader bufferReader;
	String adbOutput;
	static InputStream is;
	Date date;
	SimpleDateFormat sdf;
	String sDate;
	static String logPath;
	public static String extentReportFolderPath;

	/**
	 * @author Aatish Slathia
	 * 
	 * @description Creating the instance of Extent Report
	 */
	@Override
	public void onStart(ITestContext testContext) {
		date = new Date();
		sdf = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
		sDate = sdf.format(date);
		sStartTime = sDate;

		extentReportFolderPath = System.getProperty("user.dir") + "/Extent_Reports";
		String extentDir = System.getProperty("user.dir") + "\\Extent_Reports" + "\\Run-" + sDate + "\\extent\\";
		screenShotPath = System.getProperty("user.dir") + "\\Extent_Reports" + "\\Run-" + sDate + "\\screenshots\\";
		ocrScreenShotPath = System.getProperty("user.dir") + "\\Extent_Reports" + "\\Run-" + TestListener.sStartTime
				+ "/OcrScreens/";
		apiResponseFilePath = System.getProperty("user.dir") + "\\Extent_Reports" + "\\Run-" + TestListener.sStartTime
				+ "/apiResponses";
		logPath = System.getProperty("user.dir") + "\\Extent_Reports" + "\\Run-" + sDate + "\\Logs\\";

		System.out.println("ExtentDir:-" + extentDir);

		/* delete extent folder if it exists before running suite */
		WebActionUtil.deleteDir(extentReportFolderPath);

		// Setting Extent Report Location
		File file = new File(extentDir);
		if (!(file.exists())) {
			boolean extentFolderStatus = file.mkdirs();
			if (extentFolderStatus == true) {
				System.out.println("ereport path : " + extentDir);
				System.out.println("--> Extent Folder Created");
			} else
				System.out.println("--> Extent Folder not Created");
		}

		String filepath = extentDir + "extent_" + sDate + ".html";
		File htmlReport = new File(filepath);
		if (!(htmlReport.exists())) {
			try {
				htmlReport.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}

		// Setting ScreenShot Report Location
		File screenShot = new File(screenShotPath);
		if (!(screenShot.exists())) {
			boolean screenshotFolderStatus = screenShot.mkdirs();
			if (screenshotFolderStatus == true)
				System.out.println("--> Screenshot Folder Created");
			else
				System.out.println("--> Screenshot Folder not Created");
		}

		// creating Log folder
		File LogFile = new File(logPath);
		if (!(LogFile.exists())) {
			boolean logFolderStatus = LogFile.mkdirs();
			if (logFolderStatus == true)
				System.out.println("--> log Folder Created");
			else
				System.out.println("--> log Folder not Created");
		}

		htmlReporter = new ExtentHtmlReporter(htmlReport);

		extent = WebActionUtil.createInstance(htmlReport.toString());

		extent.attachReporter(htmlReporter);

		extent.setSystemInfo("Application Name", "HCL");
		

		// using the file path
		htmlReporter.loadXMLConfig(new File(System.getProperty("user.dir")+"./src\\main\\resources\\extent-config.xml"));
		

	}

	@Override
	public void onTestStart(ITestResult result) {
	
		String testCaseName="WorkFlow Automation Testing";
		TestListener.testName = "WorkFlow Test";
		TestListener.test = TestListener.extent.createTest(testCaseName);
//		TestListener.test = TestListener.extent.createTest(testCaseName + " - of - " + className);
		startTime = result.getStartMillis();
		sStartMethodTime.add(startTime + "");
		parentTest.set(test);
		test.info(testName + " has started");

	}

	/**
	 * @author Aatish Slathia
	 * 
	 * @description Attaching screenshot on test pass
	 */
	@Override
	public void onTestSuccess(ITestResult result) {
		endtTime = result.getEndMillis();
		sStatus.add("Passed");
		test.pass(MarkupHelper.createLabel(testName + " Test:  PASSED", ExtentColor.GREEN));
		try {
			WebActionUtil.attachScreenShotToReport(screenShotPath, BaseTest.driver);
			Reporter.log("<a href=" + screenShotPath + testName + ".png" + "> <img width='100' height='100' src="
					+ screenShotPath + testName + ".png" + "> </a>");

			test.info("Screenshot Taken On Pass");

		} catch (Exception e) {
			e.printStackTrace();
			test.error("Unable To Take Screenshot");
		}
		System.out.println("--> " + result.getName() + " Test Script Has Been Passed");

		sStatus.add("passed");
		test.pass(MarkupHelper.createLabel(testName + " Test PASSED", ExtentColor.GREEN));
		//test.pass(result.getThrowable());

		try {
			extent.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @author Aatish Slathia
	 * 
	 * @description Attaching screenshot on test failure
	 */
	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println("test : " + test);
		testName = testName + result.hashCode();

		try {
			WebActionUtil.attachScreenShotToReport(screenShotPath, BaseTest.driver);
			Reporter.log("<a href=" + screenShotPath + testName + ".png" + "> <img width='100' height='100' src="
					+ screenShotPath + testName + ".png" + "> </a>");

			test.info("Screenshot Taken On Failure");

		} catch (Exception e) {
			e.printStackTrace();
			test.error("Unable to Take Screenshot");
		}

		System.out.println("--> " + result.getName() + " test case has been failed ");

		sStatus.add("Failed");
		test.fail(MarkupHelper.createLabel(testName + " Test FAILED", ExtentColor.RED));
		test.fail(result.getThrowable());

		try {
			extent.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

}