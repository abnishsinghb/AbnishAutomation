package setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import util.Constants;
import util.ElementUtil;

public class ConfigReader {

	private Properties prop;

	/**
	 * This method is used to load the properties from config.properties file
	 * @return it returns Properties prop object
	 */
	public Properties init_prop() {

		prop = new Properties();
		try {
			FileInputStream ip = new FileInputStream(Constants.PROPERTIES_FILE_LOCATION);
			prop.load(ip);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return prop;

	}
	/**
	 * Load element locators from JSON to Hash map in contants.java class, before test Suite is started.
	 */
	
	public void initializeJsonFile() {
		/*
		 * Initialize testData.xlsx excel file
		 */
		try {
		    File jsonfile = new File(Constants.OBJECTREPOSITORY_FILE_LOCATION);
		    InputStream is; 
			is = new FileInputStream(jsonfile);
			String jsonTxt;
			jsonTxt = IOUtils.toString(is, "UTF-8");
			JSONObject json = new JSONObject(jsonTxt);  
			Constants.map = ElementUtil.jsonToMap(json);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
