package Turistguide.repository;


import Turistguide.model.TouristAttraction;
import Turistguide.repository.TouristRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
public class TouristRepositoryTest {

    @Test
    void contextLoads() {
    }

    @Autowired
    TouristRepository repository;

    @Test
    void getAttractionDb(){
        TouristAttraction objFound = repository.getAttractionDb("Den Lille havfrue");
        assertEquals("Den Lille havfrue", objFound.getName());
    }

    @Test
    void deleteAttraction(){
        TouristAttraction objFound  = repository.getAttractionDb("Den Lille havfrue");
        assertEquals("Den Lille havfrue", objFound.getName());

        repository.deleteAttraction("Den Lille havfrue");
        TouristAttraction objFound2  = repository.getAttractionDb("Den Lille havfrue");
        assertNull(objFound2);

        TouristAttraction controlObj = repository.getAttractionDb("Rundetårn");
        assertNotNull(controlObj);
    }

    @Test
    void getListOfCities(){
        List<String> list = repository.getListOfCities();
        assertTrue(list.contains("København"));
        assertFalse(list.contains("Vordingborg"));
    }

    @Test
    void getListOfTags() {
        //('Gratis'), ('Børnevenlig'), ('Kunst'), ('Museum'), ('Natur');
        List<String> expectedTagsList = Arrays.asList("Gratis", "Børnevenlig", "Kunst", "Museum", "Natur");
        List<String> falseTagsList = Arrays.asList("Hans", "Hansen", "Er", "En", "Mand");

        List<String> actualTagsList = repository.getListOfTags();

        assertEquals(expectedTagsList, actualTagsList);
        assertNotEquals(falseTagsList, actualTagsList);

    }

    @Test
    void addTouristAttraction() {

        repository.addTouristAttraction("Odense", "H.C. Andersen Hus", "H.C. Andersens fødehjem", Arrays.asList("Børnevenlig", "Kunst"));
        String touristAttractionNotInDb = "Kronborg";

        int expectedDbSize = 4;
        int actualSize = repository.getAttractions().size();


        TouristAttraction actualTouristAttraction = repository.getAttractionDb("H.C. Andersen Hus");

        assertEquals("H.C. Andersen Hus", actualTouristAttraction.getName());
        assertNotEquals(touristAttractionNotInDb, actualTouristAttraction.getName());
        assertEquals(expectedDbSize, actualSize);



    }

    @Test
    void checkIfObjectExists() {

    }

}
