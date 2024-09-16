package Turistguide.service;


import java.util.List;

@Service
public class TouristService {

    private final TouristRepository touristRepository;

    public TouristService(TouristRepository repository){
        this.touristRepository = repository;
    }


    // to create an new attraction
    public TouristAttraction addTouristAttraction(TouristAttraction attraction){
        TouristAttraction added = null;
        touristRepository.addTouristAttraction(attraction.getName(),attraction.getDescription());
        added = touristRepository.getAttraction(attraction.getName());
        return added;
    }

    // this is to the first get all attractions to the control
    public List<TouristAttraction> allTouristAttractions(){
        return touristRepository.getListOfAttractions();
    }


    // this is to get specific attraction to the controller.
    public TouristAttraction getAttraction(String name){
        return touristRepository.getAttraction(name);
    }

    //This is to update

    public String updateAttraction(String attraction, TouristAttraction obj){
        return touristRepository.updateAttraction(attraction, obj);
    }

    // this is to delete
    public String deleteAttraction(String name){
        return touristRepository.deleteAttraction(name);
    }


}
