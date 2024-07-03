package projectTest;

import static io.restassured.RestAssured.*;

import java.sql.SQLException;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import baseClass.BaseAPIClass;
import endPoints.IEndPoints;
import io.restassured.response.Response;
import pojoUtility.ProjectPojo;


public class ProjectTest extends BaseAPIClass {

	ProjectPojo pObj;
	String projectName;
	
	@Test
	public void addSingleProjectWithCreated() throws SQLException, Throwable {
		
		String expSuccMsg = "Successfully Added";
		projectName = "Mark-"+jLib.getRandomNumber();
		
		pObj = new ProjectPojo(projectName, "TonyStarc", "created", 1);
		
		Response resp = given()
		                    .spec(specReqObj)
		                    .body(pObj)
		                .when()
		                    .post(IEndPoints.ADD_PROJ);
		
		            resp.then()
		                    .assertThat().statusCode(201)
		                    .assertThat().time(Matchers.lessThan(3000L))
		                    .spec(specResObj)
		                    .log().all();
		                String actMsg = resp.jsonPath().get("msg");
		                Assert.assertEquals(expSuccMsg,actMsg);
		               
		// verify the projectName in DB Layer
		   dbLib.getDbConnection();
		   boolean flag = dbLib.executeQueryVerifyAndGetData("select * from project", 4, projectName);
		   Assert.assertTrue(flag, "project in DB is not verified");
		   dbLib.closeConnection();
	}
	
	// add new project with same name(Duplicate project)
	@Test(dependsOnMethods = "addSingleProjectWithCreated")
	public void addDuplicateProjectTest() throws Throwable {
		String expMsg = "The Project Name :"+projectName+" Already Exists";
		Response resp = given()
				           .spec(specReqObj)
						   .body(pObj)
						.when()
						   .post(IEndPoints.ADD_PROJ);
						resp.then()
						   .spec(specResObj)
						   .assertThat().statusCode(409)
						   .log().all();
						String actMsg = resp.jsonPath().get("message");
						Assert.assertEquals(expMsg, actMsg);
	}
}
