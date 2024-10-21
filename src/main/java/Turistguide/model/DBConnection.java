package Turistguide.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;


    private DBConnection(){
    }

    public static Connection getConnection(String URL, String Admin, String Password){

        if (conn != null){return conn;}

        try {
            Connection contemp = DriverManager.getConnection(URL,Admin,Password);
            conn = contemp;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }
}
