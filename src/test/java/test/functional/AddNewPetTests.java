package test.functional;

import org.testng.annotations.Test;
import shared.pojos.FullyFeaturedPet;
import shared.pojos.MinimalPet;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class AddNewPetTests {

    private MinimalPet minimalPet;
    private FullyFeaturedPet fullyFeaturedPet;
    private long existingPetId;

    @Test
    public void addNewMinimalPet() {

    }
    @Test
    public void addFullyFeaturedPet() {

    }


    @Test
    public void addPetWithAnExistingId() {

    }

    @Test
    public void addPetWithValidAndInvalidData() {

    }

}
