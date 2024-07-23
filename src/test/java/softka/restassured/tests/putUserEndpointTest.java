package softka.restassured.tests;

import com.jayway.jsonpath.DocumentContext;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static io.restassured.RestAssured.given;

@Feature("Validar la funcionalidad actualizar usuarios")
public class putUserEndpointTest extends BaseTest {
    String url = "/users";
    String file = new String(Files.readAllBytes(Path.of("src/test/resources/putapirequestbody.json")));
    DocumentContext json = JsonPath.parse(file);


    public putUserEndpointTest() throws IOException {
    }

    @Test(description = "Validar actualizar datos de un usuario")
    @Description("Validar actualizar datos de un usuario")
    @Severity(SeverityLevel.CRITICAL)
    //Obtener todos los usuarios del sistema
    public void putEndpoint() {
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
        JSONArray data = JsonPath.read(response.body().asString(), "$.data");
        int id= JsonPath.read(response.body().asString(), "$.data[0].id");
        String email = JsonPath.read(response.body().asString(), "$.data[0].email");
        String first_name = JsonPath.read(response.body().asString(), "$.data[0].first_name");
        String last_name = JsonPath.read(response.body().asString(), "$.data[0].last_name");


        // Obtener usuario por id
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

            // Actualizar datos del usuario
        RestAssured.given()
                .filter(new RestAssuredListener())
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(json.jsonString())
                .when()
                .put(url +"/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", Matchers.equalTo(JsonPath.read(json.jsonString(), "$.id")))
                .body("email", Matchers.equalTo(JsonPath.read(json.jsonString(), "$.email")))
                .body("first_name", Matchers.equalTo(JsonPath.read(json.jsonString(), "$.first_name")))
                .body("last_name", Matchers.equalTo(JsonPath.read(json.jsonString(), "$.last_name")));

    }

    }

