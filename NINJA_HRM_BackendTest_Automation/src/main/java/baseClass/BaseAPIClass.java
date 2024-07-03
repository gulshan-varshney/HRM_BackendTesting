package baseClass;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import genericUtility.DataBaseUtility;
import genericUtility.FileUtility;
import genericUtility.JavaUtility;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class BaseAPIClass {

	public JavaUtility jLib = new JavaUtility();
	public FileUtility fLib = new FileUtility();
	public DataBaseUtility dbLib = new DataBaseUtility();
	public static RequestSpecification specReqObj;
	public static ResponseSpecification specResObj;

	@BeforeSuite
	public void configBS() throws Throwable {
		dbLib.getDbConnection();
		System.out.println("=====Connected to DB=====");

		RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
		reqBuilder.setContentType(ContentType.JSON);
		reqBuilder.setBaseUri(fLib.getDataFromPropertyFile("BaseURI"));
		specReqObj = reqBuilder.build();

		ResponseSpecBuilder resbuilder = new ResponseSpecBuilder();
		resbuilder.expectContentType(ContentType.JSON);
		specResObj = resbuilder.build();
	}

	@AfterSuite
	public void configAS() throws Throwable {
		dbLib.closeConnection();
		System.out.println("=====Disconnected to DB=====");
	}

}
