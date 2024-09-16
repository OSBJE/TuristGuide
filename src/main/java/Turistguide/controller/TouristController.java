package Turistguide.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/welcome")
public class TouristController {


    private final TouristService touristService;

    public TouristController(TouristService service){
        this.touristService = service;
    }

    // get /attractions list
    @GetMapping("")
    public ResponseEntity<List<TouristAttraction>> getAllTouristAttractions(){
        return new ResponseEntity<>(touristService.allTouristAttractions(), HttpStatus.OK);
    }

    // get /attractions/{name}
    @GetMapping("/attractions/{attractionName}")
    public ResponseEntity<TouristAttraction> getTouristAttraction(@PathVariable String attractionName){
        return new ResponseEntity<>(touristService.getAttraction(attractionName), HttpStatus.OK);
    }

    // post /attractions/add
    // Works tested with Testrequest1.http
    @PostMapping("/attractions/add")
    public ResponseEntity<TouristAttraction> addTouristAttraction (@RequestBody TouristAttraction attraction){
        TouristAttraction attractionToAdd = touristService.addTouristAttraction(attraction);
        return new ResponseEntity<>(attractionToAdd, HttpStatus.CREATED );
    }


    // post /attractions/update
    // not sure I can let it return an string when you update the obj, ill test it.
    @PutMapping("/attractions/update/{attractionName}")
    public ResponseEntity<String> updateTouristAttraction (@PathVariable String attractionName, @RequestBody TouristAttraction updateAttraction ){
        return new ResponseEntity<>(touristService.updateAttraction(attractionName, updateAttraction), HttpStatus.OK);
    }



    // post attractions/delete/{name}
    // tested this with PostMan and the method works, however you can not use an URL as it sends a get method
    // and I have mapped it using a deleteMapping. Hence you need to send a delete request.
    @DeleteMapping("/attractions/delete/{attractionName}")
    public ResponseEntity<String> deleteTouristAttraction(@PathVariable String attractionName){
        return new ResponseEntity<>(touristService.deleteAttraction(attractionName),HttpStatus.OK);
    }


}