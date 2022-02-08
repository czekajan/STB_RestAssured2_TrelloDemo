package e2e;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class moveCardBetweenLists extends BaseTest {

    private static String boardId;
    private static String firstListId;
    private static String secondListId;
    private static String cardId;


    @Test
    @Order(1)
    public void createNewBoard() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My e2ee board")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + "/" +BOARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo("My e2ee board");

        boardId = json.getString("id");
    }

    @Test
    @Order(2)
    public void createFirstList(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "First list")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + "/" + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo("First list");

        firstListId = json.getString("id");
    }

    @Test
    @Order(3)
    public void createSecondList(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Second list")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + "/" + LISTS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo("Second list");

        secondListId = json.getString("id");
    }

    @Test
    @Order(4)
    public void addCartToFirstList(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My e2e card")
                .queryParam("idList", firstListId)
                .when()
                .post(BASE_URL + "/" + CARDS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo("My e2e card");

        cardId = json.getString("id");
    }

    @Test
    @Order(5)
    public void moveCardToTheSecondList(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("idList", secondListId)
                .when()
                .put(BASE_URL + "/" + CARDS + "/" + cardId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("idList")).isEqualTo(secondListId);

    }

    @Test
    @Order(6)
    public void deleteBoard(){

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + BOARDS + "/" + boardId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}
