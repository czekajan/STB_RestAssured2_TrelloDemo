package board;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class BoardTestAssertJ {

    private final String  key =  "KEY";
    private final String token = "TOKEN";


    @Test
    public void createNewBoard() {

        Response response = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("name", "My first board")
                .contentType(ContentType.JSON)
                .when()
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo("My first board");

        String boardId = json.get("id");

        given()
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .delete("https://api.trello.com/1/boards/" + boardId)
                .then()
                .statusCode(SC_OK);

    }

    @Test
    public void createBoardWitEmptyBoardName() {

        Response response = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("name", "")
                .contentType(ContentType.JSON)
                .when()
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();

    }

    @Test
    public void createBoardWithDefaultList(){

        Response response = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("name", "Board without default list")
                .queryParam("defaultLists", false)
                .contentType(ContentType.JSON)
                .when()
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.get("name")).isEqualTo("Board without default list");

        //System.out.println(response.prettyPrint());
        String boardId = json.get("id");

        Response responseGet = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.trello.com/1/boards/" + boardId + "/lists")
                .then()
                .statusCode(200)
                .extract()
                .response();

        //System.out.println(responseGet.asString());
        JsonPath jsonGet = responseGet.jsonPath();
        List<Object> idList = jsonGet.getList("id");

        Assertions.assertThat(jsonGet.getList("id")).hasSize(0);

        given()
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .delete("https://api.trello.com/1/boards/" + boardId)
                .then()
                .statusCode(SC_OK);
    }

    @Test
    public void createNewBoardWithLists() {

        Response response = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("name", "Board with default list")
                .queryParam("defaultLists", true)
                .contentType(ContentType.JSON)
                .when()
                .post("https://api.trello.com/1/boards/")
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getString("name")).isEqualTo("Board with default list");

        //System.out.println(response.prettyPrint());
        String boardId = json.get("id");

        Response responseGet = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .get("https://api.trello.com/1/boards/" + boardId + "/lists")
                .then()
                .statusCode(200)
                .extract()
                .response();


        JsonPath jsonGet = responseGet.jsonPath();

        List<String> nameList = jsonGet.getList("name");
        Assertions.assertThat(nameList).hasSize(3).contains("Do zrobienia", "W trakcie", "Zrobione");

        given()
                .queryParam("key", key)
                .queryParam("token", token)
                .contentType(ContentType.JSON)
                .when()
                .delete("https://api.trello.com/1/boards/" + boardId)
                .then()
                .statusCode(SC_OK);

    }

}
