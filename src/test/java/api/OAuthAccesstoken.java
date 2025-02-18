package api;

import static io.restassured.RestAssured.*;


import java.util.List;

import com.google.inject.spi.Element;

import apiPojo.Api;
import apiPojo.GetCourcess;
import apiPojo.webAutomation;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import util.ElementUtil;

public class OAuthAccesstoken {
	
	public static void oauthApi() {
		
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
	String respounce =	given()
		.formParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.formParam("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.formParam("grant_type","client_credentials")
		.formParam("scope","trust")
		.when()
		.post("/oauthapi/oauth2/resourceOwner/token")
        .then().log().all().assertThat().statusCode(200)
        .extract().response().asString();
		
	              
	        JsonPath js = ElementUtil.rawJson(respounce);
	     String accessToken =   js.get("access_token");
	     
	     GetCourcess gc = given()
	     .param("access_token", accessToken)
	     .when()
	     .get("/oauthapi/getCourseDetails").as(GetCourcess.class);

	     //get the price
			List<Api> getApi = gc.getCourses().getApi();

			for (int i = 0; i < getApi.size(); i++) {
				if (getApi.get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java")) {
					System.out.println(getApi.get(i).getPrice());
				}
			}
			
			// get the api title
			for (int i = 1; i < getApi.size(); i++) {	
			System.out.println(getApi.get(i).getCourseTitle());
		}
			
		   List<webAutomation>   getWebautomation =  gc.getCourses().getWebAutomation();
		   
		  for(int i=1; i < getWebautomation.size(); i++) {
			  
			 System.out.println(getWebautomation.get(i).getCourseTitle());
		  }
		   
	}
}
