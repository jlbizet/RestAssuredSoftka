package softka.restassured.tests;

import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import softka.restassured.listener.RestAssuredListener;
import softka.restassured.utils.BaseTest;


import static io.restassured.RestAssured.given;

@Feature("Validar la funcionalidad obtener usuarios")
public class getUserEndpointTest extends BaseTest {

    String url = "/users";

    @Test(description = "Obtener todos los usuarios")
    @Description("Obtener todos los usuarios")
    @Severity(SeverityLevel.CRITICAL)
    public void getAllUsers() {

        Response response = given()
                .filter(new RestAssuredListener())
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .get(url);
        response.then()
                .assertThat()
                .statusCode(200)
                .body("data", Matchers.notNullValue());
    }

    @Test(description = "Obtener usuario por id")
    @Description("Obtener usaurio por id")
    @Severity(SeverityLevel.CRITICAL)
    public void getUsersByID() {
        Response response = given()
                .filter(new RestAssuredListener())
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .get(url);
        response.then()
                .assertThat()
                .statusCode(200)
                .body("data", Matchers.notNullValue());
        int id= JsonPath.read(response.body().asString(), "$.data[0].id");
        String email = JsonPath.read(response.body().asString(), "$.data[0].email");
        String first_name = JsonPath.read(response.body().asString(), "$.data[0].first_name");
        String last_name = JsonPath.read(response.body().asString(), "$.data[0].last_name");

        // get by id
        RestAssured.given()
                .filter(new RestAssuredListener())
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .get(url +"/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data.id", Matchers.equalTo(id))
                .body("data.email", Matchers.equalTo(email))
                .body("data.first_name", Matchers.equalTo(first_name))
                .body("data.last_name", Matchers.equalTo(last_name));

    }


    @Test(description = "Validar que un usuario no exista en el sistema")
    @Description("Validar que un usuario no exista en el sistema")
    @Severity(SeverityLevel.CRITICAL)
    public void getUserIdNotExits(){
        RestAssured.given()
                .filter(new RestAssuredListener())
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .get(url +"/{id}", 50)
                .then()
                .assertThat()
                .statusCode(404)
                .body(Matchers.equalTo("{}"));
    }

    }

