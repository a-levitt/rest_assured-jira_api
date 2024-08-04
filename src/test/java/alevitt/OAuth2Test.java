package alevitt;

import files.Auth;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class OAuth2Test {
    public static void main(String[] args) {

        RestAssured.baseURI = "https://rahulshettyacademy.com";

        String response =
                given()
                        .formParam("client_id", Auth.oauth2ClientId())
                        .formParam("client_secret", Auth.oauth2ClientSecret())
                        .formParam("grant_type", "client_credentials")
                        .formParam("scope", "trust")
                        //.log().all()
                .when()
                        .post("oauthapi/oauth2/resourceOwner/token")
                .then()
                        //.log().all()
                        .assertThat()
                            .statusCode(200)
                        .extract().response().asString()
                ;

        JsonPath js = ReusableMethods.rawToJson(response);
        String oauth2Token = js.get("access_token");
        int expiresIn = js.getInt("expires_in");
        System.out.println("Token received. Expires in " + expiresIn + " seconds.");

        // Get courses, using OAuth2 token

        given()
                .queryParam("access_token", oauth2Token)
                //.log().all()
        .when()
                .get("oauthapi/getCourseDetails")
        .then()
                .log().all()
                .assertThat()
                    .statusCode(401)
        ;

        System.out.println("Authorized!");
    }
}
