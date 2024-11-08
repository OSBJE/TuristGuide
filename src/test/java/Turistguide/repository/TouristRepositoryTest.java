package Turistguide.repository;


import Turistguide.model.TouristAttraction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("h2")
public class TouristRepositoryTest {

    @Autowired
    TouristRepository repository;

    @Test
    void contextLoads() {
    }

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
        repository.addTouristAttraction("København", "Den Lille havfrue", "H.C. Andersen værk", Arrays.asList("Gratis", "Børnevenlig", "Kunst", "Natur"));
    }

    @Test
    void getListOfCities(){
        List<String> list = repository.getListOfCities();
        assertTrue(list.contains("København"));
        assertFalse(list.contains("Vordingborg"));
    }

    @Test
    void getListOfTags() {
        List<String> expectedTagsList = Arrays.asList("Gratis", "Børnevenlig", "Kunst", "Museum", "Natur");
        List<String> falseTagsList = Arrays.asList("Hans", "Hansen", "Er", "En", "Mand");

        List<String> actualTagsList = repository.getListOfTags();

        assertEquals(expectedTagsList, actualTagsList);
        assertNotEquals(falseTagsList, actualTagsList);

    }

    @Test
    void addTouristAttraction() {
        System.out.println(repository.getAttractions().size());
        List<TouristAttraction> liste = repository.getAttractions();

        repository.addTouristAttraction("Odense", "H.C. Andersen Hus", "H.C. Andersens fødehjem", Arrays.asList("Børnevenlig", "Kunst"));
        String touristAttractionNotInDb = "Kronborg";

        int expectedDbSize = 4;
        int actualSize = repository.getAttractions().size();

        TouristAttraction actualTouristAttraction = repository.getAttractionDb("H.C. Andersen Hus");

        System.out.println(expectedDbSize + " " + actualSize);
        assertEquals("H.C. Andersen Hus", actualTouristAttraction.getName());
        assertNotEquals(touristAttractionNotInDb, actualTouristAttraction.getName());
        assertEquals(expectedDbSize, actualSize);
        repository.deleteAttraction("H.C. Andersen Hus");
    }

    @Test
    void getAttractions() {
        List<TouristAttraction> expectedList = new ArrayList<>();

        TouristAttraction t1 = repository.getAttractionDb("Rundetårn");
        TouristAttraction t2 = repository.getAttractionDb("Rosenborg");
        TouristAttraction t3 = repository.getAttractionDb("Den Lille havfrue");
        expectedList.add(t1);    expectedList.add(t2);    expectedList.add(t3);

        List<TouristAttraction> actualList = repository.getAttractions();

        assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
        assertEquals(expectedList.get(1).getName(), actualList.get(1).getName());
        assertEquals(expectedList.get(2).getName(), actualList.get(2).getName());
        assertEquals(expectedList.size(), actualList.size());
    }

    @Test
    void checkIfObjectExists() {

    }
}
