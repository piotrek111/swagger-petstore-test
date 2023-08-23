package test.functional;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import shared.pojos.Category;
import shared.pojos.FullyFeaturedPet;
import shared.pojos.MinimalPet;
import shared.pojos.Tag;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class AddNewPetTests {

    private final String BASE_URI = "https://petstore.swagger.io/v2";
    private MinimalPet minimalPet;
    private FullyFeaturedPet fullyFeaturedPet;
    private long existingPetId;

    @BeforeClass
    public void createMinimalPetPOJO() {
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("http://some-place/kitty.png");
        minimalPet = new MinimalPet("baby cat", photoUrls);
    }

    @BeforeClass
    public void createFullyFeaturedPetPOJO() {

        Tag tag = new Tag(1, "fluffy");
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        Category category = new Category(1, "Felines");
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add("http://some-place/big-cat.png");

        fullyFeaturedPet = new FullyFeaturedPet(0, category, "big cat", photoUrls, tags, "sold");

    }

    @Test(priority = 0)
    public void addNewMinimalPet() {

        existingPetId = given().
                log().all().
                baseUri(BASE_URI).
                contentType(ContentType.JSON).
                body(minimalPet).
        when().
                post("/pet").
        then().
                log().all().
                statusCode(200).
                contentType(ContentType.JSON).
                body("id", is(instanceOf(Long.class))).
                body("name", is(equalTo("baby cat"))).
                body("photoUrls", hasItem("http://some-place/kitty.png")).
                extract().response().path("id"); // we end up extracting petId to use it another test case
    }
    @Test(priority = 1)
    public void addFullyFeaturedPet() {

        given().
                baseUri(BASE_URI).
                contentType(ContentType.JSON).
                body(fullyFeaturedPet).
        when().
                post("/pet").
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("id", is(instanceOf(Long.class))).
                body("name", is(equalTo("big cat"))).
                body("photoUrls", hasItem("http://some-place/big-cat.png")).
                body("category.id", is(equalTo(1))).
                body("category.name", is(equalTo("Felines"))).
                body("tags.id", hasItem(1)).
                body("tags.name", hasItem("fluffy")).
                body("status", is(equalTo("sold")));

    }


    @Test(priority = 2, dependsOnMethods = "addNewMinimalPet")
    public void addPetWithAnExistingId() {
        // We're going to re-use existing petId and post a fullyFeaturedPet
        // The API should accept this request and overwrite previous pet object with that id
        fullyFeaturedPet.setId(existingPetId);

        given().
                baseUri(BASE_URI).
                contentType(ContentType.JSON).
                body(fullyFeaturedPet).
        when().
                post("/pet").
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("id", allOf(instanceOf(Long.class), is(equalTo(existingPetId)))).
                body("name", is(equalTo("big cat"))).
                body("photoUrls", hasItem("http://some-place/big-cat.png")).
                body("category.id", is(equalTo(1))).
                body("category.name", is(equalTo("Felines"))).
                body("tags.id", hasItem(1)).
                body("tags.name", hasItem("fluffy")).
                body("status", is(equalTo("sold")));
    }

    @Test(priority = 3)
    public void addPetWithValidAndInvalidData() {
        // I cannot re-use POJO here as it will fail type checking if I try to set some fields to incorrect types
        // so for this case I will use a "faulty" JSON file and post it to the server
        String faultyJsonString = fetchFaultyJson();
        given().
                baseUri(BASE_URI).
                contentType(ContentType.JSON).
                body(faultyJsonString).
        when().
                post("/pet").
        then().
                statusCode(500).
                contentType(ContentType.JSON);
    }

    private String fetchFaultyJson() {
        Path path = Paths.get("src/main/resources/json/faulty-json.pet.json");
        String jsonString = "";
        try {
            jsonString = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
