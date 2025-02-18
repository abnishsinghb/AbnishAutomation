package api;


import static io.restassured.RestAssured.*;


import static org.hamcrest.Matchers.*;

import java.io.File;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import util.ElementUtil;

public class BugCreation {
	
	
	public static void CreateBug() {
		
		RestAssured.baseURI = "https://arunmanasu199465.atlassian.net";
		
	String resounce =	given().log().all()
		.header("Content-Type", "application/json")
		.header("Authorization", "Basic YXJ1bm1hbmFzdTE5OTQ2NUBnbWFpbC5jb206QVRBVFQzeEZmR0YwYnlramlZV3Niall1cC1PUkU1dWN4MmpSRENiSkdCSkNTTjJKV1FmZDFrSGpoeDFaa1YxUlFUN05FYlIyTE1LNUtRc1F4R1FoRWlOTkViUC1jNGNPUl95bmhUcVE2MUVvU1FDSnh0VUp4SlQ3aERzSU8tLWxnazN4dE5WRW1vT1I1NURCZ0dDcG50WkRacWhwcUNYQjV1UzRxSFZXLXBWYUx1eXhqTDZna2RjPTE5MjA3MUY3")
		.body(Payload.CreatetingBug())
		.when()
		.post("/rest/api/3/issue")
		.then().log().all().assertThat().statusCode(201).extract().response().asString();

		 JsonPath js = ElementUtil.rawJson(resounce);
		 String issueId = js.getString("id");
		
		given().log().all()
		.header("Content-Type", "multipart/form-data")
		.header("X-Atlassian-Token","nocheck")
		.header("Authorization", "Basic YXJ1bm1hbmFzdTE5OTQ2NUBnbWFpbC5jb206QVRBVFQzeEZmR0YwYnlramlZV3Niall1cC1PUkU1dWN4MmpSRENiSkdCSkNTTjJKV1FmZDFrSGpoeDFaa1YxUlFUN05FYlIyTE1LNUtRc1F4R1FoRWlOTkViUC1jNGNPUl95bmhUcVE2MUVvU1FDSnh0VUp4SlQ3aERzSU8tLWxnazN4dE5WRW1vT1I1NURCZ0dDcG50WkRacWhwcUNYQjV1UzRxSFZXLXBWYUx1eXhqTDZna2RjPTE5MjA3MUY3")
		.multiPart("file", new File("C:\\Users\\DELL\\Downloads\\Untitled.png"))
		.when()
		.post("/rest/api/3/issue/"+issueId+"/attachments")
		.then().log().all().assertThat().statusCode(200);
	}

}
