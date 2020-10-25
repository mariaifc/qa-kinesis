package api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ApiRequest {

    public static Response doGetRequest(String urlRequest) {
        return given().when().get(urlRequest).then().extract().response();
    }
}
