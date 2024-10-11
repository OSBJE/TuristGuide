package Turistguide.service;



import Turistguide.model.TouristAttraction;
import Turistguide.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TouristService {

    private final TouristRepository touristRepository;

    public TouristService(TouristRepository repository){
        this.touristRepository = repository;
    }


    // to create an new attraction
    //TODO
    // this logic should be inside TouristRepository
    public TouristAttraction addTouristAttraction(TouristAttraction attraction){
        touristRepository.addTouristAttraction(attraction.getCity(), attraction.getName(),attraction.getDescription(), attraction.getTags());
        return touristRepository.getAttractionDb(attraction.getName());

    }


    //Database method
    public List<TouristAttraction> allTouristAttractions(){
        return touristRepository.getAttractions();
    }


    // this is to get specific attraction to the controller.
    public TouristAttraction getAttraction(String name){
        return touristRepository.getAttractionDb(name);
    }

    //This is to update

    public String updateAttraction(String attraction, TouristAttraction obj){
        return touristRepository.updateAttraction(attraction, obj);
    }

    // this is to delete
    public String deleteAttraction(String name){
        return touristRepository.deleteAttraction(name);
    }


    public List<String> getListOfCities(){
        return touristRepository.getListOfCities();
    }

    public List<String> getListOfTags(){
        return touristRepository.getListOfTags();
    }

}
