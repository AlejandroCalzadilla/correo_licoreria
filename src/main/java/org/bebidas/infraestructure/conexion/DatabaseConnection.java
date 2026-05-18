package org.bebidas.infraestructure.conexion;

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
                URL ="jdbc:postgresql://mail.tecnoweb.org.bo:5432/db_grupo22sa";
                USER = "grupo22sa";
                PASSWORD = "grup022grup022*";
            } else {
                // Valores por defecto si no se encuentra el archivo
                URL = "jdbc:postgresql://mail.tecnoweb.org.bo:5432/db_grupo22sa";
                USER = "grupo22sa";
                PASSWORD = "grup022grup022*";
            } 
          /*  if (input != null) {
                props.load(input);
                URL ="jdbc:postgresql://localhost:5432/ricardo";
                USER = "postgres";
                PASSWORD = "ale12345678";
            } else {
                // Valores por defecto si no se encuentra el archivo
                URL = "jdbc:postgresql://localhost:5432/ricardo";
                USER = "postgres";
                PASSWORD = "ale12345678";
            } */

        } catch (IOException e) {
            e.printStackTrace();
            // Valores por defecto en caso de error
            URL = "jdbc:postgresql://mail.tecnoweb.org.bo:5432/db_grupo12sa";
            USER = "grupo12sa";
            PASSWORD = "grup012grup012*";
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}