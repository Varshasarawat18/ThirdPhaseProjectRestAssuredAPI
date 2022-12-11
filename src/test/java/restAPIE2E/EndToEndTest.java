package restAPIE2E;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {
	
	Response response;
	RequestSpecification request;
	String BaseURI="http://localhost:3000";
	Map<String ,Object> mapobj;
	int EmployeeID;
	
	@Test
	public void test1()
	{
		
		//fetching all employee
		System.out.println("All employees are:");
		response=getAllEmployee();
		System.out.println(response.getBody().asString());
		Assert.assertEquals(200, response.statusCode());
		
		//New employee creating and fetching the id or newly created employee
		System.out.println("New employee is");
		response= createEmployee("John","12000");
		System.out.println(response.getBody().asString());
		Assert.assertEquals(201, response.statusCode());
		JsonPath jpath = response.jsonPath();
		int id= jpath.get("id");

		//Get single employee and Validate name is John and status code 200
		System.out.println("Getting single employee and name is John ");
		response = getSingleEmployee(id);
		System.out.println("Single employee is" + response.getBody().asString());
		JsonPath jpath1=response.jsonPath();
		String name =jpath1.get("name");
		Assert.assertEquals(name,"John");
		Assert.assertEquals(200, response.getStatusCode());
	
		//Update employee and validate status 
		System.out.println("Updating employee name john as smith");
		response= UpdateEmployee(id,"Smith","12000");
		System.out.println("Updated employee name is:" +response.getBody().asString());
		Assert.assertEquals(200,response.statusCode());
		
		//Delete employee and validate status code 
		System.out.println("Deleting employee created");
		response = deleteEmployee(id);
		Assert.assertEquals(200, response.statusCode());
		
		//call the deleted employee and validate status code is 404
		response= getSingleEmployee(id);
		Assert.assertEquals(404, response.statusCode());
		
	
	}
	
	
	public Response getAllEmployee()
{
		
	RestAssured.baseURI=this.BaseURI;
	request= RestAssured.given();
	Response response = request.get("employees");
	return response;
	
}
	public Response getSingleEmployee(int empID)
	{
		RestAssured.baseURI=this.BaseURI;
		request= RestAssured.given();
		response = request.get("employees/"+empID);
		return response;
	
	}
	
	public Response createEmployee(String name , String salary)
	{
		mapobj= new HashMap<String, Object>();
		mapobj.put("name",name);
		mapobj.put("Salary", salary);
		
		
		RestAssured.baseURI=this.BaseURI;
		request= RestAssured.given();
		Response response = request
									.contentType(ContentType.JSON)
									.accept(ContentType.JSON)
									.body(mapobj)
									.post("employees/create");
		return response;
	}
	public Response UpdateEmployee(int empID, String name , String salary)
	{
		mapobj= new HashMap<String, Object>();
		mapobj.put("Id", empID);
		mapobj.put("name",name);
		mapobj.put("Salary", salary);
		
		RestAssured.baseURI=this.BaseURI;
		 request = RestAssured.given();
		Response response = request
							.contentType(ContentType.JSON)
							.accept(ContentType.JSON)
							.put("employees/"+empID);
		return response;
		}
	
	
	public Response deleteEmployee(int empID)
	{
		
		RestAssured.baseURI=this.BaseURI;
		request = RestAssured.given();	
		
		response = request.delete("employees/" + empID);
		return response;
		
}

}
