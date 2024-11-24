package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ApiTests extends TestBase {
    @Test
    void successfulGetListOfUserTest() {
        given()

                .when()
                .get("/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200);
    }

    @Test
    void singleUserNotFoundTest() {
        given()

                .when()
                .log().uri()
                .get("https://reqres.in/api/users/23")

                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

    @Test
    void getUsersListTest() {
        given()

                .when()
                .log().uri()
                .get("/users?page=2")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data[0].last_name", is("Lawson"))
                .body("data.find { it.id == 9 }.first_name", is("Tobias"));
    }

    @Test
    void createUserTest() {
        String data = "{\"name\": \"alex\",\"job\": \"qa engineer\"}";

        given()
                .body(data)
                .contentType(JSON)

                .when()
                .log().uri()
                .post("/users")

                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("alex"))
                .body("job", is("qa engineer"));
    }

    @Test
    void successfulRegistrationTest() {
        String data = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\"}";

        given()
                .body(data)
                .contentType(JSON)

                .when()
                .log().uri()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void unsuccessfulRegistrationMissingPasswordTest() {
        String data = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"\"}";

        given()
                .body(data)
                .contentType(JSON)

                .when()
                .log().uri()
                .post("/register")

                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}



