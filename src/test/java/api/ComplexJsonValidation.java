package api;

import java.util.HashMap;
import java.util.HashSet;

import org.testng.Assert;

import io.restassured.path.json.JsonPath;
import util.ElementUtil;

public class ComplexJsonValidation {
	
	static int courceSize = 0;
	static int purchaseAmount = 0;
	static String firstCourceTitle = "";
	static String courceTitle = "";
	static int courcePrice = 0;
	static int copies  = 0;
	
	public static void noOfCourses(String complexJson) {
		 JsonPath js = ElementUtil.rawJson(complexJson);
		 courceSize = js.get("courses.size()");
		 Assert.assertEquals(courceSize, 3);	
	}
	
	public static void purchaseAmount(String complexJson) {
		 JsonPath js = ElementUtil.rawJson(complexJson);
		 purchaseAmount =    ElementUtil.rawJson(complexJson).get("dashboard.purchaseAmount");
		 Assert.assertEquals(purchaseAmount, 910);	
	}
	
	public static void titleOfTheFirstCourse(String complexJson) {
		 JsonPath js = ElementUtil.rawJson(complexJson);
		 firstCourceTitle =    js.get("courses[0].title");
		 Assert.assertEquals(firstCourceTitle, "Selenium Python");	
	}
	
	public static void courseTitlesAndRespectivePrices(String complexJson) {
		 HashMap<String, Integer> titlesAndRespectivePrices = new HashMap<>();
		JsonPath js = ElementUtil.rawJson(complexJson);
		for(int i=0; i<courceSize; i++) {
		 courceTitle =  js.get(("courses["+i+"].title"));
		 courcePrice =  js.getInt(("courses["+i+"].price"));
		 titlesAndRespectivePrices.put(courceTitle, courcePrice);		 
		}
		System.out.println(titlesAndRespectivePrices);
	}
	
	public static void noOfCopiesSoldByRPACourse(String complexJson) {
		 JsonPath js = ElementUtil.rawJson(complexJson);
		 
		 for(int i=0; i<courceSize; i++) {
		 firstCourceTitle =  js.get("courses["+i+"].title");
		 if(firstCourceTitle.equalsIgnoreCase("RPA")) {
		 copies = js.getInt(("courses["+i+"].copies")); 
			 break;
		 }
	 }
		 Assert.assertEquals(copies, 10);
	}
	
	public static void allCoursepricesMatchesWithPurchaseAmount(String complexJson) {
		int amount = 0;
		int sum = 0;
		
		 JsonPath js = ElementUtil.rawJson(complexJson);
		 for(int i=0; i<courceSize; i++) {
		  courcePrice =  js.getInt(("courses["+i+"].price"));
		  copies =  js.getInt(("courses["+i+"].copies")); 
			amount = courcePrice * copies;
			sum = sum + amount;		
		 } 
		 
		 Assert.assertEquals(purchaseAmount, sum);
	}
	
	
	
}
