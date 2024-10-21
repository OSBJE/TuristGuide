package Turistguide.repository;

import Turistguide.model.DBConnection;
import Turistguide.model.TouristAttraction;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.ErrorResponseException;


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

    private Connection conn;

    /// ********************************* Constructor and set-up ************************** ///
    public TouristRepository(){
    }

    //PostConstruct runes functions after we have generated the constructor
    //remove problem with beans.
    @PostConstruct
    public void setConn() {
        this.conn = new DBConnection().getConnection(dbUrl,dbUsername,dbPassword);
    }

    /// **************************** Add and modify database functions ******************** ///

    public void addTouristAttraction(String city, String name, String description, List<String> tags) {
        int primaryKeyDB = 0;

        // Check if an attractions exits and throws and error to thymleaf
        List<TouristAttraction> list = this.getAttractions();

        // Updated code

        try {
            String sqlString = "insert into attraction (Name, CityID, Description ) VALUES(?,(SELECT CityID from city where name = ?),?)";

            PreparedStatement stmt = conn.prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, city);
            stmt.setString(3, description);
            stmt.executeUpdate();

            ResultSet returnPrimaryKey = stmt.getGeneratedKeys();

            if (returnPrimaryKey.next()) {
                primaryKeyDB = returnPrimaryKey.getInt(1);
            }

            Map<String, Integer> pairList = getTagsId();

            if (!tags.isEmpty()) {
                for (String tag : tags) {

                    String sqlString2 = "insert into attraction_tag (AttractionID, tagID) VALUES (?, ?)";

                    PreparedStatement stmt2 = conn.prepareStatement(sqlString2);
                    stmt2.setInt(1, primaryKeyDB);
                    stmt2.setInt(2, pairList.get(tag));
                    stmt2.executeUpdate();
                }
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    public String updateAttraction(String attractionName, TouristAttraction update)  {
        String message = "nothing was updated";

        try {
            String sqlAttractionID = "SELECT attractionID FROM attraction WHERE name = ?";
            PreparedStatement stmt = conn.prepareStatement(sqlAttractionID);
            stmt.setString(1,attractionName);
            ResultSet rls = stmt.executeQuery();

            rls.next();

            int attractionID = rls.getInt(1);

            Map<String, Integer> cityPairList = this.getCityId();

            int cityInt = cityPairList.get(update.getCity());


            String sqlUpdateAttraction = "UPDATE attraction SET name = ? , description = ?, cityID = ? WHERE attractionID = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sqlUpdateAttraction);
            stmt2.setString(1, update.getName());
            stmt2.setString(2, update.getDescription());
            stmt2.setInt(3, cityInt);
            stmt2.setInt(4, attractionID);

            stmt2.executeUpdate();


            Map<String, Integer> pairList = getTagsId();

            if(!update.getTags().isEmpty()){

                String sqlDeleteTags ="DELETE FROM attraction_tag where attractionID = ?";
                PreparedStatement stmt3 = conn.prepareStatement(sqlDeleteTags);
                stmt3.setInt(1, attractionID);
                stmt3.executeUpdate();

                for(String tag : update.getTags()){

                    String sqlString3 = "insert into attraction_tag (AttractionID, tagID) VALUES (?, ?)";

                    PreparedStatement stmt4 = conn.prepareStatement(sqlString3);
                    stmt4.setInt(1, attractionID);
                    stmt4.setInt(2,pairList.get(tag));
                    stmt4.executeUpdate();
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    public String deleteAttraction(String name){
        int primaryKeyDB = 0;


        try {
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


            String resetAutoPrimaryKey = "SELECT IFNULL(MAX(attractionID), 0) + 1 AS next_id FROM attraction";
            PreparedStatement autoIncremt = conn.prepareStatement(resetAutoPrimaryKey);
            ResultSet rls = autoIncremt.executeQuery();
            rls.next();
            int nextPrimaryKey = rls.getInt(1);

            String setPrimaryKeyValue = "ALTER TABLE attraction AUTO_INCREMENT = ?";
            PreparedStatement autoSet = conn.prepareStatement(setPrimaryKeyValue);
            autoSet.setInt(1,nextPrimaryKey);
            autoSet.executeUpdate();


            return  "object was deleted";

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<TouristAttraction> getAttractions() {
        List<TouristAttraction> touristAttractions = new ArrayList<>();
        String sql = "SELECT a.Name as attraction_name, a.Description, c.Name as city_name, t.Name as tag_name from Attraction a " +
                "left join City c on a.CityID = c.CityID " +
                "left join Attraction_Tag at on a.AttractionID = at.AttractionID " +
                "left join Tags t on at.TagID = t.TagID";

        try {
            Statement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql);


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
        TouristAttraction  attraction = null;

        try {
            String sql = "SELECT a.Name as attraction_name, a.Description, c.Name as city_name, t.Name as tag_name " +
                    "FROM Attraction a " +
                    "LEFT JOIN City c ON a.CityID = c.CityID " +
                    "LEFT JOIN Attraction_Tag at ON a.AttractionID = at.AttractionID " +
                    "LEFT JOIN Tags t ON at.TagID = t.TagID " +
                    "where a.Name = ?";

            PreparedStatement preparedStatement= conn.prepareStatement(sql);
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


    /// ***************************** Helper function to get infomration ************************* ///

    // *** Helper function to get out tagsID and cityID for updating it in DB *** //
    public Map<String, Integer> getTagsId(){
        Map<String, Integer> pairList = new HashMap<>();

        try {

            String sqlString = "SELECT * FROM tags";
            PreparedStatement stmt = conn.prepareStatement(sqlString);

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

    public Map<String, Integer> getCityId(){
        Map<String, Integer> pairList = new HashMap<>();

        try {

            String sqlString = "SELECT * FROM city";
            PreparedStatement stmt = conn.prepareStatement(sqlString);

            ResultSet rls = stmt.executeQuery();


            while(rls.next()){
                String name = rls.getString("name");
                int cityID = rls.getInt("cityID");

                pairList.put(name,cityID);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return pairList;
    }

    // *** Thymeleaf functions to get lists from DB *** //
    public List<String> getListOfCities(){
        List<String> list = new ArrayList<>();

        try{

            String sqlString = "SELECT name FROM city";

            PreparedStatement stmt = conn.prepareStatement(sqlString);
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

        try{

            String sqlString = "SELECT name FROM tags";

            PreparedStatement stmt = conn.prepareStatement(sqlString);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                list.add(rs.getString("name"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean checkIfObjectExists(String name) {
        String sql = "SELECT COUNT(*) FROM attraction WHERE name = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }



}
