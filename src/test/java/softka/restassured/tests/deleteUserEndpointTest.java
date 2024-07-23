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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;


@Feature("Validar la funcionalidad eliminar")
public class deleteUserEndpointTest extends BaseTest {
    String url = "/users";

    @Test(description = "Eliminar un usuario del Sistema")
    @Description("Eliminar un usuario del Sistema")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteUser() {
        RestAssured.given()
                .filter(new RestAssuredListener())
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .delete(url +"/{id}", 2)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Test(description = "Eliminar un usuario del Sistema y verificar que se haya eliminado")
    @Description("Eliminar un usuario del Sistema y verificar que se haya eliminado")
    @Severity(SeverityLevel.CRITICAL)
    public void deleteUserAndVerify() {
        //Listar todos los usuarios y obtener el id a eliminar
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

        // Eliminar usuario
        RestAssured.given()
                .filter(new RestAssuredListener())
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .delete(url +"/{id}", id)
                .then()
                .assertThat()
                .statusCode(204);

            // Verificar que el usuario este eliminado
        RestAssured.given()
                .filter(new RestAssuredListener())
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .when()
                .get(url +"/{id}", id)
                .then()
                .assertThat()
                .statusCode(200)
                .body("data.id", not(hasItem(id)));;
    }
}
