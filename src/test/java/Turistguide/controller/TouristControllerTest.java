package Turistguide.controller;

import Turistguide.model.TouristAttraction;
import Turistguide.service.TouristService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//MockTest class af TouristController

@WebMvcTest(TouristController.class)
class TouristControllerTest {

    private TouristAttraction touristAttraction = new TouristAttraction();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TouristService touristService;

    @Test
    void testGetAllTouristAttractions() throws Exception {
        mockMvc.perform(get("/welcome/attractionList"))
                .andExpect(status().isOk())
                .andExpect(view().name("attractionList"));
    }

    @Test
    void showTagsForAttraction() throws Exception {
                touristAttraction = new TouristAttraction("Aarhus", "Rosenborg", "Et flot slot", Arrays.asList("Børnevenligt", "Gratis"));
                when(touristService.getAttraction("Rosenborg")).thenReturn(touristAttraction);

        mockMvc.perform(get("/welcome/Rosenborg/tags"))
                .andExpect(status().isOk())
                .andExpect(view().name("tags"))
                .andExpect(model().attributeExists("listOfTags"))
                .andExpect(model().attribute("listOfTags", Arrays.asList("Børnevenligt", "Gratis")));


    }

    @Test
    void createTouristAttraction() throws Exception {
        mockMvc.perform(get("/welcome/addAttraction"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("obj"))
                .andExpect(model().attribute("obj", hasProperty("name", nullValue())))
                .andExpect(model().attribute("obj", hasProperty("description", nullValue())))
                .andExpect(model().attribute("obj", hasProperty("city", nullValue())))
                .andExpect(model().attribute("obj", hasProperty("tags", notNullValue())))
                .andExpect(view().name("addAttraction"));
    }


    @Test
    void saveTouristAttraction() throws Exception {
        mockMvc.perform(post("/welcome/save").sessionAttr("obj", this.touristAttraction))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/welcome/attractionList"))
                .andExpect(redirectedUrl("/welcome/attractionList"));
    }



}