package api;

import java.util.ArrayList;

import java.util.List;

import apiPojo.AddMapDetails;
import apiPojo.Location;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;

public class AddApiSerialization {

	public static void addPlaceApis() {
		// TODO Auto-generated method stub
		AddMapDetails p = new AddMapDetails();	
		p.setAccuracy(50);
		p.setName("Frontline ");
		p.setPhone_number("(+91) 983 893 3937");
		p.setAddress("29, side layout, cohen 09");
		p.setWebsite("http://google.com");
		p.setLanguage("English");
		
		List<String> myList = new ArrayList<String>();
		myList.add("Pline");
		myList.add("lie");
		myList.add("pro");
		p.setTypes(myList);
		
		Location lo = new Location();
		lo.setLat(-38.383494);
		lo.setLng(8.383494);
		p.setLocation(lo);
			    

		RequestSpecification requiestSpecific = new RequestSpecBuilder()
				            .setBaseUri("https://rahulshettyacademy.com")
                            .addQueryParam("Key", "qaclick123")
                            .setContentType(ContentType.JSON).build();
		ResponseSpecification respounceSpecific = new ResponseSpecBuilder()
				             .expectStatusCode(200)
				             .expectContentType(ContentType.JSON).build();
		
		
		RequestSpecification requiest  = given().spec(requiestSpecific).body(p);	
		Response respounce = requiest.when().post("maps/api/place/add/json").then().spec(respounceSpecific).extract().response();
    	String str = respounce.asString();
    	System.out.println(str);
		
	}

}
