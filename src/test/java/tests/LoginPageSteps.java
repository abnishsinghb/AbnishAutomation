package tests;

import io.cucumber.java.en.Given;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import pages.LoginPage;
import setup.DriverFactory;
import util.ElementUtil;
import util.Statuss;

public class LoginPageSteps extends DriverFactory {

	private LoginPage loginPage = new LoginPage(getDriver());

    @Given("I have entered a valid username and password")
    public void i_have_entered_a_valid_username_and_password() {
//  	loginPage.enterEmail("qatestertest@gmail.com");
//    	loginPage.enterPassword("Test@123");
      	
        ElementUtil.clearAndFillValue(loginPage.getTxt_EmailInputLocator(), "qatestertest@gmail.com", "EmailInputLocator");
        ElementUtil.clearAndFillValue(loginPage.getTxt_passwordInputLocator(), "Test@123", "EmailInputLocator");
    }

    @When("I click on the login button")
    public void i_click_on_the_login_button() throws InterruptedException {
//    	loginPage.clickLoginButton();
    	ElementUtil.click(loginPage.getBtn_loginButton(), "login button");
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
    	Assert.assertEquals(loginPage.checkLogoutLink(), true);
        Statuss status = ElementUtil.fluentWaitForClickable(loginPage.getLink_logout());
        Assert.assertTrue(status.isSuccess(), "Logout link should be clickable.");
    }
}