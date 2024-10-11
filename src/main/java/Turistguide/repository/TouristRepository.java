package Turistguide.repository;

import Turistguide.model.TouristAttraction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.*;


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

    }


    public void addTouristAttraction(String city, String name, String description, List<String> tags){
        int primaryKeyDB = 0;

        try {
            Connection con = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);

            String sqlString = "insert into attraction (Name, CityID, Description ) VALUES(?,(SELECT CityID from city where name = ?),?)";

            PreparedStatement stmt = con.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,name);
            stmt.setString(2,city);
            stmt.setString(3,description);
            stmt.executeUpdate();

            ResultSet returnPrimaryKey = stmt.getGeneratedKeys();

            if (returnPrimaryKey.next()){
                primaryKeyDB = returnPrimaryKey.getInt(1);
            }

            Map<String, Integer> pairList = getTagsId();


            if(!tags.isEmpty()){
                for(String tag : tags){

                    String sqlString2 = "insert into attraction_tag (AttractionID, tagID) VALUES (?, ?)";

                    PreparedStatement stmt2 = con.prepareStatement(sqlString2);
                    stmt2.setInt(1, primaryKeyDB);
                    stmt2.setInt(2,pairList.get(tag));
                    stmt2.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Map<String, Integer> getTagsId(){
        Map<String, Integer> pairList = new HashMap<>();

        try {
            Connection con = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);

            String sqlString = "SELECT * FROM tags";
            PreparedStatement stmt = con.prepareStatement(sqlString);

            ResultSet rls = stmt.executeQuery();


            while(rls.next()){
                String name = rls.getString("name");
                int tagID = rls.getInt("TagID");

                pairList.put(name,tagID);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pairList;

    }

    public String updateAttraction(String attraction, TouristAttraction update){
        String message = "nothing was updated";

        String cityUpdate = update.getCity();
        String nameUpdate = update.getName();
        String descriptionUpdate = update.getDescription();
        List<String> tagsList = update.getTags();

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
        int primaryKeyDB = 0;


        try {
            Connection conn = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);

            String getKey = "SELECT attractionID FROM attraction WHERE name = ? ";
            PreparedStatement stmt = conn.prepareStatement(getKey);
            stmt.setString(1,name);

            ResultSet primaryKey = stmt.executeQuery();
            primaryKey.next(); //Den skal være der fordi vi skal rykke coursen til første række.


            primaryKeyDB = primaryKey.getInt(1);


            String deleteAttraction = "delete from attraction where attractionID = ?";
            PreparedStatement stmt2 = conn.prepareStatement(deleteAttraction);
            stmt2.setInt(1,primaryKeyDB);
            stmt2.executeUpdate();

            return  "object was deleted";

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
                    attraction.getTags().add(tagName);
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


                    if (attraction == null) {
                        attraction = new TouristAttraction(cityName, attractionName, description, new ArrayList<>());
                    }

                    if (tagName != null) {
                        attraction.getTags().add(tagName);
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

    public List<String> getListOfTags(){
        List<String> list = new ArrayList<>();

        try(Connection con = DriverManager.getConnection(dbUrl,dbUsername,dbPassword)){

            String sqlString = "SELECT name FROM tags";

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


}
