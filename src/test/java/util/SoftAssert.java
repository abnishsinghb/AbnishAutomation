package util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Reporter;
import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Maps;
import setup.DriverFactory;

/**
 * When an assertion fails, don't throw an exception but record the failure.
 * Calling {@code assertAll()} will cause an exception to be thrown if at least
 * one assertion failed.
 */

public class SoftAssert extends Assertion {
	
    /**
     * LinkedHashMap to preserve the order
     */
    private final Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();
    private String assertMessage = null;
    
    /**
     * Below method with catch the assertion failures and will add them in a map and captures the screenshot of the screen(when failure occurs)
     */
    
    @Override
    protected void doAssert(IAssert<?> a) {
        onBeforeAssert(a);
        try {
        	System.setProperty("org.uncommons.reportng.escape-output", "false"); 
            assertMessage = a.getMessage();
            a.doAssert();
            onAssertSuccess(a);
            Reporter.log(assertMessage +"<br></br>");
        } catch (AssertionError ex) {
            onAssertFailure(a, ex);
            m_errors.put(ex, a);
            saveScreenshot(assertMessage,ex);
        } finally {
            onAfterAssert(a);
        }
    }

	/**
	 * Below method will be called at the end of test class in teardown method to capture and throw all assertion failures
	 */
	
    public void assertAll() {
        if (!m_errors.isEmpty()) {
            StringBuilder sb = new StringBuilder("The following asserts failed:");
            boolean first = true;
            for (Map.Entry<AssertionError, IAssert<?>> ae : m_errors.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(",");
                }
                sb.append("\n\t");
                sb.append(ae.getKey().getMessage());
            }
            throw new AssertionError(sb.toString());
        }
    }

	/**
	 * Captures the screenshot of the screen(when assertion failure occurs). Complements doAssert() method
	 */
	
    public void saveScreenshot(String assertMessage, AssertionError ex) {    	    
    	Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
            File scrFile = ((TakesScreenshot)DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
            try {
            	String reportDirectory ="./target/surefire-reports/html/";
            	File destFile = new File((String) reportDirectory +assertMessage+"_"+formater.format(calendar.getTime())+".png");
            	File link = new File((String) assertMessage+"_"+formater.format(calendar.getTime())+".png");
                FileUtils.copyFile(scrFile, destFile); 
                Reporter.log("<b>" + ex + "</b>");
                Reporter.log("<a href='"+ link + "'> <img src='"+ link + "' height='100' width='100'/> </a><br></br>");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}