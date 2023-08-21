package test.functional;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class FindByStatusTests {

    private final String BASE_URI = "https://petstore.swagger.io/v2";
    private final String FIND_BY_STATUS_PATH = "/pet/findByStatus";


    @Test
    public void searchPetsMatchingSingleStatus() {
        /*
        NOTE: In this version the test case will only check the first object in the JSON array if the status field
        matches "available".
        I would need to figure out how to match them all.
         */
        given().
                baseUri(BASE_URI).
                queryParam("status", "available").
        when().
                get(FIND_BY_STATUS_PATH).
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("[0].status", is(equalTo("available")));
    }

    @Test
    public void searchPetsMatchingAllStatuses() {
        /*
        Note: This test case is just a skeleton. I would some more time to figure out how to deal with an array of JSON
        objects that can have different status values.

        Suggested solution: extract response and use JsonPath (which I've added as dependency) to traverse the array and
        check statues.
         */
        given().
                baseUri(BASE_URI).
                queryParam("status", "available").
                queryParam("status", "sold").
                queryParam("status", "pending").
        when().
                get(FIND_BY_STATUS_PATH).
        then().
                contentType(ContentType.JSON).
                statusCode(200);

    }

    @Test
    public void searchPetsUsingInvalidStatus() {
        /*
        Note: it looks like Swagger API doesn't do any validation and accepts whatever status value. 200 OK is returned
        with an empty array.
         */
        given().
                baseUri(BASE_URI).
                queryParam("status", "error").
        when().
                get(FIND_BY_STATUS_PATH).
        then().
                statusCode(400);
    }

    @Test
    public void searchPetsUsingMixedStatuses() {
        /*
        Looks like Swagger API ignores the "bad" value and returns other valid matches
         */
        given().
                baseUri(BASE_URI).
                queryParam("status", "sold").
                queryParam("status", "error").
        when().
                get(FIND_BY_STATUS_PATH).
        then().
                contentType(ContentType.JSON).
                statusCode(200).
                body("[0].status", is(equalTo("sold")));
    }

}
