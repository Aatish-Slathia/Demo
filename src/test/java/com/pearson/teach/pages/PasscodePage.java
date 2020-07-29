package com.pearson.teach.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.pearson.teach.init.BasePage;
import com.pearson.teach.util.WebActionUtil;

public class PasscodePage extends BasePage{

	
		public PasscodePage(WebDriver driver, long ETO, WebActionUtil WebActionUtil) {
			super(driver, ETO, WebActionUtil);
		}
			
			@FindBy(id = "login-username")
			private WebElement userName;
			
			@FindBy(id = "login-signin")
			private WebElement userPasscode;
		
			
			public void clickOnLoginButton() {
				WebActionUtil.typeText(userName, "aabislathia", "Enterting the Username");
				WebActionUtil.clickOnElement(userPasscode, "entering the UserPasscode");
			}

	}
