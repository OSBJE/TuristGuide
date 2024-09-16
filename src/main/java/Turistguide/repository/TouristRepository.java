package Turistguide.repository;

import Turistguide.model.TouristAttraction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Repository
public class TouristRepository {


    private List<TouristAttraction> listOfAttractions = new ArrayList<>();

    public TouristRepository(){
        populateTouristList();
    }

    private void populateTouristList(){
        listOfAttractions.add(new TouristAttraction("Rundtårn", "Det er et tårn som er rundt"));
        listOfAttractions.add(new TouristAttraction("Lille havfrue", "Chinks er vilde med hende"));
        listOfAttractions.add(new TouristAttraction("Rosenborg", "Et flot slot, hvor den dansk kongefamile holder til"));
    }


    public void addTouristAttraction(String name, String description){
        listOfAttractions.add(new TouristAttraction(name, description));
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

        String nameUpdate = update.getName();
        String descriptionUpdate = update.getDescription();

        for (TouristAttraction obj : listOfAttractions){
            if (obj.getName().equals(attraction)) {
                obj.setName(nameUpdate);
                obj.setDescription(descriptionUpdate);
                return "Attraction was updated";
            }
        }


        return message;
    }


    //delete function
    public String deleteAttraction(String name){
        String deleteAttraction = "object was deleted";

        List<TouristAttraction> list = listOfAttractions;
        Iterator<TouristAttraction> iterator = list.iterator();

        while (iterator.hasNext()){
            TouristAttraction attraction = iterator.next();
            if (attraction.getName().equals(name)){
                iterator.remove();
            }
        }

        return deleteAttraction;
    }

}
