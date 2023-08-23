package test.functional;

import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FindByStatusTests {

    private final String BASE_URI = "https://petstore.swagger.io/v2";
    private final String FIND_BY_STATUS_PATH = "/pet/findByStatus";

    @DataProvider
    public Object[][] provideStatusValues() {
        return new Object[][]{
                {"available"},
                {"sold"},
                {"pending"}
        };
    }

    @Test(dataProvider = "provideStatusValues")
    public void searchPetsMatchingSingleStatus(String statusValue) {
        // Using data provider to check each possible status value
        given().
                baseUri(BASE_URI).
                queryParam("status", statusValue).
        when().
                get(FIND_BY_STATUS_PATH).
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("status", everyItem(equalTo(statusValue)));
    }

    @Test(enabled = false) // disabled for now because Swagger API doesn't return all categories, only the first one. Bug?
    public void searchPetsMatchingAllStatuses() {
        given().
                baseUri(BASE_URI).
                queryParam("status", "available").
                queryParam("status", "sold").
                queryParam("status", "pending").
        when().
                get(FIND_BY_STATUS_PATH).
        then().
                contentType(ContentType.JSON).
                statusCode(200).
                body("status", hasItems("available", "sold", "pending"));

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
                statusCode(200).
                contentType(ContentType.JSON).
                body("", hasSize(equalTo(0)));
        ;
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
                body("status", everyItem(equalTo("sold")));
    }

}
