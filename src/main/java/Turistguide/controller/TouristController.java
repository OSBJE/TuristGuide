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


    // get /attractions/{name}
    @GetMapping("/attractions/{attractionName}")
    public ResponseEntity<TouristAttraction> getTouristAttraction(@PathVariable String attractionName){
        return new ResponseEntity<>(touristService.getAttraction(attractionName), HttpStatus.OK);
    }

    //TODO this should get out city values directly from database.

    // This method allow us to store values when creating a new object.
    @GetMapping("/addAttraction")
    public String addTouristAttraction (Model model){
        TouristAttraction obj = new TouristAttraction();
        model.addAttribute("obj", obj);
        model.addAttribute("tags", Arrays.asList(Tags.values()));
        model.addAttribute("CityNames", touristService.getListOfCities());
        return "addAttraction";
    }

    // This method allow us to save our stored values when creating a new object
    @PostMapping("/save")
    public String saveTouristAttraction(@ModelAttribute TouristAttraction obj){
        touristService.addTouristAttraction(obj);
        return "redirect:/welcome/attractionList";
    }


    //
    @GetMapping("/{name}/edit")
    public String updateTouristAttraction(@PathVariable String name, Model model){
        TouristAttraction obj = touristService.getAttraction(name);
        model.addAttribute("objToUpdate", obj);
        model.addAttribute("tagsList", Arrays.asList(Tags.values()));
        model.addAttribute("CityNames", touristService.getListOfCities());
        return "updateAttraction";
    }



    //Fik det til at virker med Update begrund af hidden input i ens html fil.
    @PostMapping("/update")
    public String saveUpdateTouristAttraction(@ModelAttribute TouristAttraction objToUpdate){
        touristService.updateAttraction(objToUpdate.getName(), objToUpdate);
        return "redirect:/welcome/attractionList";

    }


    //method to delete TuristAttraction
    @PostMapping("/{name}/delete")
    public String deleteTouristAttraction(@PathVariable String name){
        touristService.deleteAttraction(name);
        return "redirect:/welcome/attractionList";
    }


    @GetMapping("/{name}/tags")
    public String getTouristAttractionTags(@PathVariable String name, Model model){
        TouristAttraction obj = touristService.getAttraction(name);
        List<Tags> listOfTags = obj.getTags();
        model.addAttribute("listOfTags", listOfTags);
        return "tags";
    }








}