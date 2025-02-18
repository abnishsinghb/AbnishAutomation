package stepDefnition;
import java.io.IOException;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import setup.DriverFactory;
import util.ElementUtil;
import util.SoftAssert;
import util.StatusResult;

public class LoginPageSteps extends DriverFactory {
	
	SoftAssert softassert = new SoftAssert();
	private LoginPage loginPage = new LoginPage(getDriver());
	 
	@Given("I have entered a valid {string} and {string}")
	public void i_have_entered_a_valid_and(String username, String password) throws InterruptedException {
        ElementUtil.clearAndFillValue(loginPage.getTxt_EmailInputLocator(), username, "Email");
        Thread.sleep(3000);
        ElementUtil.clearAndFillValue(loginPage.getTxt_passwordInputLocator(), password, "password");
    }

	@When("I click on the login button")
	public void i_click_on_the_login_button() throws InterruptedException {

    	ElementUtil.click(loginPage.getBtn_loginButton(), "login button");
    }

	@Then("I should be logged in successfully")
	public void i_should_be_logged_in_successfully() throws IOException {
    	StatusResult  StatusResult  = ElementUtil.fluentWaitForClickable(loginPage.getLink_logout());
        softassert.assertTrue(StatusResult.isSuccess(), "Logout link should be clickable.");
        softassert.assertAll();      
    }
}
