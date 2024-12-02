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
import com.aventstack.extentreports.Status;

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
public class ElementUtil {

	static Map<String, Object> map = new HashMap<String, Object>();
	static Instant start;
	static Instant end;
	static String parentWindow = "";
	static String childWindow = "";

	
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
 * Click on an element
 */
public static Statuss click(By xpath, String fieldName) {
	return click(xpath, fieldName, true);
}

/**
 * Click on an element Overloaded Method
 */
public static Statuss click(By xpath, String fieldName, boolean allowException) {
	Statuss Statuss = new Statuss(true, "");
	By loadingIcon = getElementIdentifierFromJson("LoadingIcon", "xpath");
	try {
		startTimer();
		while (getAttribute(loadingIcon, "class", "loading icon class Statuss", false).contentEquals("loading")) {
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
			Statuss.setStatus(false);
			Statuss.setError("Error in clicking element by xpath :" + xpath + " Details: " + e.getMessage());
			Assert.fail("Error in clicking element by xpath :" + xpath + " Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			Statuss.setStatus(false);
		}
	} catch (Exception e) {
		endTimer();
		if (allowException) {
			Statuss.setStatus(false);
			Statuss.setError("Error in clicking element by xpath :" + xpath + " Details: " + e.getMessage());
			Assert.fail("Error in clicking element by xpath :" + xpath + " Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			Statuss.setStatus(false);
		}
	}
	return Statuss;
}

/**
 * Verify the radio button isSelected or not
 */
public static Statuss isSelected(By xpath, String fieldName) {
	return click(xpath, fieldName, true);
}

/**
 * Verify the radio button isSelected or not
 */
public static Statuss isSelected(By xpath, String fieldName, boolean allowException) {
	Statuss Statuss = new Statuss(true, "");
	By loadingIcon = getElementIdentifierFromJson("LoadingIcon", "xpath");
	try {
		startTimer();
		while (getAttribute(loadingIcon, "class", "loading icon class Statuss", false).contentEquals("loading")) {
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
			Statuss.setStatus(false);
			Statuss.setError("Error in Verifying the element by xpath :" + xpath + " Details: " + e.getMessage());
			Assert.fail("Error in Verifying the element by xpath :" + xpath + " Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			Statuss.setStatus(false);
		}
	} catch (Exception e) {
		endTimer();
		if (allowException) {
			Statuss.setStatus(false);
			Statuss.setError("Error in Verifying the element by xpath :" + xpath + " Details: " + e.getMessage());
			Assert.fail("Error in Verifying the element by xpath :" + xpath + " Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			Statuss.setStatus(false);
		}
	}
	return Statuss;
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
	Statuss Statuss = new Statuss(true);
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
			Statuss.setStatus(false);
			Assert.fail("No Element Found:" + ele + e.getMessage());
		} else {
			flag = false;
			Statuss.setStatus(false);
		}
	} catch (Exception e) {
		if (allowExcpetion) {
			flag = false;
			Statuss.setStatus(false);
			Assert.fail("No Element Found:" + ele + e.getMessage());
		} else {
			flag = false;
			Statuss.setStatus(false);
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
	Statuss Statuss = new Statuss(true);
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
			Statuss.setStatus(false);
			endTimer();
			Assert.fail("Error in getting the text from xpath :" + xpath + " Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			Statuss.setStatus(false);
		}
	} catch (Exception e) {
		if (allowException) {
			Statuss.setStatus(false);
			endTimer();
			Assert.fail("Error in getting the text from xpath :" + xpath + " Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			Statuss.setStatus(false);
		}
	}
	return textvalue.trim();
}

/**
 * Page refresh
 */
public static Statuss refreshPage() {
	Statuss Statuss = new Statuss(true, "");
	try {
		DriverFactory.getDriver().navigate().refresh();
		Reporter.log("Refreshed web page <br></br>");
	} catch (Exception e) {
		Statuss.setStatus(false);
	}
	return Statuss;
}
/**
 * Page back
 */
public static Statuss backPage() {
	Statuss Statuss = new Statuss(true, "");
	try {
		DriverFactory.getDriver().navigate().back();
		Reporter.log("Backward web page <br></br>");
	} catch (Exception e) {
		Statuss.setStatus(false);
	}
	return Statuss;
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

public static Statuss fluentWaitForDownloadComplete(By xpath) {
	return fluentWaitForVisibility(xpath, 60, false);
}

/**
 * Dynamic wait for visibility of an element
 */

public static Statuss fluentWaitForVisibility(By xpath) {
	return fluentWaitForVisibility(xpath, 60, true);
}

/**
 * Dynamic wait for visibility of an element Overloaded Method
 */
public static Statuss fluentWaitForVisibility(By xpath, int durationInSeconds, boolean allowException) {

	Statuss Statuss = new Statuss(true);

	try {
		Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(DriverFactory.getDriver())
				.withTimeout(Duration.ofSeconds(durationInSeconds)).pollingEvery(Duration.ofMillis(100))
				.ignoring(NoSuchElementException.class);
		fluentWait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
	} catch (NoSuchElementException e) {
		if (allowException) {
			Statuss.setStatus(false);
			Statuss.setError("Error while waiting for visibility of element in path: '" + xpath + "' Details: "
					+ e.getMessage());
			Assert.fail("Error while waiting for visibility of element in path: '" + xpath + "' Details: "
					+ e.getMessage());
		} else {
			Statuss.setStatus(false);
		}
	} catch (Exception e) {
		if (allowException) {
			Statuss.setStatus(false);
			Statuss.setError("Error while waiting for visibility of element in path: '" + xpath + "' Details: "
					+ e.getMessage());
			Assert.fail("Error while waiting for visibility of element in path: '" + xpath + "' Details: "
					+ e.getMessage());
		} else {
			Statuss.setStatus(false);
		}
	}
	return Statuss;
}

/**
 * Dynamic wait for clickability of an element
 */
public static Statuss fluentWaitForClickable(By xpath) {
	return fluentWaitForClickable(xpath, 60, true);
}

/**
 * Dynamic wait for clickability of an element Overloaded Method
 */
public static Statuss fluentWaitForClickable(By xpath, int durationInSeconds, boolean allowException) {

	Statuss Statuss = new Statuss(true);

	try {
		Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(DriverFactory.getDriver())
				.withTimeout(Duration.ofSeconds(durationInSeconds)).pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);
		fluentWait.until(ExpectedConditions.elementToBeClickable(xpath));
	} catch (NoSuchElementException e) {
		if (allowException) {
			Statuss.setStatus(false);
			Statuss.setError(
					"Error while waiting to click element in path: '" + xpath + "' Details: " + e.getMessage());
			Assert.fail("Error while waiting to click element in path: '" + xpath + "' Details: " + e.getMessage());
		} else {
			Statuss.setStatus(false);
		}
	} catch (Exception e) {
		if (allowException) {
			Statuss.setStatus(false);
			Statuss.setError(
					"Error while waiting to click element in path: '" + xpath + "' Details: " + e.getMessage());
			Assert.fail("Error while waiting to click element in path: '" + xpath + "' Details: " + e.getMessage());
		} else {
			Statuss.setStatus(false);
		}
	}
	return Statuss;
}

/**
 * Dynamic wait for text to be available in an element
 */
public static Statuss fluentWaitForTextToBePresentInElement(By element, String text, int durationInSeconds) {

	Statuss Statuss = new Statuss(true);

	try {
		Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(DriverFactory.getDriver())
				.withTimeout(Duration.ofSeconds(durationInSeconds)).pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);
		fluentWait.until(ExpectedConditions.textToBePresentInElementLocated(element, text));
	} catch (NoSuchElementException e) {
		Statuss.setStatus(false);
		Statuss.setError("Error while waiting for visibility of element in path: '" + element + "' Details: "
				+ e.getMessage());
		Assert.fail("Error while waiting for visibility of element in path: '" + element + "' Details: "
				+ e.getMessage());
	} catch (Exception e) {
		Statuss.setStatus(false);
		Statuss.setError("Error while waiting for visibility of element in path: '" + element + "' Details: "
				+ e.getMessage());
		Assert.fail("Error while waiting for visibility of element in path: '" + element + "' Details: "
				+ e.getMessage());
	}
	return Statuss;
}

/**
 * Dynamic wait for number of elements to be. Takes time duration to wait as a
 * parameter.
 */
public static Statuss fluentWaitForNumberOfElementsToBe(By element, int numberOfElements, int durationInSeconds) {

	Statuss Statuss = new Statuss(true);

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
	return Statuss;
}

/**
 * Dynamic wait for number of elements to be. By default time duration to wait
 * is 10 secs. Overloaded method
 */
public static Statuss fluentWaitForNumberOfElementsToBe(By element, int numberOfElements) {
	return fluentWaitForNumberOfElementsToBe(element, numberOfElements, 10);
}

/**
 * Dynamic wait for number of elements to be less than. By default wait duration
 * is set at 60 secs
 */
public static Statuss fluentWaitForNumberOfElementsToBeLessThan(By element, int number) {

	Statuss Statuss = new Statuss(true);

	try {
		Wait<WebDriver> fluentWait_60seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
				.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);
		fluentWait_60seconds.until(ExpectedConditions.numberOfElementsToBeLessThan(element, number));
	} catch (NoSuchElementException e) {
		Statuss.setStatus(false);
		Statuss.setError("Element not found: " + element + "Details: " + e.getMessage());
		Assert.fail("Exception occured while waiting for " + element + " to be less than: " + number + " Details: "
				+ e.getMessage());
	} catch (Exception e) {
		Statuss.setStatus(false);
		Statuss.setError("Exception occured while waiting for " + element + " to be less than: " + number
				+ " Details: " + e.getMessage());
		Assert.fail("Exception occured while waiting for " + element + " to be less than: " + number + " Details: "
				+ e.getMessage());
	}
	return Statuss;
}

/**
 * Dynamic wait for number of elements to be more than
 */
public static Statuss fluentWaitForNumberOfElementsToBeMoreThan(By element, int number) {

	Statuss Statuss = new Statuss(true);

	try {
		Wait<WebDriver> fluentWait_60seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
				.withTimeout(Duration.ofSeconds(60)).pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);
		fluentWait_60seconds.until(ExpectedConditions.numberOfElementsToBeMoreThan(element, number));
	} catch (NoSuchElementException e) {
		Statuss.setStatus(false);
		Statuss.setError("Element not found: " + element + "Details: " + e.getMessage());
		Assert.fail("Exception occured while waiting for " + element + " to be more than: " + number + " Details: "
				+ e.getMessage());
	} catch (Exception e) {
		Statuss.setStatus(false);
		Statuss.setError("Exception occured while waiting for " + element + " to be more than: " + number
				+ " Details: " + e.getMessage());
		Assert.fail("Exception occured while waiting for " + element + " to be more than: " + number + " Details: "
				+ e.getMessage());
	}
	return Statuss;
}

/**
 * Dynamic wait for presence of element in DOM
 */
public static Statuss fluentWaitForPresenceOfElement(By element) {
	return fluentWaitForPresenceOfElement(element, 60, true);

}

/**
 * Dynamic wait for presence of element in DOM overloaded Method
 */
public static Statuss fluentWaitForPresenceOfElement(By element, int durationInSeconds, boolean allowException) {
	Statuss Statuss = new Statuss(true);

	try {
		Wait<WebDriver> fluentWait_seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
				.withTimeout(Duration.ofSeconds(durationInSeconds)).pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);
		fluentWait_seconds.until(ExpectedConditions.presenceOfElementLocated(element));
	} catch (NoSuchElementException e) {
		if (allowException) {
			Statuss.setStatus(false);
			Statuss.setError("Error while waiting for presence of element in path: '" + element + "' Details: "
					+ e.getMessage());
			Assert.fail("Error while waiting for presence of element in path: '" + element + "' Details: "
					+ e.getMessage());
		} else {
			Statuss.setStatus(false);
		}
	} catch (Exception e) {
		if (allowException) {
			Statuss.setStatus(false);
			Statuss.setError("Error while waiting for presence of element in path: '" + element + "' Details: "
					+ e.getMessage());
			Assert.fail("Error while waiting for presence of element in path: '" + element + "' Details: "
					+ e.getMessage());
		} else {
			Statuss.setStatus(false);
		}
	}
	return Statuss;
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
	Statuss Statuss = new Statuss(true);
	String attributeValue = "";
	try {
		startTimer();
		fluentWaitForPresenceOfElement(xpath, 60, allowException);
		attributeValue = DriverFactory.getDriver().findElement(xpath).getAttribute(attributeName);
		endTimer();
		Reporter.log("Retrieved " + fieldName + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		Statuss.setStatus(true);
	} catch (NoSuchElementException e) {
		if (allowException) {
			Statuss.setStatus(false);
			endTimer();
			Assert.fail("Error in retrieving the attribute value of xpath :" + xpath + " Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			Statuss.setStatus(false);
			endTimer();
			Statuss.setError("Error in retrieving the attribute value of xpath :" + xpath + " Details: "
					+ e.getMessage() + "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		}
	} catch (Exception e) {
		if (allowException) {
			Statuss.setStatus(false);
			endTimer();
			Assert.fail("Error in retrieving the attribute value of xpath :" + xpath + " Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			Statuss.setStatus(false);
			endTimer();
			Statuss.setError("Error in retrieving the attribute value of xpath :" + xpath + " Details: "
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
	ElementUtil.threadSleep(10000);
}

/**
 * Dynamic wait for invisibility of an element. By default wait duration is set
 * at 7 secs
 */
public static void fluentWaitForInvisibilityOfLoadingIcon() {
	Statuss Statuss = new Statuss(true);
	By element = getElementIdentifierFromJson("LoadingIcon", "xpath");

	try {
		Wait<WebDriver> fluentWait_3seconds = new FluentWait<WebDriver>(DriverFactory.getDriver())
				.withTimeout(Duration.ofSeconds(3)).pollingEvery(Duration.ofMillis(100))
				.ignoring(NoSuchElementException.class);
		fluentWait_3seconds.until(ExpectedConditions.attributeToBe(element, "class", "loading"));
		fluentWait_3seconds.until(ExpectedConditions.attributeToBe(element, "class", ""));
	} catch (Exception e) {
		Statuss.setStatus(true);
	}
}

/**
 * Dynamic wait for invisibility of an element. By default wait duration is set
 * at 7 secs
 */
public static void waitForLoadingToComplete() {
	Statuss Statuss = new Statuss(true);
	// By element = getElementIdentifierFromJson("LoadingIcon", "xpath");
	By copyRightText = getElementIdentifierFromJson("CopyrightInfoLbl", "xpath");
	ElementUtil.threadSleep(1000);
	// fluentWaitForClickable(copyRightText, 5, false);
	int counter = 0;
	while (!checkClickability(copyRightText, "Copy right text") & counter < 15) {
		ElementUtil.threadSleep(500);
		++counter;
	}
	try {

	} catch (Exception e) {
		Statuss.setStatus(false);
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
 * Fill value in a text box, after clearing its content
 */
public static Statuss clearAndFillValue(By fieldSelector, String fieldValue, String fieldName) {
	return clearAndFillValue(fieldSelector, fieldValue, fieldName, true);
}

/**
 * Fill value in a text box, after clearing its content Overloaded Method
 */
public static Statuss clearAndFillValue(By fieldSelector, String fieldValue, String fieldName,
		boolean allowException) {
	Statuss status = new Statuss(true);
	try {
		startTimer();
		fluentWaitForVisibility(fieldSelector, 5, allowException);
		fluentWaitForClickable(fieldSelector, 5, allowException);
		WebElement element = DriverFactory.getDriver().findElement(fieldSelector);
		element.clear();
		element.sendKeys(fieldValue);
		endTimer();
		Reporter.log("Entered " + fieldName + " as " + fieldValue + "<br></br>Time elapsed- " + getElapsedTime()
				+ "<br></br>");
	} catch (NoSuchElementException e) {
		if (allowException) {
			status.setStatus(false);
			endTimer();
			status.setError("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage());
			Assert.fail("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			status.setStatus(false);
		}
	} catch (Exception e) {
		if (allowException) {
			status.setStatus(false);
			endTimer();
			status.setError("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage());
			Assert.fail("Error in clearing and filling: " + fieldName + ". Details: " + e.getMessage()
					+ "<br></br>Time elapsed- " + getElapsedTime() + "<br></br>");
		} else {
			status.setStatus(false);
		}
	}
	return status;
}

	
}	

