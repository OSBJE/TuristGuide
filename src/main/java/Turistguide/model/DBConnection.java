package Turistguide.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private String URL;
    private String Admin;
    private String Password;

    public DBConnection(){
    }

    public Connection getConnection(String URL, String Admin, String Password){
        Connection conn = null;


        try {
            Connection contemp = DriverManager.getConnection(URL,Admin,Password);
            conn = contemp;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }
}
