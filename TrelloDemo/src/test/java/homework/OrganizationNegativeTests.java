package homework;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class OrganizationNegativeTests extends BaseHomework {

    @Test
    public void createOrganizationWithUppercaseLettersOfName(){

        given()
                .spec(reqSpec)
                .queryParam("name", "ORG_NAME_123")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(400);
    }

    @Test
    public void createOrganizationWithoutDisplayName() {
        given()
                .spec(reqSpec)
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(400);
    }
}
