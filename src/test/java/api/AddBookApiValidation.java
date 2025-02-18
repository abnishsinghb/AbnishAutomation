package api;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import util.ElementUtil;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AddBookApiValidation {

	public static void addBookApi(String isbn, String aisle) {
		
		RestAssured.baseURI = "http://216.10.245.166";
		
		//Add book
		String respounce = given().log().all()
		.header("Content-Type","application/json")
		.body(Payload.addBook(isbn,aisle))
		.when()
		.post("/Library/Addbook.php")
		.then().log().all()
		.assertThat().statusCode(200).body("Msg", equalTo("successfully added")).extract().response().asString();
		
		
		
		//Get Id		
		 JsonPath js=  ElementUtil.rawJson(respounce);
		String Id = js.get("ID");
		
		//Get Respounce
		
		given().log().all()
		.param("ID", Id)
		.header("Content-Type","application/json")
		.when()
		.get("/Library/GetBook.php")
		.then().log().all()
		.assertThat().statusCode(200);
		 
		//Delete Respounce
		
		given().log().all()
		.header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"ID\": \""+Id+"\"    \r\n"
				+ "}")
		.when()
		.post("/Library/DeleteBook.php")
		.then().log().all()
		.assertThat().statusCode(200).body("msg", equalTo("book is successfully deleted"));

	}	
}
