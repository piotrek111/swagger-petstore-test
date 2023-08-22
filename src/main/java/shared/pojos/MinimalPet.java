package shared.pojos;

import java.util.List;

public class MinimalPet {
    // This is a simple POJO class representing Pet with minimum required parameters
    private String name;
    List<String> photoUrls;

    public MinimalPet() {

    }

    public MinimalPet(String name, List<String> photoUrls) {
        this.name = name;
        this.photoUrls = photoUrls;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }
}
