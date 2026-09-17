package main.java.com.fammateam.gestionresidencial.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

    private static Connection connection;

    private DataBaseConnection() {
    }

    public static Connection getConnectionDataBase() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(Credentials.URL_DB, Credentials.USER_DB, Credentials.PASS_DB);
        }
        return connection;
    }

}
