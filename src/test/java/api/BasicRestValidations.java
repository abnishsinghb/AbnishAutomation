package api;

import io.restassured.RestAssured;

import io.restassured.path.json.JsonPath;
import util.Constants;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;



public class BasicRestValidations {

    public static void checkApi() throws IOException {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        // Add Place
        String response = given()
            .queryParam("Key", "qaclick123")
            .header("Content-Type", "application/json")
            .body(new String(Files.readAllBytes(Paths.get(Constants.INPUJSON_FILE_LOCATION+"jsonTestData.json")))).log().all()
        .when()
            .post("/maps/api/place/add/json")
        .then()
            .log().all()  // Log response details
            .assertThat().statusCode(200)
            .body("scope", equalTo("APP"))
            .header("Server", "Apache/2.4.52 (Ubuntu)")
            .extract().response().asString();

        JsonPath js = new JsonPath(response);
        String placeId = js.getString("place_id");


        // Update Place
        String newAddress = "winter walk, USA";
        given()
            .queryParam("Key", "qaclick123")
            .header("Content-Type", "application/json")
            .body("{\r\n"
                + "\"place_id\":\"" + placeId + "\",\r\n"
                + "\"address\":\"" + newAddress + "\",\r\n"
                + "\"key\":\"qaclick123\"\r\n"
                + "}")
            .log().all()  // Log request details
        .when()
            .put("/maps/api/place/update/json")
        .then()
            .log().all()  // Log response details
            .assertThat().statusCode(200)
            .body("msg", equalTo("Address successfully updated"));

        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        System.out.println("getplace id :-" +placeId);
        
     given()
        .queryParam("Key", "qaclick123")
        .queryParam("place_id", placeId)
        .log().all()
    .when()
        .get("/maps/api/place/get/json")
    .then().log().all()
        .statusCode(200);
        
        


    }
}