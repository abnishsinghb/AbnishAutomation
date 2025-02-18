package util;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import setup.DriverFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.testng.Assert;

/**
 * Utility methods for complete suite are included in this class. Eg. click,
 * clear and fill, drag and drop, fluent waits and getting environment
 * variables.
 */
public class Utility {

	 private static ExtentReports extentReports = new ExtentReports();
	    private static ExtentTest extentTest = extentReports.createTest("Test Name");

	static Map<String, Object> map = new HashMap<String, Object>();
	static Instant start;
	static Instant end;
	static String parentWindow = "";
	static String childWindow = "";

	/**
	 * Load the URL
	 */
	public static StatusResult loadURL(String locator) {
		StatusResult StatusResult = new StatusResult(true, "");
		try {
			startTimer();
			DriverFactory.getDriver().get(locator);
			endTimer();
			Reporter.log("URL loaded successfully" + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		
		} catch (Exception e) {
			StatusResult = new StatusResult(false, "Error in loading URL :" + locator + " Details: " + e.getMessage());
			endTimer();
			Assert.fail("Error in loading URL :" + locator + " Details: " + e.getMessage() + "<br></br>Time elapsed- "
					+ getElapsedTime() + "<br></br>");
		}
		return StatusResult;
	}

	/**
	 * Hard sleep
	 */
	public static void threadSleep(int durationInMiliSecs) {
		try {
			startTimer();
			Thread.sleep(durationInMiliSecs);
			endTimer();
			Reporter.log("Hard wait" + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wait for certain text to be present in DOM
	 */
	public static StatusResult waitForTextPresent(String text) {

		StatusResult StatusResult = new StatusResult(true);

		try {
			boolean isTextPresent = false;
			int count = 0;
			int start = 0;
			int delay = Integer.valueOf(getProperty("IMPLICIT_WAIT_TIME"));
			JavascriptExecutor jse = (JavascriptExecutor) DriverFactory.getDriver();
			while (jse.executeScript("return document.readyState").equals("complete") == false) {
				threadSleep(10000);
				count++;
				if (count == 3) {
					break;
				}
			}
			while ((start < delay) && DriverFactory.getDriver().getPageSource() != null) {
				isTextPresent = DriverFactory.getDriver().getPageSource().contains(text);
				if (isTextPresent) {
					return StatusResult;
				}
				Thread.sleep(500);
				start = start + 500;
			}
			StatusResult.setStatus(false);
			StatusResult.setError("Could not find the text '" + text + "' within the implict wait time-out period.");
		} catch (Exception e) {
			StatusResult.setStatus(false);
			StatusResult.setError("Error while waiting for text-present: '" + text + "' Details: " + e.getMessage());
			Assert.fail("Error while waiting for text-present: '" + text + "' Details: " + e.getMessage());
		}
		return StatusResult;
	}

	/**
	 * Fill value in a textbox
	 */
	public static StatusResult fillValue(By fieldSelector, String fieldValue, String fieldName) {
		return fillValue(fieldSelector, fieldValue, fieldName, true);
	}

	/**
	 * Fill value in a textbox Overloaded Method
	 */
	public static StatusResult fillValue(By fieldSelector, String fieldValue, String fieldName, boolean allowException) {
		StatusResult StatusResult = new StatusResult(true);
		try {
			startTimer();
			fluentWaitForVisibility(fieldSelector, 5, allowException);
			WebElement element = DriverFactory.getDriver().findElement(fieldSelector);
			element.sendKeys(fieldValue);
			endTimer();
			Reporter.log("Entered " + fieldName + " as " + fieldValue + "<br></br>Time elapsed- " + getElapsedTime()
					+ "<br></br>");
		} catch (NoSuchElementException e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				StatusResult.setError("Error in filling: " + fieldName + ". Details: " + e.getMessage());
				Assert.fail("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				StatusResult.setError("Error in filling: " + fieldName + ". Details: " + e.getMessage());
				Assert.fail("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		}
		return StatusResult;
	}

	/**
	 * Fill value in a text box, after clearing its content
	 */
	public static StatusResult clearAndFillValue(By fieldSelector, String fieldValue, String fieldName) {
		return clearAndFillValue(fieldSelector, fieldValue, fieldName, true);
	}

	/**
	 * Fill value in a text box, after clearing its content Overloaded Method
	 */
	public static StatusResult clearAndFillValue(By fieldSelector, String fieldValue, String fieldName,
			boolean allowException) {
		StatusResult StatusResult = new StatusResult(true);
		try {
			startTimer();
			fluentWaitForVisibility(fieldSelector, 5, allowException);
			fluentWaitForClickable(fieldSelector, 5, allowException);
			WebElement element = DriverFactory.getDriver().findElement(fieldSelector);
			element.clear();
			threadSleep(1000);
			element.sendKeys(fieldValue);
			endTimer();
			Reporter.log("Entered " + fieldName + " as " + fieldValue + "<br></br>Time elapsed- " + getElapsedTime()
					+ "<br></br>");
			
		} catch (NoSuchElementException e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				StatusResult.setError("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage());
				Assert.fail("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				StatusResult.setError("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage());
				Assert.fail("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		}
		return StatusResult;
	}

	/**
	 * Clear contents of a text box
	 */
	public static StatusResult clearElement(By fieldSelector, String fieldName) {
		StatusResult StatusResult = new StatusResult(true);
		try {
			startTimer();
			fluentWaitForVisibility(fieldSelector);
			WebElement element = DriverFactory.getDriver().findElement(fieldSelector);
			element.clear();
			endTimer();
			Reporter.log("Cleared " + fieldName + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} catch (NoSuchElementException e) {
			StatusResult.setStatus(false);
			endTimer();
			StatusResult.setError("Error in clearing " + fieldName + ". Details: " + e.getMessage());
			Assert.fail("Error in clearing" + fieldName + ". Details: " + e.getMessage() + "<br></br>Time elapsed- "
					+ getElapsedTime() + "<br></br>");
		} catch (Exception e) {
			StatusResult.setStatus(false);
			endTimer();
			StatusResult.setError("Error in clearing" + fieldName + ". Details: " + e.getMessage());
			Assert.fail("Error in clearing" + fieldName + ". Details: " + e.getMessage() + "<br></br>Time elapsed- "
					+ getElapsedTime() + "<br></br>");
		}
		return StatusResult;
	}
	/**
	 * Delete the field element of text box
	 */
	public static StatusResult deleteFieldElement(By fieldSelector, String fieldName) {
		StatusResult StatusResult = new StatusResult(true);
		try {
			startTimer();
			fluentWaitForVisibility(fieldSelector);
			WebElement element = DriverFactory.getDriver().findElement(fieldSelector);
			element.sendKeys(Keys.CONTROL + "a");			
			element.sendKeys(Keys.DELETE);
			endTimer();
			Reporter.log("Deleted " + fieldName + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} catch (NoSuchElementException e) {
			StatusResult.setStatus(false);
			endTimer();
			StatusResult.setError("Error in deleting " + fieldName + ". Details: " + e.getMessage());
			Assert.fail("Error in clearing" + fieldName + ". Details: " + e.getMessage() + "<br></br>Time elapsed- "
					+ getElapsedTime() + "<br></br>");
		} catch (Exception e) {
			StatusResult.setStatus(false);
			endTimer();
			StatusResult.setError("Error in deleting" + fieldName + ". Details: " + e.getMessage());
			Assert.fail("Error in deleting" + fieldName + ". Details: " + e.getMessage() + "<br></br>Time elapsed- "
					+ getElapsedTime() + "<br></br>");
		}
		return StatusResult;
	}
	/**
	 * Click on an element
	 */
	public static StatusResult click(By xpath, String fieldName) {
		return click(xpath, fieldName, true);
	}

	/**
	 * Click on an element Overloaded Method
	 */
	public static StatusResult click(By xpath, String fieldName, boolean allowException) {
		StatusResult StatusResult = new StatusResult(true, "");
		By loadingIcon = getElementIdentifierFromJson("LoadingIcon", "xpath");
		try {
			startTimer();
			while (getAttribute(loadingIcon, "class", "loading icon class StatusResult", false).contentEquals("loading")) {
				threadSleep(1000);
			}
			fluentWaitForVisibility(xpath, 5, false);
			fluentWaitForClickable(xpath, 5, false);
			WebElement element = DriverFactory.getDriver().findElement(xpath);
			element.click();
			endTimer();
			Reporter.log("Clicked on " + fieldName + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} catch (NoSuchElementException e) {
			endTimer();
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError("Error in clicking element by xpath :" + xpath + " Details: " + e.getMessage());
				Assert.fail("Error in clicking element by xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			endTimer();
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError("Error in clicking element by xpath :" + xpath + " Details: " + e.getMessage());
				Assert.fail("Error in clicking element by xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		}
		return StatusResult;
	}

	/**
	 * Verify the radio button isSelected or not
	 */
	public static StatusResult isSelected(By xpath, String fieldName) {
		return click(xpath, fieldName, true);
	}

	/**
	 * Verify the radio button isSelected or not
	 */
	public static StatusResult isSelected(By xpath, String fieldName, boolean allowException) {
		StatusResult StatusResult = new StatusResult(true, "");
		By loadingIcon = getElementIdentifierFromJson("LoadingIcon", "xpath");
		try {
			startTimer();
			while (getAttribute(loadingIcon, "class", "loading icon class StatusResult", false).contentEquals("loading")) {
				threadSleep(1000);
			}
			fluentWaitForVisibility(xpath, 5, false);
			fluentWaitForClickable(xpath, 5, false);
			WebElement element = DriverFactory.getDriver().findElement(xpath);
			element.isSelected();
			endTimer();
			Reporter.log("Selected " + fieldName + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} catch (NoSuchElementException e) {
			endTimer();
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError("Error in Verifying the element by xpath :" + xpath + " Details: " + e.getMessage());
				Assert.fail("Error in Verifying the element by xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			endTimer();
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError("Error in Verifying the element by xpath :" + xpath + " Details: " + e.getMessage());
				Assert.fail("Error in Verifying the element by xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		}
		return StatusResult;
	}

	/**
	 * Returns true if element is present and displayed on current page
	 */
	public static boolean checkIfElementIsPresent(By ele) {
		return checkIfElementIsPresent(ele, true);
	}

	/**
	 * Returns true if element is present and displayed on current page overloaded
	 * method
	 */
	public static boolean checkIfElementIsPresent(By ele, boolean allowExcpetion) {
		StatusResult StatusResult = new StatusResult(true);
		WebElement element = null;
		boolean flag = false;
		try {
			element = DriverFactory.getDriver().findElement(ele);
			if (element.isDisplayed()) {
				flag = true;
			}
		} catch (NoSuchElementException e) {
			if (allowExcpetion) {
				flag = false;
				StatusResult.setStatus(false);
				Assert.fail("No Element Found:" + ele + e.getMessage());
			} else {
				flag = false;
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			if (allowExcpetion) {
				flag = false;
				StatusResult.setStatus(false);
				Assert.fail("No Element Found:" + ele + e.getMessage());
			} else {
				flag = false;
				StatusResult.setStatus(false);
			}
		}
		return flag;
	}

	/**
	 * Returns text present between element tags
	 */
	public static String getText(By xpath, String fieldName) {
		return getText(xpath, fieldName, true);
	}

	/**
	 * Returns text present between element tags Overloaded Method
	 */
	public static String getText(By xpath, String fieldName, boolean allowException) {
		StatusResult StatusResult = new StatusResult(true);
		String textvalue = "";
		try {
			startTimer();
			fluentWaitForPresenceOfElement(xpath, 20, false);
			WebElement element = DriverFactory.getDriver().findElement(xpath);
			textvalue = element.getText();
			endTimer();
			Reporter.log("Retrieved " + fieldName + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} catch (NoSuchElementException e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				Assert.fail("Error in getting the text from xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				Assert.fail("Error in getting the text from xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		}
		return textvalue.trim();
	}

	/**
	 * Page refresh
	 */
	public static StatusResult refreshPage() {
		StatusResult StatusResult = new StatusResult(true, "");
		try {
			DriverFactory.getDriver().navigate().refresh();
			Reporter.log("Refreshed web page <br></br>");
		} catch (Exception e) {
			StatusResult.setStatus(false);
		}
		return StatusResult;
	}
	/**
	 * Page back
	 */
	public static StatusResult backPage() {
		StatusResult StatusResult = new StatusResult(true, "");
		try {
			DriverFactory.getDriver().navigate().back();
			Reporter.log("Backward web page <br></br>");
		} catch (Exception e) {
			StatusResult.setStatus(false);
		}
		return StatusResult;
	}

	/**
	 * Returns true if text is present in DOM, else false
	 */
	public static boolean checkTextPresence(String key) {
		return checkTextPresence(key, true);
	}

	/**
	 * Returns true if text is present in DOM, else false Overloaded method
	 */
	public static boolean checkTextPresence(String key, boolean allowException) {
		try {
			if (DriverFactory.getDriver().getPageSource().contains(key)) {
				return true;
			}
		} catch (Exception e) {
			if (allowException) {
				Assert.fail("Error in checking the presence of text: " + key + " Details: " + e.getMessage());
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Dynamic wait for Download the file to be completed
	 */

	public static StatusResult fluentWaitForDownloadComplete(By xpath) {
		return fluentWaitForVisibility(xpath, 60, false);
	}

	/**
	 * Dynamic wait for visibility of an element
	 */

	public static StatusResult fluentWaitForVisibility(By xpath) {
		return fluentWaitForVisibility(xpath, 60, true);
	}

	/**
	 * Dynamic wait for visibility of an element Overloaded Method
	 */
	public static StatusResult fluentWaitForVisibility(By xpath, int durationInSeconds, boolean allowException) {

		StatusResult StatusResult = new StatusResult(true);

		try {
			Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(durationInSeconds)).pollingEvery(Duration.ofMillis(100))
					.ignoring(NoSuchElementException.class);
			fluentWait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
		} catch (NoSuchElementException e) {
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError("Error while waiting for visibility of element in path: '" + xpath + "' Details: "
						+ e.getMessage());
				Assert.fail("Error while waiting for visibility of element in path: '" + xpath + "' Details: "
						+ e.getMessage());
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError("Error while waiting for visibility of element in path: '" + xpath + "' Details: "
						+ e.getMessage());
				Assert.fail("Error while waiting for visibility of element in path: '" + xpath + "' Details: "
						+ e.getMessage());
			} else {
				StatusResult.setStatus(false);
			}
		}
		return StatusResult;
	}

	/**
	 * Dynamic wait for clickability of an element
	 */
	public static StatusResult fluentWaitForClickable(By xpath) {
		return fluentWaitForClickable(xpath, 60, true);
	}

	/**
	 * Dynamic wait for clickability of an element Overloaded Method
	 */
	public static StatusResult fluentWaitForClickable(By xpath, int durationInSeconds, boolean allowException) {

		StatusResult StatusResult = new StatusResult(true);

		try {
			Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(durationInSeconds)).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class);
			fluentWait.until(ExpectedConditions.elementToBeClickable(xpath));
		} catch (NoSuchElementException e) {
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError(
						"Error while waiting to click element in path: '" + xpath + "' Details: " + e.getMessage());
				Assert.fail("Error while waiting to click element in path: '" + xpath + "' Details: " + e.getMessage());
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError(
						"Error while waiting to click element in path: '" + xpath + "' Details: " + e.getMessage());
				Assert.fail("Error while waiting to click element in path: '" + xpath + "' Details: " + e.getMessage());
			} else {
				StatusResult.setStatus(false);
			}
		}
		return StatusResult;
	}

	/**
	 * Dynamic wait for text to be available in an element
	 */
	public static StatusResult fluentWaitForTextToBePresentInElement(By element, String text, int durationInSeconds) {

		StatusResult StatusResult = new StatusResult(true);

		try {
			Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(durationInSeconds)).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class);
			fluentWait.until(ExpectedConditions.textToBePresentInElementLocated(element, text));
		} catch (NoSuchElementException e) {
			StatusResult.setStatus(false);
			StatusResult.setError("Error while waiting for visibility of element in path: '" + element + "' Details: "
					+ e.getMessage());
			Assert.fail("Error while waiting for visibility of element in path: '" + element + "' Details: "
					+ e.getMessage());
		} catch (Exception e) {
			StatusResult.setStatus(false);
			StatusResult.setError("Error while waiting for visibility of element in path: '" + element + "' Details: "
					+ e.getMessage());
			Assert.fail("Error while waiting for visibility of element in path: '" + element + "' Details: "
					+ e.getMessage());
		}
		return StatusResult;
	}

	/**
	 * Dynamic wait for number of elements to be. Takes time duration to wait as a
	 * parameter.
	 */
	public static StatusResult fluentWaitForNumberOfElementsToBe(By element, int numberOfElements, int durationInSeconds) {

		StatusResult StatusResult = new StatusResult(true);

		try {
			Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(durationInSeconds)).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class);
			fluentWait.until(ExpectedConditions.numberOfElementsToBe(element, numberOfElements));
		} catch (TimeoutException e) {
			Reporter.log("Waited " + durationInSeconds + " seconds for " + element + " to be: " + numberOfElements);
		} catch (NoSuchElementException e) {
			Reporter.log("Element not found: " + element + "Details: " + e.getMessage());
		} catch (Exception e) {
			Reporter.log("Exception occurred while waiting for " + element + " to be: " + numberOfElements + "Details: "
					+ e.getMessage());
		}
		return StatusResult;
	}

	/**
	 * Dynamic wait for number of elements to be. By default time duration to wait
	 * is 10 secs. Overloaded method
	 */
	public static StatusResult fluentWaitForNumberOfElementsToBe(By element, int numberOfElements) {
		return fluentWaitForNumberOfElementsToBe(element, numberOfElements, 10);
	}

	/**
	 * Dynamic wait for number of elements to be less than. By default wait duration
	 * is set at 60 secs
	 */
	public static StatusResult fluentWaitForNumberOfElementsToBeLessThan(By element, int number) {

		StatusResult StatusResult = new StatusResult(true);

		try {
			Wait<WebDriver> fluentWait_60seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class);
			fluentWait_60seconds.until(ExpectedConditions.numberOfElementsToBeLessThan(element, number));
		} catch (NoSuchElementException e) {
			StatusResult.setStatus(false);
			StatusResult.setError("Element not found: " + element + "Details: " + e.getMessage());
			Assert.fail("Exception occured while waiting for " + element + " to be less than: " + number + " Details: "
					+ e.getMessage());
		} catch (Exception e) {
			StatusResult.setStatus(false);
			StatusResult.setError("Exception occured while waiting for " + element + " to be less than: " + number
					+ " Details: " + e.getMessage());
			Assert.fail("Exception occured while waiting for " + element + " to be less than: " + number + " Details: "
					+ e.getMessage());
		}
		return StatusResult;
	}

	/**
	 * Dynamic wait for number of elements to be more than
	 */
	public static StatusResult fluentWaitForNumberOfElementsToBeMoreThan(By element, int number) {

		StatusResult StatusResult = new StatusResult(true);

		try {
			Wait<WebDriver> fluentWait_60seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class);
			fluentWait_60seconds.until(ExpectedConditions.numberOfElementsToBeMoreThan(element, number));
		} catch (NoSuchElementException e) {
			StatusResult.setStatus(false);
			StatusResult.setError("Element not found: " + element + "Details: " + e.getMessage());
			Assert.fail("Exception occured while waiting for " + element + " to be more than: " + number + " Details: "
					+ e.getMessage());
		} catch (Exception e) {
			StatusResult.setStatus(false);
			StatusResult.setError("Exception occured while waiting for " + element + " to be more than: " + number
					+ " Details: " + e.getMessage());
			Assert.fail("Exception occured while waiting for " + element + " to be more than: " + number + " Details: "
					+ e.getMessage());
		}
		return StatusResult;
	}

	/**
	 * Dynamic wait for presence of element in DOM
	 */
	public static StatusResult fluentWaitForPresenceOfElement(By element) {
		return fluentWaitForPresenceOfElement(element, 60, true);

	}

	/**
	 * Dynamic wait for presence of element in DOM overloaded Method
	 */
	public static StatusResult fluentWaitForPresenceOfElement(By element, int durationInSeconds, boolean allowException) {
		StatusResult StatusResult = new StatusResult(true);

		try {
			Wait<WebDriver> fluentWait_seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(durationInSeconds)).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class);
			fluentWait_seconds.until(ExpectedConditions.presenceOfElementLocated(element));
		} catch (NoSuchElementException e) {
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError("Error while waiting for presence of element in path: '" + element + "' Details: "
						+ e.getMessage());
				Assert.fail("Error while waiting for presence of element in path: '" + element + "' Details: "
						+ e.getMessage());
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			if (allowException) {
				StatusResult.setStatus(false);
				StatusResult.setError("Error while waiting for presence of element in path: '" + element + "' Details: "
						+ e.getMessage());
				Assert.fail("Error while waiting for presence of element in path: '" + element + "' Details: "
						+ e.getMessage());
			} else {
				StatusResult.setStatus(false);
			}
		}
		return StatusResult;
	}

	/**
	 * Returns a web element, will wait for its visibility
	 */

	public static WebElement findElementWhenVisible(By xpath) {
		fluentWaitForVisibility(xpath);
		return DriverFactory.getDriver().findElement(xpath);
	}

	/**
	 * Returns a web element, without waiting for visibility
	 */

	public static WebElement findElementWithoutCheckingVisibility(By xpath) {
		return DriverFactory.getDriver().findElement(xpath);
	}

	/**
	 * Returns a list of web element, will wait for its visibility
	 */
	public static List<WebElement> findElementsWhenVisible(By xpath) {
		fluentWaitForVisibility(xpath);
		return DriverFactory.getDriver().findElements(xpath);
	}

	/**
	 * Returns a web element, without waiting for its visibility
	 */
	public static List<WebElement> findElementsWithoutCheckingVisibility(By xpath) {
		return DriverFactory.getDriver().findElements(xpath);
	}

	/**
	 * Returns value of an attribute of an element
	 */
	public static String getAttribute(By xpath, String attributeName, String fieldName) {
		return getAttribute(xpath, attributeName, fieldName, true);
	}

	/**
	 * Returns value of an attribute of an element, without throwing exception
	 * Overloaded method
	 */
	public static String getAttribute(By xpath, String attributeName, String fieldName, boolean allowException) {
		StatusResult StatusResult = new StatusResult(true);
		String attributeValue = "";
		try {
			startTimer();
			fluentWaitForPresenceOfElement(xpath, 60, allowException);
			attributeValue = DriverFactory.getDriver().findElement(xpath).getAttribute(attributeName);
			endTimer();
			Reporter.log("Retrieved " + fieldName + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			StatusResult.setStatus(true);
		} catch (NoSuchElementException e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				Assert.fail("Error in retrieving the attribute value of xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
				endTimer();
				StatusResult.setError("Error in retrieving the attribute value of xpath :" + xpath + " Details: "
						+ e.getMessage() + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			}
		} catch (Exception e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				Assert.fail("Error in retrieving the attribute value of xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
				endTimer();
				StatusResult.setError("Error in retrieving the attribute value of xpath :" + xpath + " Details: "
						+ e.getMessage() + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			}
		}
		return attributeValue;
	}

	/**
	 * Dynamic wait for application to be loaded
	 */
	public void fluentWaitForApplicationLoad() {
		/**
		 * Hard wait, for map to be loaded with all the elements
		 */
		Utility.threadSleep(10000);
	}

	/**
	 * Dynamic wait for invisibility of an element. By default wait duration is set
	 * at 7 secs
	 */
	public static void fluentWaitForInvisibilityOfLoadingIcon() {
		StatusResult StatusResult = new StatusResult(true);
		By element = getElementIdentifierFromJson("LoadingIcon", "xpath");

		try {
			Wait<WebDriver> fluentWait_3seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(3)).pollingEvery(Duration.ofMillis(100))
					.ignoring(NoSuchElementException.class);
			fluentWait_3seconds.until(ExpectedConditions.attributeToBe(element, "class", "loading"));
			fluentWait_3seconds.until(ExpectedConditions.attributeToBe(element, "class", ""));
		} catch (Exception e) {
			StatusResult.setStatus(true);
		}
	}

	/**
	 * Dynamic wait for invisibility of an element. By default wait duration is set
	 * at 7 secs
	 */
	public static void waitForLoadingToComplete() {
		StatusResult StatusResult = new StatusResult(true);
		// By element = getElementIdentifierFromJson("LoadingIcon", "xpath");
		By copyRightText = getElementIdentifierFromJson("CopyrightInfoLbl", "xpath");
		Utility.threadSleep(1000);
		// fluentWaitForClickable(copyRightText, 5, false);
		int counter = 0;
		while (!checkClickability(copyRightText, "Copy right text") & counter < 15) {
			Utility.threadSleep(500);
			++counter;
		}
		try {

		} catch (Exception e) {
			StatusResult.setStatus(false);
		}
	}

	/**
	 * Verify whether able to click the field
	 */
	public static boolean checkClickability(By fieldSelector, String fieldName) {
		try {
			threadSleep(1000);
			WebElement element = DriverFactory.getDriver().findElement(fieldSelector);
			element.click();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Dynamic wait for an element's attribute's value to be. By default wait
	 * duration is set at 30 secs
	 */
	public static void fluentWaitForValueOfAttributeToBe(By element, String attribute, String value) {
		fluentWaitForValueOfAttributeToBe(element, attribute, value, 30);
	}

	/**
	 * Dynamic wait for an element's attribute's value to be. Overloaded
	 */
	public static void fluentWaitForValueOfAttributeToBe(By element, String attribute, String value,
			int waitDurationInSeconds) {

		StatusResult StatusResult = new StatusResult(true);

		try {
			Wait<WebDriver> fluentWait_30seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(waitDurationInSeconds)).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class);
			fluentWait_30seconds.until(ExpectedConditions.attributeContains(element, attribute, value));
		} catch (NoSuchElementException e) {
			StatusResult.setStatus(false);
			StatusResult.setError("Error while waiting for element: " + element + " attribute: " + attribute
					+ " value to be: " + value + " Details: " + e.getMessage());
			Assert.fail("Error while waiting for element: " + element + " attribute: " + attribute + " value to be: "
					+ value + " Details: " + e.getMessage());
		} catch (Exception e) {
			StatusResult.setStatus(false);
			StatusResult.setError("Error while waiting for element: " + element + " attribute: " + attribute
					+ " value to be: " + value + " Details: " + e.getMessage());
			Assert.fail("Error while waiting for element: " + element + " attribute: " + attribute + " value to be: "
					+ value + " Details: " + e.getMessage());
		}
	}

	/**
	 * Dynamic wait for URL to be. By default wait duration is set at 30 secs.
	 */
	public static void fluentWaitForURLTobe(String URL) {

		try {
			Wait<WebDriver> fluentWait_30seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(30)).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class);
			fluentWait_30seconds.until(ExpectedConditions.urlToBe(URL));
		} catch (Exception e) {
			Reporter.log("Exception while waiting for URL to be: " + URL + " Details: " + e.getMessage());
		}
	}

	/**
	 * Dynamic wait for URL contains some text. By default wait duration is set at
	 * 30 secs.
	 */
	public static boolean fluentWaitForURLToHaveText(String text) {

		try {
			Wait<WebDriver> fluentWait_60seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofSeconds(1))
					.ignoring(NoSuchElementException.class);
			fluentWait_60seconds.until(ExpectedConditions.urlContains(text));
			return true;
		} catch (Exception e) {
			Reporter.log("Exception while waiting for URL to have text: " + text + " Details: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Returns By locator of a web element by reading it from hash map(in
	 * constants.java file)
	 */
	public static By getElementIdentifierFromJson(String elementName, String identifierType) {
		By by = null;
		// File jsonfile = new File("ObjectRepository.json");

		try {
			switch (identifierType) {
			case "xpath":
				// by = By.xpath(String.valueOf(JsonPath.read(jsonfile,
				// "$."+elementName)));
				by = By.xpath((String) Constants.map.get(elementName));
				break;
			case "CSS":
				// by = By.cssSelector(String.valueOf(JsonPath.read(jsonfile,
				// "$."+elementName)));
				by = By.cssSelector((String) Constants.map.get(elementName));
				break;
			case "ID":
				// by = By.id(String.valueOf(JsonPath.read(jsonfile,
				// "$."+elementName)));
				by = By.id((String) Constants.map.get(elementName));
				break;
			case "tagName":
				// by = By.tagName(String.valueOf(JsonPath.read(jsonfile,
				// "$."+elementName)));
				by = By.tagName((String) Constants.map.get(elementName));
				break;
			case "linkText":
				// by = By.linkText(String.valueOf(JsonPath.read(jsonfile,
				// "$."+elementName)));
				by = By.linkText((String) Constants.map.get(elementName));
				break;
			case "name":
				// by = By.name(String.valueOf(JsonPath.read(jsonfile,
				// "$."+elementName)));
				by = By.name((String) Constants.map.get(elementName));
				break;
			case "partialLinkText":
				// by =
				// By.partialLinkText(String.valueOf(JsonPath.read(jsonfile,
				// "$."+elementName)));
				by = By.partialLinkText((String) Constants.map.get(elementName));
				break;
			case "className":
				// by = By.className(String.valueOf(JsonPath.read(jsonfile,
				// "$."+elementName)));
				by = By.className((String) Constants.map.get(elementName));
				break;
			default:
				// by = By.xpath(String.valueOf(JsonPath.read(jsonfile,
				// "$."+elementName)));
				by = By.xpath((String) Constants.map.get(elementName));
			}
		} catch (Exception e) {
			Assert.fail(
					"Exception occured while fetching element identifier from Object repository: " + e.getMessage());
		}
		return by;
	}

	/**
	 * Used to load JSON data in hash map. Complements initializeJsonFile() in
	 * TestBaseSetup.java class
	 */
	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
		Map<String, Object> retMap = new HashMap<String, Object>();

		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	/**
	 * This method is also used to load JSON data in hash map, complements
	 * jsonToMap() method
	 */
	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		@SuppressWarnings("unchecked")
		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);
			if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	/**
	 * Returns value of an environment variable
	 */
	public static String getEnvVariable(String variableName, String defaultValue) {
		String value = System.getenv(variableName);
		if (value != null && !value.isEmpty()) {
			return value;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Returns true if values are sorted in expected order
	 */
	public static boolean verifySortingOrderAlphabatical(By element, String orderBy) {
		ArrayList<String> obtainedList = new ArrayList<>();
		List<WebElement> elementList = Utility.findElementsWhenVisible(element);
		for (WebElement we : elementList) {
			obtainedList.add(we.getText());
		}
		ArrayList<String> sortedList = new ArrayList<>();
		for (String s : obtainedList) {
			sortedList.add(s);
		}
		Collections.sort(sortedList, String.CASE_INSENSITIVE_ORDER);
		if (orderBy.contains("descending")) {
			Collections.reverse(sortedList);
		}
		return sortedList.equals(obtainedList);
	}

	/**
	 * Hover over an element
	 */
	public static void hoverOnElement(By element) {
		Actions action = new Actions(DriverFactory.getDriver());
		action.moveToElement(findElementWithoutCheckingVisibility(element)).perform();
	}

	/**
	 * Get Current date
	 */
	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		return dateFormat.format(date).toString();
	}

	/**
	 * Double click on web element
	 */
	public static void doubleClickOnElement(By element) {
		Actions action = new Actions(DriverFactory.getDriver());
		action.doubleClick(findElementWithoutCheckingVisibility(element)).perform();
	}
	/**
	 * Double click on web element
	 */
	public static void clickOnElement(By element) {
		Actions action = new Actions(DriverFactory.getDriver());
		action.click(findElementWithoutCheckingVisibility(element)).perform();
	}
	/**
	 * Start timer
	 */
	public static void startTimer() {
		start = Instant.now();
	}

	/**
	 * End timer
	 */
	public static void endTimer() {
		end = Instant.now();
	}

	/**
	 * Get Elapsed time
	 */
	public static String getElapsedTime() {
		Duration timeElapsed = Duration.between(start, end);
		StringBuilder sb = new StringBuilder();
		BigDecimal timeInSecs = BigDecimal.valueOf(timeElapsed.toMillis() / 1000.0);
		sb.append(String.valueOf(timeInSecs));
		sb.append("s");
		return sb.toString();
	}

	/**
	 * Context click on a link and open in new tab
	 */
	public static boolean openNewTabByContextClick(By element) {
		String parentWindow = DriverFactory.getDriver().getWindowHandle();
		Actions action = new Actions(DriverFactory.getDriver());
		action.contextClick(findElementWithoutCheckingVisibility(element)).perform();
		action.sendKeys("T").perform();
//		action.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
		threadSleep(3000);
		Set<String> windowHandles = DriverFactory.getDriver().getWindowHandles();
		Iterator<String> itr = windowHandles.iterator();
		String childWindow = "";
		while (itr.hasNext()) {
			childWindow = itr.next();
		}
		DriverFactory.getDriver().switchTo().window(childWindow);
		if (!DriverFactory.getDriver().getTitle().isEmpty()) {
			Reporter.log("Link successfully opened in new tab");
			DriverFactory.getDriver().close();
			DriverFactory.getDriver().switchTo().window(parentWindow);
			return true;
		} else {
			Reporter.log("Failed to open link in new tab");
			DriverFactory.getDriver().switchTo().window(parentWindow);
			return false;
		}
	}

	/**
	 * Get window handles
	 */
	public static void getWindowHandles() {
		Set<String> windowHandles = DriverFactory.getDriver().getWindowHandles();
		Iterator<String> itr = windowHandles.iterator();
		try {
			parentWindow = itr.next();
			childWindow = itr.next();
		} catch (NoSuchElementException e) {
			Reporter.log("Some exception occured while getting window handles.");
		}
	}

	/**
	 * Switch to tab. Currently works only for 2 tabs.
	 */
	public static void switchTab(String parentOrChild) {
		if (parentOrChild.contains("parent")) {
			DriverFactory.getDriver().switchTo().window(parentWindow);
		} else if (parentOrChild.contains("child")) {
			DriverFactory.getDriver().switchTo().window(childWindow);
		} else {
			Reporter.log("Not a valid tab name. Please provide tab name - parent or child");
		}
	}
	/**
	 * Read properties
	 */
	public static String getProperty(String key) {
		FileInputStream fis;
		Properties prop = new Properties();
		try {
			fis = new FileInputStream(Constants.PROPERTIES_FILE_LOCATION);
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop.getProperty(key);
}
	/**
	 * Get file full path
	 */
	public static String getfile(String filelocation, String filename) {
		String filefullpath = "";
		try {
			filefullpath = filelocation.concat(filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return filefullpath;
	}	
//	/**
//	 * Read input data from excel
//	 */
//	public static void getInputDataFromExcel(String sheetName) {
//		File file;
//		FileInputStream fis;
//		try {
//			file = new File(Constants.INPUT_DATA_FILE_LOCATION);
//			fis = new FileInputStream(file);
//			XSSFWorkbook workBook = new XSSFWorkbook(fis);
//			XSSFSheet sheet = workBook.getSheet(sheetName);
//			Row row;
//			int noOfRows = sheet.getLastRowNum() - sheet.getFirstRowNum();
//			int noOfMergedRegions = sheet.getNumMergedRegions();
//			Set<Integer> mergedRowSet = new TreeSet<>();
//			Map<String, Object> testDataMap = new LinkedHashMap<>();
//			String TC_NAME;
//			for (int i = 1; i <= noOfMergedRegions;) {
//				row = sheet.getRow(sheet.getMergedRegion(i).getFirstRow());
//				TC_NAME = row.getCell(1).getStringCellValue();
//				for (int j = sheet.getMergedRegion(i).getFirstRow(); j <= sheet.getMergedRegion(i).getLastRow(); j++) {
//					row = sheet.getRow(j);
//					mergedRowSet.add(j);
//					testDataMap.put(row.getCell(2).getStringCellValue(), row.getCell(3).getStringCellValue());
//					testDataMap.put(row.getCell(2).getStringCellValue() + "_NEGATIVE",
//							row.getCell(4).getStringCellValue());
//				}
//				Constants.masterMap.put(TC_NAME, testDataMap);
//				testDataMap = new LinkedHashMap<>();
//				i = i + 2;
//			}
//			for (int k = 1; k < noOfRows; k++) {
//				if (!mergedRowSet.contains(k)) {
//					testDataMap = new LinkedHashMap<>();
//					row = sheet.getRow(k);
//					TC_NAME = row.getCell(1).getStringCellValue();
//					testDataMap.put(row.getCell(2).getStringCellValue(), row.getCell(3));
//					testDataMap.put(row.getCell(2).getStringCellValue() + "_NEGATIVE",
//							row.getCell(4).getStringCellValue());
//					Constants.masterMap.put(TC_NAME, testDataMap);
//				}
//			}
//
//			workBook.close();
//			fis.close();
//		} catch (Exception e) {
//			Assert.fail("Error while initializing testData excel file" + e.getMessage());
//		}
//	}
//
//	/**
//	 * Read output data from excel
//	 */
//	public static void getOutputDataFromExcel() {
//		File file;
//		FileInputStream fis;
//		try {
//			Constants.masterOPMap = new LinkedHashMap<>();
//			file = new File(Constants.OUTPUT_DATA_FILE_LOCATION);
//			fis = new FileInputStream(file);
//			XSSFWorkbook workBook = new XSSFWorkbook(fis);
//			XSSFSheet sheet = workBook.getSheet("OutputDataSheet");
//			Row row;
//			int noOfRows = sheet.getLastRowNum() - sheet.getFirstRowNum();
//			int noOfMergedRegions = sheet.getNumMergedRegions();
//			Set<Integer> mergedRowSet = new TreeSet<>();
//			Map<String, Object> testOPDataMap = new LinkedHashMap<>();
//			String TC_NAME;
//			for (int i = 0; i < noOfMergedRegions;) {
//				row = sheet.getRow(sheet.getMergedRegion(i).getFirstRow());
//				TC_NAME = row.getCell(0).getStringCellValue();
//				for (int j = sheet.getMergedRegion(i).getFirstRow(); j <= sheet.getMergedRegion(i).getLastRow(); j++) {
//					row = sheet.getRow(j);
//					mergedRowSet.add(j);
//					testOPDataMap.put(row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue());
//				}
//				Constants.masterOPMap.put(TC_NAME, testOPDataMap);
//				testOPDataMap = new LinkedHashMap<>();
//				i = i + 1;
//			}
//			for (int k = 1; k < noOfRows; k++) {
//				if (!mergedRowSet.contains(k)) {
//					testOPDataMap = new LinkedHashMap<>();
//					row = sheet.getRow(k);
//					TC_NAME = row.getCell(1).getStringCellValue();
//					testOPDataMap.put(row.getCell(1).getStringCellValue(), row.getCell(2));
//					Constants.masterOPMap.put(TC_NAME, testOPDataMap);
//				}
//			}
//			workBook.close();
//			fis.close();
//		} catch (Exception e) {
//			Assert.fail("Error while initializing test output Data excel file" + e.getMessage());
//		}
//	}
//
//	/**
//	 * Read dependence data from excel
//	 */
//	public static void getDependenceDataFromExcel() {
//		File file;
//		FileInputStream fis;
//		try {
//			Constants.masterDependenceTestMap = new LinkedHashMap<>();
//			file = new File(Constants.Dependence_DATA_FILE_LOCATION);
//			fis = new FileInputStream(file);
//			XSSFWorkbook workBook = new XSSFWorkbook(fis);
//			XSSFSheet sheet = workBook.getSheet("DataSheet");
//			int noOfRows = sheet.getLastRowNum();
//			for (int testRowCount = 1; testRowCount <= noOfRows; testRowCount++) {
//				Constants.masterDependenceTestMap.put(sheet.getRow(testRowCount).getCell(0).getStringCellValue(),
//						sheet.getRow(testRowCount).getCell(1).getBooleanCellValue());
//			}
//			workBook.close();
//			fis.close();
//		} catch (Exception e) {
//			Assert.fail("Error while getting dependence Data from excel file" + e.getMessage());
//		}
//	}
//
//	/**
//	 * Add outputmap to masterOutputDataMap
//	 */
//	public static void setOutputData(String testCaseName) {
//		Constants.masterWriteOPMap.put(testCaseName, Constants.writeOPMap);
//		loadOutputDataToExcel();
//	}
//
//	/**
//	 * Delete testOutput.xlsx
//	 */
//	public static void deleteOutputDataExcel() {
//		File file = new File(Constants.OUTPUT_DATA_FILE_LOCATION);
//		if (file.delete()) {
//			Reporter.log("Deleted output file from the location");
//			System.out.println("file deleted");
//		} else {
//			Reporter.log("Failed to delete the output file");
//		}
//	}
//
//	/*
//	 * Create and write output data to xlsx
//	 */
//	public static void loadOutputDataToExcel() {
//		File file;
//		FileInputStream fis;
//		FileOutputStream fos;
//		try {
//			file = new File(Constants.OUTPUT_DATA_FILE_LOCATION);
//			fis = new FileInputStream(file);
//			XSSFWorkbook workBook = new XSSFWorkbook(fis);
//			// XSSFSheet sheet = workBook.createSheet("OutputDataSheet");
//			XSSFSheet sheet = workBook.getSheet("OutputDataSheet");
//			Map<String, String> innerMap = new LinkedHashMap<>();
//			// int rowNumber = 1;
//			int rowNumber = sheet.getLastRowNum();
//			int fromRowNumber = ++rowNumber;
//			int toRowNumber = rowNumber;
//			Row row = sheet.createRow(0);
//			Cell cell = row.createCell(0);
//			cell.setCellValue("Test Case Name");
//			cell = row.createCell(1);
//			cell.setCellValue("Input Field Name");
//			cell = row.createCell(2);
//			cell.setCellValue("Output Data");
//			Set<String> outerMapKeys = Constants.masterWriteOPMap.keySet();
//			// System.out.println("keys size"+outerMapKeys.size());
//			for (String outerK : outerMapKeys) {
//				row = sheet.createRow(rowNumber);
//				// System.out.println("keys"+outerK);
//				cell = row.createCell(0);
//				cell.setCellValue(outerK);
//				// ++rowNumber;
//				innerMap = Constants.masterWriteOPMap.get(outerK);
//				Set<String> innerKeys = innerMap.keySet();
//				for (String innerK : innerKeys) {
//					cell = row.createCell(1);
//					cell.setCellValue(innerK);
//					cell = row.createCell(2);
//					cell.setCellValue(innerMap.get(innerK));
//					toRowNumber = rowNumber;
//					++rowNumber;
//					row = sheet.createRow(rowNumber);
//				}
//				sheet.removeRow(sheet.getRow(rowNumber));
//				sheet.addMergedRegion(new CellRangeAddress(fromRowNumber, toRowNumber, 0, 0));
//			}
//			fis.close();
//			fos = new FileOutputStream(file);
//			workBook.write(fos);
//			workBook.close();
//			fos.close();
//			Constants.masterWriteOPMap = new LinkedHashMap<>();
//			Constants.writeOPMap = new LinkedHashMap<>();
//		} catch (Exception e) {
//			Assert.fail("Error while writing data to testOutput.xlsx file" + e.getMessage());
//			Constants.masterWriteOPMap = new LinkedHashMap<>();
//			Constants.writeOPMap = new LinkedHashMap<>();
//		}
//	}
//
//	/*
//	 * Update dependence data to xlsx
//	 */
//	public static void updateDependenceDatainExcel(String testName, Boolean testExecutionResult) {
//		File file;
//		FileInputStream fis;
//		FileOutputStream fos;
//		try {
//			file = new File(Constants.Dependence_DATA_FILE_LOCATION);
//			fis = new FileInputStream(file);
//			XSSFWorkbook workBook = new XSSFWorkbook(fis);
//			XSSFSheet sheet = workBook.getSheet("DataSheet");
//			int rowNumber = sheet.getLastRowNum();
//			Row row = sheet.createRow(0);
//			Cell cell = row.createCell(0);
//			cell.setCellValue("Test Case Name");
//			cell = row.createCell(1);
//			cell.setCellValue("Test Case Result");
//			for (int rowCount = 1; rowCount <= rowNumber; rowCount++) {
//				if (sheet.getRow(rowCount).getCell(0).getStringCellValue().contentEquals(testName)) {
//					XSSFCell cellToBeUpdated = sheet.getRow(rowCount).getCell(1);
//					cellToBeUpdated.setCellValue(testExecutionResult);
//				}
//			}
//			fis.close();
//			fos = new FileOutputStream(file);
//			workBook.write(fos);
//			workBook.close();
//			fos.close();
//		} catch (Exception e) {
//			Assert.fail("Error while updating dependence data to testDependence.xlsx file" + e.getMessage());
//			Constants.masterWriteOPMap = new LinkedHashMap<>();
//			Constants.writeOPMap = new LinkedHashMap<>();
//		}
//	}

	/*
	 * Select the value from dropdown
	 */
	public static StatusResult selectDropdown(By fieldSelector, String fieldValue, String fieldName, boolean allowException) {
		StatusResult StatusResult = new StatusResult(true);
		try {
			startTimer();
			fluentWaitForVisibility(fieldSelector, 5, allowException);
			WebElement element = DriverFactory.getDriver().findElement(fieldSelector);
			Select drpdown = new Select(element);
			drpdown.selectByVisibleText(fieldValue);
			endTimer();
			Reporter.log("Selected " + fieldName + " as " + fieldValue + "<br></br>Time elapsed- " + getElapsedTime()
					+ "<br></br>");
		} catch (NoSuchElementException e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				StatusResult.setError("Error in selecting the dropdown: " + fieldName + ". Details: " + e.getMessage());
				Assert.fail("Error in selecting the dropdown: " + fieldName + ". Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				StatusResult.setError("Error in selecting the dropdown: " + fieldName + ". Details: " + e.getMessage());
				Assert.fail("Error in selecting the dropdown: " + fieldName + ". Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		}
		return StatusResult;
	}

	/**
	 * Generating random numbers
	 */
	public static String generateRandomNumbers() {
		StatusResult StatusResult = new StatusResult(true, "");
		int random_int = 567;
		int min = 100;
		int max = 9999999;
		try {
			random_int = (int) (Math.random() * (max - min + 1) + min);
		} catch (Exception e) {
			StatusResult.setStatus(false);
		}
		return String.valueOf(random_int);
	}

	/**
	 * Scrolling element to view
	 */
	public static void scrollElementToView(By xpath, String direction) {
		By element = By.xpath("//p[contains(text(),'Copyright')]");
		click(element, "copyright");
		Actions action = new Actions(DriverFactory.getDriver());
		if (direction.contains("up")) {
			while (!(checkIfElementIsPresent(xpath, false))) {
				action.sendKeys(Keys.ARROW_UP).perform();
			}
		}
		if (direction.contains("down")) {
			while (!checkIfElementIsPresent(xpath, false)) {
				action.sendKeys(Keys.ARROW_DOWN).perform();
			}
		}
	}

	/**
	 * Page scroll
	 */
	public static void pageScroll(int times, String direction) {
		By element = By.xpath("//p[contains(text(),'Copyright')]");
		click(element, "Heading");
		Actions action = new Actions(DriverFactory.getDriver());
		if (direction.contains("up")) {
			for (int scroll = 0; scroll < times; scroll++) {
				action.sendKeys(Keys.ARROW_UP).perform();
			}
		}
		if (direction.contains("down")) {
			for (int scroll = 0; scroll < times; scroll++) {
				action.sendKeys(Keys.ARROW_DOWN).perform();
			}
		}
		threadSleep(1000);
	}

	/**
	 * Modal scroll
	 */
	public static void modalScroll(int times, String direction) {
		Actions action = new Actions(DriverFactory.getDriver());
		if (direction.contains("up")) {
			for (int scroll = 0; scroll < times; scroll++) {
				action.sendKeys(Keys.ARROW_UP).perform();
			}
		}
		if (direction.contains("down")) {
			for (int scroll = 0; scroll < times; scroll++) {
				action.sendKeys(Keys.ARROW_DOWN).perform();
			}
		}
		threadSleep(1000);
	}

	/**
	 * Returns true if element is disabled
	 */
	public static boolean checkIsElementDisabled(By ele) {
		fluentWaitForVisibility(ele, 4, false);
		WebElement element = DriverFactory.getDriver().findElement(ele);
		return !(element.isEnabled());
	}

	/**
	 * Returns true if element is enabled
	 */
	public static boolean checkIsElementEnabled(By ele) {
		fluentWaitForVisibility(ele, 4, true);
		WebElement element = DriverFactory.getDriver().findElement(ele);
		return (element.isEnabled());
	}

	/**
	 * Returns parameter from URL
	 */
	public static String getURLParameter(String splitter, int position, String fieldName) {
		StatusResult StatusResult = new StatusResult(true);
		String URL;
		String textvalue = "";
		try {
			startTimer();
			URL = DriverFactory.getDriver().getCurrentUrl();
			String[] textSplit = URL.split(splitter);
			textvalue = textSplit[position];
			Reporter.log("Retrieved " + fieldName + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			endTimer();
		} catch (NoSuchElementException e) {
			StatusResult.setStatus(false);
			endTimer();
			Assert.fail("Error in getting the paramter from url, Details: " + e.getMessage() + "<br></br>Time elapsed- "
					+ getElapsedTime() + "<br></br>");

		} catch (Exception e) {
			StatusResult.setStatus(false);
			Assert.fail("Error in getting the parameter from url, Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		}
		return textvalue.trim();
	}

	/**
	 * Initiate DB connection
	 */
	public static Connection initiateDBConnection() {
		StatusResult StatusResult = new StatusResult(true);
		Connection con = null;
		try {
			startTimer();
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@" + Utility.getProperty("DB_CONNECTION_DETAILS"),
					Utility.getProperty("DB_SCHEMA_NAME"), "myPassword");
			Reporter.log("Iniatiated DB connection<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			endTimer();
		} catch (SQLException e) {
			StatusResult.setStatus(false);
			endTimer();
			Assert.fail("Error in initialising db connection, Details: " + e.getMessage() + "<br></br>Time elapsed- "
					+ getElapsedTime() + "<br></br>");

		} catch (Exception e) {
			StatusResult.setStatus(false);
			Assert.fail("Error in initialising db connection, Details: " + e.getMessage() + "<br></br>Time elapsed- "
					+ getElapsedTime() + "<br></br>");
		}
		return con;
	}

	/**
	 * Dynamic wait for invisibility of an element. By default wait duration is set
	 * at 3 secs
	 */
	public static void fluentWaitForInvisibilityOfElemet(By element) {
		StatusResult StatusResult = new StatusResult(true);
		try {
			Wait<WebDriver> fluentWait_3seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
					.withTimeout(Duration.ofSeconds(3)).pollingEvery(Duration.ofMillis(100))
					.ignoring(NoSuchElementException.class);
			fluentWait_3seconds.until(ExpectedConditions.visibilityOfElementLocated(element));
			fluentWait_3seconds.until(ExpectedConditions.invisibilityOfElementLocated(element));
		} catch (Exception e) {
			StatusResult.setStatus(false);
		}
	}

	/**
	 * Verify whether able to fill value in the field
	 */
	public static boolean checkAbleToEnterValue(By fieldSelector, String fieldValue, String fieldName) {
		try {
			threadSleep(1000);
			WebElement element = DriverFactory.getDriver().findElement(fieldSelector);
			element.clear();
			element.sendKeys(fieldValue);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 *Verify current date in info section
	 */
	public static String info_CurrentDate() {
		
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM d, yyyy");
		   LocalDateTime now = LocalDateTime.now();
		   String info_CurrentDate = dtf.format(now);
			return info_CurrentDate;		
  }
	
	/**
	 * Delete the file
	 */
	public static void deleteDowloadedFiles() {
		StatusResult executionStatusResult = new StatusResult(true);
		if (executionStatusResult.isSuccess()) {
			File filepath = null;
			File[] objFiles = null;
			try {
				filepath = new File(Utility.getProperty("DEFAULT_DOWNLOAD_FILE_FOLDER"));
				objFiles = filepath.listFiles();
				for (int i = 0; i < objFiles.length; i++) {
					if (objFiles[i].isFile()) {
						objFiles[i].getPath();
						objFiles[i].delete();
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				try {
					filepath = null;
					objFiles = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else {
			Assert.fail("Not able delete downloded Files");
		}
	}

	/**
	 * Read Vera filename
	 */
	public static long verifyVeraFileSize(String filenumber, String format) {
		StatusResult StatusResult = new StatusResult(true, "");
		String filename = null;
		try {
			filename = filenumber.concat(format);
		} catch (Exception e) {
			StatusResult.setStatus(false);
		}
		return verifyTheFileSize(filename);
	}

	/**
	 * Read Rack filename
	 */
	public static long verifyRackFileSize(String filenumber, String format) {
		StatusResult StatusResult = new StatusResult(true, "");
		String filename = null;
		try {
			filename = filenumber.concat(format);
		} catch (Exception e) {
			StatusResult.setStatus(false);
		}
		return verifyTheFileSize(filename);
	}

	/**
	 * Read BNG filename
	 */
	public static long verifyBngFileSize(String filenumber, String format) {
		StatusResult StatusResult = new StatusResult(true, "");
		String filename = null;
		try {
			filename = filenumber.concat(format);
		} catch (Exception e) {
			StatusResult.setStatus(false);
		}
		return verifyTheFileSize(filename);
	}

	/**
	 * Read the file size
	 */
	public static long verifyTheFileSize(String filename) {
		StatusResult StatusResult = new StatusResult(true, "");
		File file = null;
		String filePath = null;
		File[] objFiles = null;
		long bytes = 0;
		long kilobytes = 0;
		try {
			file = new File(Utility.getProperty("DEFAULT_DOWNLOAD_FILE_FOLDER"));
			objFiles = file.listFiles();
			for (int i = 0; i < objFiles.length; i++) {
				if (objFiles[i].isFile()) {
					filePath = objFiles[i].getPath();
					if (filePath.contains(filename)) {
						bytes += objFiles[i].length();
						kilobytes += (bytes / 1024);
					}
				}
			}
		} catch (Exception e) {
			StatusResult.setStatus(false);
		} finally {
			try {
				file = null;
				filePath = null;
				objFiles = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return kilobytes;
	}

	/**
	 * Create a new tab
	 */
	public static void createNewTab() {

		Robot robot = null;
		try {
			robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_T);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_T);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Upload file from local machine
	 */
	public static void uploadFileFromWindows(String filefullpath) {		
		Robot robot = null;
		try {
			StringSelection selection = new StringSelection(filefullpath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
			robot = new Robot();
			robot.delay(2000);
			robot.keyPress(KeyEvent.VK_CONTROL);
    		robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(2000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Returns size present between element tags
	 */
	public static int getsize(By xpath, String fieldName) {
		return getsize(xpath, fieldName, false);
	}

	/**
	 * Returns size present between element tags Overloaded Method
	 */
	public static int getsize(By xpath, String fieldName, boolean allowException) {
		StatusResult StatusResult = new StatusResult(true);
		int sizevalue = 0;
		try {
			startTimer();
			fluentWaitForPresenceOfElement(xpath, 20, false);
			List<WebElement> elements = DriverFactory.getDriver().findElements(xpath);
			sizevalue = elements.size();
			endTimer();
			Reporter.log("Retrieved " + fieldName + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} catch (NoSuchElementException e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				Assert.fail("Error in getting the text from xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		} catch (Exception e) {
			if (allowException) {
				StatusResult.setStatus(false);
				endTimer();
				Assert.fail("Error in getting the text from xpath :" + xpath + " Details: " + e.getMessage()
						+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
			} else {
				StatusResult.setStatus(false);
			}
		}
		return sizevalue;
	}
	/**
	 * Delete all the cookies
	 */
	public static void deleteAllCookies() {
		
	DriverFactory.getDriver().manage().deleteAllCookies();
  }
	/**
	 * Check the file name checkVariablePresent
	 */		
	public static boolean checkVariablePresent(String racknumber, String format, String variable) {
		StatusResult StatusResult = new StatusResult(true, "");
		File file = null;
		String filename = null;
		String filePath = null;
		File[] objFiles = null;
		boolean result = false;
		try {
			filename = racknumber.concat(format);
			file = new File(Utility.getProperty("DEFAULT_DOWNLOAD_FILE_FOLDER"));
			objFiles = file.listFiles();
			for (int i = 0; i < objFiles.length; i++) {
				if (objFiles[i].isFile()) {
					filePath = objFiles[i].getPath();
					if (filePath.contains(filename)) {
						 result = checkVariableInFile(filePath, variable);
					}
				}
			}
		} catch (Exception e) {
			StatusResult.setStatus(false);
			Reporter.log("Not able to find the file" + " Details: " + e.getMessage());
	  }
		return result;
	}
	/**
	 * Check variable in file
	 */
	public static boolean checkVariableInFile(String filePath, String variable) {
		StatusResult StatusResult = new StatusResult(true, "");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains(variable)) {
					return true;
				}
			}
		} catch (IOException e) {
			StatusResult.setStatus(false);
			Reporter.log("Not able to read the veriable" + " Details: " + e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			Reporter.log("Error closing the reader" + " Details: " + e.getMessage());
			}
		}
		return false;
	}
}