package org.bebidas.conexion;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            loadConfig();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void loadConfig() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
                URL = props.getProperty("db.url", "jdbc:postgresql://localhost:5432/bebidas_db");
                USER = props.getProperty("db.user", "postgres");
                PASSWORD = props.getProperty("db.password", "password");
            } else {
                // Valores por defecto si no se encuentra el archivo
                URL = "jdbc:postgresql://localhost:5432/bebidas_db";
                USER = "postgres";
                PASSWORD = "password";
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Valores por defecto en caso de error
            URL = "jdbc:postgresql://localhost:5432/bebidas_db";
            USER = "postgres";
            PASSWORD = "password";
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}