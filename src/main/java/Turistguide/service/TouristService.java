package Turistguide.service;



import Turistguide.model.TouristAttraction;
import Turistguide.repository.TouristRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class TouristService {

    private TouristRepository touristRepository;

    public TouristService(TouristRepository repository){
        this.touristRepository = repository;
    }


    // to create an new attraction
    //TODO
    // this logic should be inside TouristRepository
    public TouristAttraction addTouristAttraction(TouristAttraction attraction) {
        if (touristRepository.checkIfObjectExists(attraction.getName())) {
            throw new IllegalArgumentException("A tourist attraction with that name already exists");
        }
        touristRepository.addTouristAttraction(attraction.getCity(), attraction.getName(),attraction.getDescription(), attraction.getTags());
        return touristRepository.getAttractionDb(attraction.getName());
    }


    /// ************************** Get infomation from database *********************** ///
    public List<TouristAttraction> allTouristAttractions(){
        return touristRepository.getAttractions();
    }

    // this is to get specific attraction to the controller.
    public TouristAttraction getAttraction(String name){
        return touristRepository.getAttractionDb(name);
    }

    /// ************************* Update and remove from database *********************** ///
    public String updateAttraction(String attraction, TouristAttraction obj){
        return touristRepository.updateAttraction(attraction, obj);
    }

    public String deleteAttraction(String name){
        return touristRepository.deleteAttraction(name);
    }

    // *** Helper functions for Thymleaf *** //
    public List<String> getListOfCities(){
        return touristRepository.getListOfCities();
    }

    public List<String> getListOfTags(){
        return touristRepository.getListOfTags();
    }

}
