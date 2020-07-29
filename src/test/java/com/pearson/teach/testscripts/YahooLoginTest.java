package com.pearson.teach.testscripts;



import org.testng.annotations.Test;

import com.pearson.teach.init.BaseTest;
import com.pearson.teach.init.InitializePages;

public class YahooLoginTest extends BaseTest{

	@Test
	public void  YahooLoginTest_001() {

		InitializePages pages = new InitializePages(driver, ETO, WebActionUtil);
		pages.loginPage.clickOnLoginButton();
	}

}
