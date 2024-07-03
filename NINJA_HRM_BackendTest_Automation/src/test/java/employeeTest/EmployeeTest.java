package employeeTest;

import  static io.restassured.RestAssured.*;

import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import baseClass.BaseAPIClass;
import endPoints.IEndPoints;
import pojoUtility.EmployeePOJO;
import pojoUtility.ProjectPojo;


public class EmployeeTest extends BaseAPIClass{
	
	@Test
	public void addEmployeeTest() throws Throwable {
		
		String projectName = "Mark-"+ jLib.getRandomNumber();
		String userName = "Tony_" + jLib.getRandomNumber();
		// create an object for POJO class
		// API-1 Add Project
		ProjectPojo pObj = new ProjectPojo(projectName, "TonyStarc", "created", 1);
		
	                given()
	                    .spec(specReqObj)
	                    .body(pObj)
	                .when()
	                    .post(IEndPoints.ADD_PROJ)
	                .then()
	                    .spec(specResObj)
	                    .log().all();
			              
	    // Add employee to same project              
		EmployeePOJO empObj = new EmployeePOJO("Engineer", "02/02/1995", userName + "@gmail.com", userName, 5,
				                                "9856321472", projectName, "ROLE_EMPLOYEE", userName);
		
                      given()
                          .spec(specReqObj)
                          .body(empObj)
                      .when()
                          .post(IEndPoints.ADD_EMP)
                      .then()
                          .assertThat().statusCode(201)
                          .and()
     			          .time(Matchers.lessThan(3000L))
                          .spec(specResObj)
                          .log().all();
                      
       // verify the employee in DB Layer
         boolean flag = dbLib.executeQueryVerifyAndGetData("select * from employee", 5, userName);
         Assert.assertTrue(flag, "employee in DB is not verified");
	}
	
	@Test
	public void addEmployeWithOutEmail() throws Throwable {
		
		String projectName = "Mark-"+jLib.getRandomNumber();
		String userName = "Tony_" + jLib.getRandomNumber();
		// create an object for POJO class
		// API-1 Add Project
		ProjectPojo pObj = new ProjectPojo(projectName, "TonyStarc", "created", 1);
		
	                given()
	                    .spec(specReqObj)
	                    .body(pObj)
	                .when()
	                    .post(IEndPoints.ADD_PROJ);
			              
	    // Add employee to same project              
		EmployeePOJO empObj = new EmployeePOJO("Engineer", "02/02/1995", "", userName, 5,
				                                "9856321472", projectName, "ROLE_EMPLOYEE", userName);
		
                      given()
                          .spec(specReqObj)
                          .body(empObj)
                      .when()
                          .post(IEndPoints.ADD_EMP)
                      .then()
                          .assertThat().statusCode(500)
                          .spec(specResObj)
                          .log().all();
		
	}
}
