package Turistguide.controller;


import Turistguide.model.City;
import Turistguide.model.Tags;
import Turistguide.model.TouristAttraction;
import Turistguide.service.TouristService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/welcome")
public class TouristController {


    private final TouristService touristService;

    public TouristController(TouristService service){
        this.touristService = service;
    }

    // get /attractions list
    @GetMapping("/attractionList")
    public String getAllTouristAttractions(Model model){
        model.addAttribute("something", "List of Tourist Attractions");
        model.addAttribute("attractionList", touristService.allTouristAttractions());
     return "attractionList";
    }

    /*
    //Old code from first project
    @GetMapping("/attractionlist")
    public ResponseEntity<List<TouristAttraction>> getAllTouristAttractions(){
        return new ResponseEntity<>(touristService.allTouristAttractions(), HttpStatus.OK);
    }
    */

    // get /attractions/{name}
    @GetMapping("/attractions/{attractionName}")
    public ResponseEntity<TouristAttraction> getTouristAttraction(@PathVariable String attractionName){
        return new ResponseEntity<>(touristService.getAttraction(attractionName), HttpStatus.OK);
    }


    // This method allow us to store values when creating a new object.
    @GetMapping("/addAttraction")
    public String addTouristAttraction (Model model){
        TouristAttraction obj = new TouristAttraction();
        model.addAttribute("obj", obj);
        model.addAttribute("tags", Arrays.asList(Tags.values()));
        model.addAttribute("CityNames", Arrays.asList(City.values()));
        return "addAttraction";
    }

    // This method allow us to save our stored values when creating a new object
    @PostMapping("/addAttraction")
    public String saveTouristAttraction(@ModelAttribute TouristAttraction obj){
        touristService.addTouristAttraction(obj);
        return "redirect:/welcome/attractionList";
    }



    /*
    //old code
    // Works tested with Testrequest1.http
    @PostMapping("/attractions/add")
    public ResponseEntity<TouristAttraction> addTouristAttraction (@RequestBody TouristAttraction attraction){
        TouristAttraction attractionToAdd = touristService.addTouristAttraction(attraction);
        return new ResponseEntity<>(attractionToAdd, HttpStatus.CREATED );
    }
    */

    //
    @GetMapping("/{name}/edit")
    public String updateTouristAttraction(@PathVariable String name, Model model){
        TouristAttraction obj = touristService.getAttraction(name);
        model.addAttribute("objToUpdate", obj);
        model.addAttribute("tagsList", Arrays.asList(Tags.values()));
        model.addAttribute("CityNames", Arrays.asList(City.values()));
        return "updateAttraction";
    }



    //Fik det til at virker med Update begrund af hidden input i ens html fil.
    @PostMapping("/update")
    public String saveUpdateTouristAttraction(@ModelAttribute TouristAttraction objToUpdate){
        touristService.updateAttraction(objToUpdate.getName(), objToUpdate);
        return "redirect:/welcome/attractionList";

    }



    /*
    old code to update attractio
    // post /attractions/update
    // not sure I can let it return an string when you update the obj, ill test it.
    @PutMapping("/attractions/update/{attractionName}")
    public ResponseEntity<String> updateTouristAttraction (@PathVariable String attractionName, @RequestBody TouristAttraction updateAttraction ){
        return new ResponseEntity<>(touristService.updateAttraction(attractionName, updateAttraction), HttpStatus.OK);
    }

     */

    //method to delete TuristAttraction
    @PostMapping("/{name}/delete")
    public String deleteTouristAttraction(@PathVariable String name){
        touristService.deleteAttraction(name);
        return "redirect:/welcome/attractionList";
    }

    /*
    old code to update attractio

    // post attractions/delete/{name}
    // tested this with PostMan and the method works, however you can not use an URL as it sends a get method
    // and I have mapped it using a deleteMapping. Hence you need to send a delete request.
    @DeleteMapping("/attractions/delete/{attractionName}")
    public ResponseEntity<String> deleteTouristAttraction(@PathVariable String attractionName){
        return new ResponseEntity<>(touristService.deleteAttraction(attractionName),HttpStatus.OK);
    }
    */

    @GetMapping("/{name}/tags")
    public String getTouristAttractionTags(@PathVariable String name, Model model){
        TouristAttraction obj = touristService.getAttraction(name);
        List<Tags> listOfTags = obj.getTags();
        model.addAttribute("listOfTags", listOfTags);
        return "tags";
    }








}