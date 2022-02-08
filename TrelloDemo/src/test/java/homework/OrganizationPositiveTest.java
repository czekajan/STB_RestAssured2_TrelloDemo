package homework;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;


public class OrganizationPositiveTest extends BaseHomework{

    private static String organizationId;

    @AfterEach
    public void deleteOrganization() {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(200);
    }


    @Test
    public void createOrgzanizationWithCorrectName() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Org name 1")
                .queryParam("name", "org_name_1")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo("Org name 1");
        assertThat(json.getString("name")).isEqualTo("org_name_1");

        String orgName = json.getString("name");
        assertThat(orgName.length() > 2).isTrue();

        organizationId = json.get("id");
    }

    @Test
    public void createOrganizationWithDescription(){
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Org name")
                .queryParam("desc", "This is desc for the organizations.")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("desc")).isEqualTo("This is desc for the organizations.");

        organizationId = json.get("id");
    }

    @Test
    public void createOrganizationWithWebsite(){
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "Org name")
                .queryParam("website", "https://firstorganization.com")
                .when()
                .post(BASE_URL + "/" + ORGANIZATIONS)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("website")).isEqualTo("https://firstorganization.com");
        assertThat(json.getString("website")).startsWith("https://");

        organizationId = json.get("id");
    }
}
