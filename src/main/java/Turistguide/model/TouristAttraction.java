package Turistguide.model;

import java.util.ArrayList;
import java.util.List;

public class TouristAttraction {


    private String name;
    private String description;
    private List<Tags> tags = new ArrayList<>();

    public TouristAttraction(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public TouristAttraction(){

    }

    public TouristAttraction(String name, String description, List<Tags> tags) {
        this.name = name;
        this.description = description;
        this.tags = tags;
    }



    ///**************** Get and Setters ***************///

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTag(Tags tag) {
        tags.add(tag);
    }

    public void setTags(List<Tags> newTags){
        tags.addAll(newTags);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tags> getTags(){
        return tags;
    }


}
