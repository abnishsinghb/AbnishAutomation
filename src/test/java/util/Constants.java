package util;

import java.io.File;
import java.util.Map;

/**
 * Framework-wide constants: file locations for configuration and the
	 * object-repository, plus the in-memory locator map populated by
	 * ConfigReader.initializeJsonFile() at suite start-up.
	 */
public class Constants {

    public static Map<String, Object> map = null;

    public static final String PROPERTIES_FILE_LOCATION =
	            System.getProperty("user.dir") + File.separator + "config.properties";

    public static final String OBJECTREPOSITORY_FILE_LOCATION =
	            System.getProperty("user.dir") + File.separator + "ObjectRepository.json";

    public static final String INPUJSON_FILE_LOCATION =
	            System.getProperty("user.dir") + File.separator + "test-inputJsonFiles" + File.separator;
}
