package stepDefnition;
import static io.restassured.RestAssured.*;


import java.io.IOException;

import org.junit.AfterClass;

import api.ApiResource;
import api.TestDataBuild;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pages.LoginPage;
import setup.DriverFactory;
import util.ElementUtil;
import util.SoftAssert;

public class AppPlaceApi extends DriverFactory{
	RequestSpecification res;
	ResponseSpecification resspec;
	Response response;
	TestDataBuild data =new TestDataBuild();
	static String place_id;
	
	SoftAssert softassert = new SoftAssert();
			
	@Given("Add Place Payload with {string}  {string} {string}")
	public void add_Place_Payload_with(String name, String language, String address) {
		try {
			res = given().spec(ElementUtil.requestSpecification()).body(data.addPlacePayLoad(name, language, address));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		@When("user calls {string} with {string} http request")
		public void user_calls_with_http_request(String resource, String method)  {

			ApiResource resourceAPI=ApiResource.valueOf(resource);
			
			resspec =new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
			
			if(method.equalsIgnoreCase("POST")) {
			 response =res.when().post(resourceAPI.getResource());
			}else if(method.equalsIgnoreCase("GET")) {
				 response =res.when().get(resourceAPI.getResource());    
		}
	}

		@Then("the API call got success with status code {int}")
		public void the_api_call_got_success_with_status_code(Integer statuscode) {
			
			 softassert.assertEquals((int) response.getStatusCode(), (int) statuscode,"Status code matched sucessfully");

		}

		@Then("{string} in response body is {string}")
		public void in_response_body_is(String keyValue, String Expectedvalue) {
		    // Write code here that turns the phrase above into concrete actions
		  
			softassert.assertEquals((String) ElementUtil.getJsonPath(response, keyValue), (String) Expectedvalue,"Respounse matched sucessfully");
			
			
		}

		@Then("verify place_Id created maps to {string} using {string}")
		public void verify_place_Id_created_maps_to_using(String expectedName, String resource) throws IOException {		
		   // requestSpec
		     place_id=ElementUtil.getJsonPath(response,"place_id");
			 res=given().spec(ElementUtil.requestSpecification()).queryParam("place_id",place_id);
			 user_calls_with_http_request(resource,"GET");
			 String actualName=ElementUtil.getJsonPath(response,"name");
			 
			 softassert.assertEquals((String) actualName, (String) expectedName,"Name matched sucessfully");

		}
		@Given("DeletePlace Payload")
		public void deleteplace_Payload() throws IOException {
		    // Write code here that turns the phrase above into concrete actions	   
			res =given().spec(ElementUtil.requestSpecification()).body(data.deletePlacePayload(place_id));
			softassert.assertAll();
			
		}

		
		

}
