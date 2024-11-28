package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;

/**
 * Utility methods for complete suite are included in this class. Eg. click,
 * clear and fill, drag and drop, fluent waits and getting environment
 * variables.
 */
public class ElementUtil {

	static Map<String, Object> map = new HashMap<String, Object>();

	/**
	 * Returns By locator of a web element by reading it from hash map(in
	 * constants.java file)
	 */
	public static By getElementIdentifierFromJson(String elementName, String identifierType) {
		By by = null;
		
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

}
