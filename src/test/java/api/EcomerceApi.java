package api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import util.ElementUtil;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import apiPojo.EcomarceLoginApi;
import apiPojo.EcomarceLoginRespounse;
import apiPojo.OrderDetail;
import apiPojo.Orders;


public class EcomerceApi {
	
	public static void addEcomarceApi() {
	
		//login app
		EcomarceLoginApi EcomarceLoginApi = new EcomarceLoginApi();	
		EcomarceLoginApi.setUserEmail("arunmanasu199465@gmail.com");
		EcomarceLoginApi.setUserPassword("9901009611!Qwe");		
	RequestSpecification requiestSpecific = new RequestSpecBuilder()
			.setBaseUri("https://rahulshettyacademy.com")
			.setContentType(ContentType.JSON).build();		               	
	RequestSpecification requiest   =   given().spec(requiestSpecific).body(EcomarceLoginApi);
	EcomarceLoginRespounse res =	requiest.when().post("/api/ecom/auth/login")
			.then().extract().response().as(EcomarceLoginRespounse.class);
	String token = res.getToken();
	String userID = res.getUserId();
       //add product
	
	RequestSpecification createReqspecific = new RequestSpecBuilder()
			.setBaseUri("https://rahulshettyacademy.com").addHeader("authorization", token).build();

	RequestSpecification reqspeci = given().spec(createReqspecific)
	       .param("productName", "Laptop")
	       .param("productAddedBy", userID)
	       .param("productCategory", "fashion")
	       .param("productSubCategory", "shirts")
	       .param("productPrice", "11500")
	       .param("productDescription", "Addias Originals")
	       .param("productFor", "women")
	       .multiPart("productImage", new File("D:\\documents\\Laptop.jpg"));
	       
	 String ress =     reqspeci.when().post("/api/ecom/product/add-product").then().extract().response().asString();	             JsonPath pa =  ElementUtil.rawJson(ress);  
	      String productId =       pa.get("productId"); 
     
	  	//Create Order
	  	RequestSpecification createOrderBaseReq=	new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
	  			.addHeader("authorization", token).setContentType(ContentType.JSON)
	  			.build();
	  	OrderDetail orderDetail = new OrderDetail();
	  	orderDetail.setCountry("India");
	  	orderDetail.setProductOrderedId(productId);
	  	
	  	List<OrderDetail> orderDetailList = new ArrayList<OrderDetail> ();
	  	orderDetailList.add(orderDetail);	
	  	Orders orders = new Orders();
	  	orders.setOrders(orderDetailList);	  	
	  RequestSpecification createOrderReq=given().log().all().spec(createOrderBaseReq).body(orders);
	  String responseAddOrder = createOrderReq.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();
	  System.out.println(responseAddOrder);

	//Delete Product

	  RequestSpecification deleteProdBaseReq=	new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
	  .addHeader("authorization", token).setContentType(ContentType.JSON)
	  .build();

	  RequestSpecification deleteProdReq =given().log().all().spec(deleteProdBaseReq).pathParam("productId",productId);

	  String deleteProductResponse = deleteProdReq.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all().
	  extract().response().asString();

	  JsonPath js1 = new JsonPath(deleteProductResponse);

	  Assert.assertEquals("Product Deleted Successfully",js1.get("message"));
	 
	}
	
}
