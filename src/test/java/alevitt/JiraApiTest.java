package alevitt;

import files.ReusableMethods;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

import files.Payload;
import files.Auth;
import io.restassured.path.json.JsonPath;

import java.io.File;
import java.util.Arrays;

public class JiraApiTest {
    public static void main(String[] args) {

        RestAssured.baseURI = "https://alevitt.atlassian.net";

        String response =
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Basic " + Auth.baseApiKey())
                .body(Payload.postAddBugBody())
                //.log().all()
        .when()
                .post("rest/api/3/issue")
        .then()
                //.log().all()
                .assertThat()
                    .statusCode(201)
                .extract().response().asString()
        ;

        JsonPath js = ReusableMethods.rawToJson(response);
        String createdBugId = js.get("id");
        System.out.println("New bug was created with ID: " + createdBugId);

        // Attach file to the bug
        given()
                .pathParam("key", createdBugId)
                .header("X-Atlassian-Token", "no-check")
                .header("Authorization", "Basic " + Auth.baseApiKey())
                .multiPart("file", new File("C:\\Users\\daski\\Pictures\\Screenshots\\Fullstack.jpeg"))
                //.log().all()
        .when()
                .post("rest/api/3/issue/{key}/attachments")
        .then()
                //.log().all()
                .assertThat()
                    .statusCode(200)
                //.body("filename", equalTo("Fullstack.jpeg"))
        ;
        System.out.println("File was successfully attached to bug-" + createdBugId);
    }
}
