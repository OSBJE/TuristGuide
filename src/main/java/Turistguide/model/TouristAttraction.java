package Turistguide.model;

import java.util.ArrayList;
import java.util.List;

public class TouristAttraction {

    private String city;
    private String name;
    private String description;
    private List<String> tags = new ArrayList<>();

    public TouristAttraction(String city, String name, String description, List<String> tags) {
        this.city = city;
        this.name = name;
        this.description = description;
        this.tags = tags;
    }

    //TODO
    // Some logic there makes sure you cannot add blank names and descriptions
    public TouristAttraction(){
    }


    ///**************** Get and Setters ***************///

    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTags(List<String> tag) {
        tags = tag;
    }

    public List<String> getTags(){
        return tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
