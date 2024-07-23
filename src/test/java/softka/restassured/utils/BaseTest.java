package softka.restassured.utils;

import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BaseTest {
	
	private static final Logger logger = LogManager.getLogger();
	
	@BeforeMethod
	public void beforeMethod() {
		//RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	@AfterMethod
	public void afterMethod(ITestResult result) {
		if(result.getStatus() == ITestResult.FAILURE) {
			Throwable t = result.getThrowable();
			StringWriter error = new StringWriter();
			t.printStackTrace(new PrintWriter(error));
			logger.info(error.toString());
		}
		
	}
	@BeforeClass
	public  void setup() {

        RestAssured.baseURI = "https://reqres.in/api";
	}

	@BeforeTest
	public void BeforeTest(){
		logger.info("Test execution started...");
	}

	@AfterTest
	public void AfterTest(){
		logger.info("Test execution finished...");
	}
}
