package Turistguide.repository;

import Turistguide.model.City;
import Turistguide.model.Tags;
import Turistguide.model.TouristAttraction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Repository
public class TouristRepository {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    private List<TouristAttraction> listOfAttractions = new ArrayList<>();


    public TouristRepository(){
        this.listOfAttractions = getAttractions();
    }

    public void addTouristAttraction(String city, String name, String description, List<Tags> tags){
        listOfAttractions.add(new TouristAttraction(city, name, description, tags));
    }

    public List<TouristAttraction> getListOfAttractions() {
        return listOfAttractions;
    }


    public TouristAttraction getAttraction(String name){
        TouristAttraction attraction = null;
        for (TouristAttraction obj : listOfAttractions){
            if (name.equals(obj.getName())){
                attraction = obj;
            }
        }

        return attraction;
    }




    public String updateAttraction(String attraction, TouristAttraction update){
        String message = "nothing was updated";

        String cityUpdate = update.getCity();
        String nameUpdate = update.getName();
        String descriptionUpdate = update.getDescription();
        List<Tags> tagsList = update.getTags();

        for (TouristAttraction obj : listOfAttractions){
            if (obj.getName().equals(attraction)) {
                obj.setName(nameUpdate);
                obj.setDescription(descriptionUpdate);
                obj.setTags(tagsList);
                obj.setCity(cityUpdate);
                return "Attraction was updated";
            }
        }


        return message;
    }


    //delete function
    public String deleteAttraction(String name){
        String deleteAttraction = "object was deleted";

        Iterator<TouristAttraction> iterator = listOfAttractions.iterator();

        while (iterator.hasNext()){
            TouristAttraction attraction = iterator.next();
            if (attraction.getName().equals(name)){
                iterator.remove();
            }
        }

        return deleteAttraction;
    }

// *********************** ---------- DATABASE METHODS ------------ ************************** //


    //Database method to get the touristAttractions - might want to consider NOT using Enums, as this creates more code
    public List<TouristAttraction> getAttractions() {
        List<TouristAttraction> touristAttractions = new ArrayList<>();
        String sql = "SELECT a.Name as attraction_name, a.Description, c.Name as city_name, t.Name as tag_name from Attraction a " +
                "left join City c on a.CityID = c.CityID " +
                "left join Attraction_Tag at on a.AttractionID = at.AttractionID " +
                "left join Tags t on at.TagID = t.TagID";

        try(Connection connection= DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            Statement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql)) {

            while (resultSet.next()){
                String attractionName = resultSet.getString("attraction_name");
                String description = resultSet.getString("description");
                String cityName = resultSet.getString("city_name");
                String tagName = resultSet.getString("tag_name");

                //converting cityName and tagName to enums to for the touristAttraction object
                Tags tag = Tags.valueOf(tagName.toUpperCase());

                //checking to see if the touristAttraction already exists
                TouristAttraction attraction = null;
                for (TouristAttraction ta : touristAttractions){
                    if (ta.getName().equals(attractionName)){
                        attraction = ta;
                    }
                }
                //if it doesnt, we create it
                if (attraction == null){
                    attraction = new TouristAttraction(cityName, attractionName, description, new ArrayList<>());
                    touristAttractions.add(attraction);
                }

                //adding the tags to the tag list
                if (tagName != null) {
                    attraction.getTags().add(tag);
                }

            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return touristAttractions;
    }


    public TouristAttraction getAttractionDb(String name) {
        String sql = "SELECT a.Name as attraction_name, a.Description, c.Name as city_name, t.Name as tag_name " +
                "FROM Attraction a " +
                "LEFT JOIN City c ON a.CityID = c.CityID " +
                "LEFT JOIN Attraction_Tag at ON a.AttractionID = at.AttractionID " +
                "LEFT JOIN Tags t ON at.TagID = t.TagID " +
                "where a.Name = ?";

        TouristAttraction  attraction = null;

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement preparedStatement= connection.prepareStatement(sql)) {

                preparedStatement.setString(1, name);
                ResultSet resultSet = preparedStatement.executeQuery();

                //redundant code - consider finding a way to optimize
                //if statement used only when finding 1 touristAttraction object
                while (resultSet.next()){
                    String attractionName = resultSet.getString("attraction_name");
                    String description = resultSet.getString("description");
                    String cityName = resultSet.getString("city_name");
                    String tagName = resultSet.getString("tag_name");

                    Tags tag = Tags.valueOf(tagName.toUpperCase());

                    if (attraction == null) {
                        attraction = new TouristAttraction(cityName, attractionName, description, new ArrayList<>());
                    }

                    if (tagName != null) {
                        attraction.getTags().add(tag);
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return attraction;
    }

    public List<String> getListOfCities(){
        List<String> list = new ArrayList<>();

        try(Connection con = DriverManager.getConnection(dbUrl,dbUsername,dbPassword)){

            String sqlString = "SELECT name FROM city";

            PreparedStatement stmt = con.prepareStatement(sqlString);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                list.add(rs.getString("name"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


    //*******

    public void logEnvVariables() {
        System.out.println("DEV_DB_URL: " + dbUrl);
        System.out.println("DEV_DB_USER: " + dbUsername);
        System.out.println("DEV_DB_PASSWORD: " + dbPassword);
    }

}
