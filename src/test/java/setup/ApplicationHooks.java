package setup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Properties;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import com.aventstack.extentreports.gherkin.model.ScenarioOutline;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;


public class ApplicationHooks extends DriverFactory {

	private DriverFactory driverFactory;
	private WebDriver driver;
	private ConfigReader configReader;
	
	Properties prop;

	@Before(order = 0)
	public void getProperty() {
		
		configReader = new ConfigReader();
		configReader.initializeJsonFile();
		prop = configReader.init_prop();
		
	}
	
	@Before(order = 1)
	public void launchBrowser() {
		String browserName = prop.getProperty("browser");
		String browserview = prop.getProperty("browserview");
		driverFactory = new DriverFactory();
		driver = driverFactory.init_driver(browserName,browserview);
	
	}

	@AfterStep
	public void actionPostEachStep(Scenario scenario) {
        LocalDateTime now = LocalDateTime.now(); // Get current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);
		String stepNumber = Integer.toString(scenario.getLine()); // Get the line number (step index)
		String fileName = formattedTime+".png";
		byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		scenario.attach(sourcePath, "image/png", fileName);
	}
	
	@After(order = 0)
	public void quitBrowser() {
		driver.quit();
		
	}
	@After(order = 1)
	public void screenShot(Scenario scenario) {
		if (scenario.isFailed()) {
			String screenshotName = scenario.getName().replaceAll(" ", "_");
	    	byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			scenario.attach(sourcePath, "image/png", screenshotName);
		}
		
		
	}
}
