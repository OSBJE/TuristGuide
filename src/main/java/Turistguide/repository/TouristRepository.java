package Turistguide.repository;

import Turistguide.model.City;
import Turistguide.model.Tags;
import Turistguide.model.TouristAttraction;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Repository
public class TouristRepository {


    private final List<TouristAttraction> listOfAttractions = new ArrayList<>();

    public TouristRepository(){
        populateTouristList();
    }

    private void populateTouristList(){
        List<Tags> rundeTårnTags = new ArrayList<>();
        List<Tags> lilleHavfrueTags = new ArrayList<>();
        List<Tags> rosenborgTags = new ArrayList<>();
        rundeTårnTags.add(Tags.GRATIS);
        rundeTårnTags.add(Tags.BØRNEVENLIG);

        lilleHavfrueTags.add(Tags.GRATIS);
        lilleHavfrueTags.add(Tags.BØRNEVENLIG);
        lilleHavfrueTags.add(Tags.KUNST);

        rosenborgTags.add(Tags.KUNST);
        rosenborgTags.add(Tags.MUSEUM);
        listOfAttractions.add(new TouristAttraction(City.KØBENHAVN,"Rundtårn", "Det er et tårn som er rundt", rundeTårnTags));
        listOfAttractions.add(new TouristAttraction(City.ODENSE,"Lille havfrue", "Chinks er vilde med hende", lilleHavfrueTags));
        listOfAttractions.add(new TouristAttraction(City.AARHUS,"Rosenborg", "Et flot slot, hvor den dansk kongefamile holder til", rosenborgTags));
    }


    public void addTouristAttraction(City city, String name, String description, List<Tags> tags){
        listOfAttractions.add(new TouristAttraction(city, name, description, tags));
    }

    public List<TouristAttraction> getListOfAttractions() {
        return listOfAttractions;
    }

    public TouristAttraction getAttraction(String name){
        TouristAttraction attraction = null;
        for (TouristAttraction obj : listOfAttractions){
            if (name.equals(obj.getName())){
                attraction = obj;
            }
        }

        return attraction;
    }


    public String updateAttraction(String attraction, TouristAttraction update){
        String message = "nothing was updated";

        City cityUpdate = update.getCity();
        String nameUpdate = update.getName();
        String descriptionUpdate = update.getDescription();
        List<Tags> tagsList = update.getTags();

        for (TouristAttraction obj : listOfAttractions){
            if (obj.getName().equals(attraction)) {
                obj.setName(nameUpdate);
                obj.setDescription(descriptionUpdate);
                obj.setTags(tagsList);
                obj.setCity(cityUpdate);
                return "Attraction was updated";
            }
        }


        return message;
    }


    //delete function
    public String deleteAttraction(String name){
        String deleteAttraction = "object was deleted";

        Iterator<TouristAttraction> iterator = listOfAttractions.iterator();

        while (iterator.hasNext()){
            TouristAttraction attraction = iterator.next();
            if (attraction.getName().equals(name)){
                iterator.remove();
            }
        }

        return deleteAttraction;
    }

// *********************** ---------- DATABASE METHODS ------------ **************************







}
