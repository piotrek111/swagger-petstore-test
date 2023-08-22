package test.functional;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import shared.pojos.MinimalPet;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class DeleteTests {

    private final String BASE_URI = "https://petstore.swagger.io/v2";

    private MinimalPet samplePet;
    private String existingPetId;
    private String invalidPetId = "0f0f0f0";

    @BeforeClass
    public void setUp() {
        // Create POJO for a sample pet
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("http://samle.url.com/image1.png");
        samplePet = new MinimalPet("test-kitty", photoUrls);

        // Add pet to the system so that we can extract an actual id
        Long petIdAsLong = given().
                            baseUri(BASE_URI).
                            contentType(ContentType.JSON).
                            body(samplePet).
                        when().
                            post("/pet").
                        then().
                            log().all().
                            statusCode(200).
                            extract().response().path("id");
        existingPetId = petIdAsLong.toString();
    }

    @Test(priority = 0)
    public void deleteExistingPet() {
        given().
                baseUri(BASE_URI).
        when().
                delete("/pet/" + existingPetId).
        then().log().all().
            statusCode(200).
            contentType(ContentType.JSON).
            body("code", is(equalTo(200))).
            body("message", is(equalTo(existingPetId)));
    }

    @Test(priority = 1, dependsOnMethods = "deleteExistingPet")
    public void deleteSamePetAgain() {
        given().
                baseUri(BASE_URI).
        when().
                delete("/pet/" + existingPetId).
        then().
            log().all().
            statusCode(404);
    }

    @Test(priority = 2)
    public void deletePetWithInvalidId() {
        given().
                baseUri(BASE_URI).
        when().
                delete("/pet/" + invalidPetId).
        then().
                log().all().
                statusCode(404).
                body("code", is(equalTo(404))).
                body("message", containsString("java.lang.NumberFormatException")).
                body("message", containsString(invalidPetId));
    }

}
