package alevitt;

import files.Auth;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;

import java.util.Arrays;
import java.util.List;

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

        // Get courses, using OAuth2 token | UPD: using pojo class
        GetCourse jsonObject =
        given()
                .queryParam("access_token", oauth2Token)
                //.log().all()
        .when()
                .get("oauthapi/getCourseDetails").as(GetCourse.class)
        /*.then()
                .log().all()
                .assertThat()
                    .statusCode(401)*/
        ;
        System.out.println(jsonObject.getInstructor());
        System.out.println(jsonObject.getLinkedIn());
        //System.out.println(jsonObject.getCourses().getApi().get(1).getCourseTitle());

        List<Api> apiCourses = jsonObject.getCourses().getApi();
        for (int i = 0; i < apiCourses.size(); i++) {
            if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
                System.out.println(apiCourses.get(i).getCourseTitle());
                System.out.println(apiCourses.get(i).getPrice());
            }
        }
    }


}
