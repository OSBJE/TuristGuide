package Turistguide.repository;


import Turistguide.model.TouristAttraction;
import Turistguide.repository.TouristRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    }

    @Test
    void getListOfCities(){

    }

}
