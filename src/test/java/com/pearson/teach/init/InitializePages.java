package com.pearson.teach.init;

import org.openqa.selenium.WebDriver;

import com.pearson.teach.pages.LoginPage;
import com.pearson.teach.util.WebActionUtil;


/**
 * @author         aatish.s@testyantra.com
 * @description    Initialize all pages with driver,ETO, WebAactionUtil
 */


public class InitializePages {
	
	public LoginPage loginPage;
	
	public InitializePages(WebDriver driver,long ETO,WebActionUtil WebActionUtil) {
		loginPage = new LoginPage(driver, ETO, WebActionUtil);
		
	}
	
	
	
}
