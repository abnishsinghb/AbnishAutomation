package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import util.ElementUtil;

public class LoginPage {

	private WebDriver driver;

    // Locators
    private By emailInputLocator = ElementUtil.getElementIdentifierFromJson("email", "name");
    private By passwordInputLocator = ElementUtil.getElementIdentifierFromJson("password", "name"); 
    private By loginButtonLocator = ElementUtil.getElementIdentifierFromJson("login_Btn", "xpath");
    private By forgottenPasswordLinkLocator = ElementUtil.getElementIdentifierFromJson("Forgotten", "linkText");
    private By logoutLinkLocator = ElementUtil.getElementIdentifierFromJson("Logout_Txt", "xpath");


    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Methods
    public void enterEmail(String email) {
        WebElement emailInput = driver.findElement(emailInputLocator);
        emailInput.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passwordInput = driver.findElement(passwordInputLocator);
        passwordInput.sendKeys(password);
    }

    public void clickLoginButton() {
        WebElement loginButton = driver.findElement(loginButtonLocator);
        loginButton.click();
    }

    public void clickForgottenPasswordLink() {
        WebElement forgottenPasswordLink = driver.findElement(forgottenPasswordLinkLocator);
        forgottenPasswordLink.click();
    }

    public boolean checkForgotPwdLink(){
        return driver.findElement(forgottenPasswordLinkLocator).isDisplayed();
    }

    public boolean checkLogoutLink(){
        return driver.findElement(logoutLinkLocator).isDisplayed();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickLoginButton();
    }

    public String getForgotPwdPageUrl(){
        String forgotPwdPageUrl = driver.getCurrentUrl();
        return forgotPwdPageUrl;
    }
    
	public By getTxt_EmailInputLocator() {
		return emailInputLocator;
	}	
	
	public By getTxt_passwordInputLocator() {
		return passwordInputLocator;
	}	
	public By getBtn_loginButton() {
		return loginButtonLocator;
	}

	public By getLink_logout() {
		return logoutLinkLocator;
	}
	
}