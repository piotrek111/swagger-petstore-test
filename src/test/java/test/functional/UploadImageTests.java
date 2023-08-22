package test.functional;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UploadImageTests {

    private final String BASE_URI = "https://petstore.swagger.io/v2";
    private final String dogImagePath = "src/main/resources/images/dogo.png";
    private final String dogImageSize = "1155141 bytes";
    private final String catImagePath = "src/main/resources/images/kitty.jpg";
    private final String catImageSize = "236878 bytes";
    /*
    NOTE: It looks like Swagger API simply accepts the file and doesn't do many further validations,
    i.e. it doesn't check if the petId already exists. So you can upload the files multiple times
    to your heart's content while using random IDs at the same time. It only seems to care about ID format (it must be long integer)
     */
    @BeforeClass
    public void setUp() {
        // Ideally we should create an actual pet with existing Id before the first test case is executed.
        // But as I mentioned before, Swagger doesn't seem to care about this.
    }
    @Test
    public void uploadImageForExistingPetId() {
        given().
                baseUri(BASE_URI).
                multiPart("file", new File(dogImagePath)).
        when().
                post("/pet/12345/uploadImage").
        then().
                log().all().
                statusCode(200).
                contentType(ContentType.JSON).
                body("code", is(equalTo(200))).
                body("message", containsString("dogo.png")).
                body("message", containsString(dogImageSize));
    }

    @Test
    public void uploadImageForInvalidPetId() {
        given().
                baseUri(BASE_URI).
                multiPart("file", new File(catImagePath)).
        when().
                post("/pet/0a0b0c/uploadImage").
        then().
                log().all().
                statusCode(404).
                contentType(ContentType.JSON).
                body("code", is(equalTo(404 ))).
                body("message", containsString("NumberFormatException"));
    }

    @Test
    public void uploadImageAndMetadata() {
        String metadataString = "doggy-dog";
        given().
                baseUri(BASE_URI).
                multiPart("file", new File(dogImagePath)).
                multiPart("additionalMetadata", metadataString).
        when().
                post("/pet/12345/uploadImage").
        then().
                log().all().
                statusCode(200).
                contentType(ContentType.JSON).
                body("code", is(equalTo(200))).
                body("message", containsString(metadataString)).
                body("message", containsString("dogo.png"));
    }

    /*
    Swagger Docs doesn't mention what error state would be triggered
    but by trial and error I see 415 in case of empty request.
     */
    @Test
    public void uploadEmptyForm() {
        given().
                baseUri(BASE_URI).
        when().
                post("/pet/12345/uploadImage").
        then().
                log().all().
                statusCode(415);

    }

}
