package board;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class BoardTest extends BaseTest {

    private final String  key =  "KEY";
    private final String token = "TOKEN";


    @Test
    public void createNewBoard() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "My first board")
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("My first board", json.get("name"));

        String boardId = json.get("id");

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + BOARDS + "/" + boardId)
                .then()
                .statusCode(SC_OK);

    }

    @Test
    public void createBoardWitEmptyBoardName() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "")
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .response();

    }

    @Test
    public void createBoardWithDefaultList(){

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Board without default list")
                .queryParam("defaultLists", false)
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("Board without default list", json.get("name"));

        //System.out.println(response.prettyPrint());
        String boardId = json.get("id");

       Response responseGet = given()
               .spec(reqSpec)
               .when()
               .get(BASE_URL + "/" + BOARDS + "/" + boardId + "/lists")
               .then()
               .statusCode(200)
               .extract()
               .response();

        //System.out.println(responseGet.asString());
        JsonPath jsonGet = responseGet.jsonPath();
        List<Object> idList = jsonGet.getList("id");

        Assertions.assertEquals(0, idList.size());

    }

    @Test
    public void createNewBoardWithLists() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Board with default list")
                .queryParam("defaultLists", true)
                .when()
                .post(BASE_URL + "/" + BOARDS)
                .then()
                .statusCode(400)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("Board with default list", json.get("name"));

        //System.out.println(response.prettyPrint());
        String boardId = json.get("id");

        Response responseGet = given()
                .spec(reqSpec)
                .when()
                .get(BASE_URL + "/" + BOARDS + "/" + boardId + "/lists")
                .then()
                .statusCode(200)
                .extract()
                .response();

        //System.out.println(responseGet.prettyPrint());
        JsonPath jsonGet = responseGet.jsonPath();
        List<Object> idList = jsonGet.getList("id");
        Assertions.assertEquals(3, idList.size());

        List<String> nameList = jsonGet.getList("name");
        Assertions.assertEquals("Do zrobienia", nameList.get(0));
        Assertions.assertEquals("W trakcie", nameList.get(1));
        Assertions.assertEquals("Zrobione", nameList.get(2));


    }

}
