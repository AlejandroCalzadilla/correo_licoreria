package org.bebidas.infraestructure.conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.bebidas.core.util.MensajesError;

public class DatabaseConnection {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static boolean errorImpreso = false;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            loadConfig();
        } catch (ClassNotFoundException e) {
            MensajesError.imprimirErrorTerminal("ERROR AL CARGAR EL DRIVER DE POSTGRESQL", e);
        }
    }

    private static void loadConfig() {
        Properties props = new Properties();
        String urlDefault = "jdbc:postgresql://mail.tecnoweb.org.bo:5432/db_grupo22sa";
        String userDefault = "grupo22sa";
        String passwordDefault = "grup022grup022*";

        URL = urlDefault;
        USER = userDefault;
        PASSWORD = passwordDefault;

        boolean cargado = false;
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
                cargado = true;
            }
        } catch (IOException e) {
            MensajesError.imprimirErrorTerminal("ERROR AL LEER LA CONFIGURACIÓN DE LA BASE DE DATOS", e);
        }

        if (!cargado) {
            Path[] rutasCandidatas = new Path[] {
                    Paths.get("config.properties"),
                    Paths.get("src/main/resources/config.properties"),
                    Paths.get("src/main/java/org/bebidas/resources/config.properties")
            };

            for (Path ruta : rutasCandidatas) {
                if (Files.exists(ruta)) {
                    try (InputStream input = new FileInputStream(ruta.toFile())) {
                        props.load(input);
                        cargado = true;
                        break;
                    } catch (IOException e) {
                        MensajesError.imprimirErrorTerminal("ERROR AL LEER LA CONFIGURACIÓN DE LA BASE DE DATOS", e);
                    }
                }
            }
        }

        if (cargado) {
            URL = props.getProperty("db.url", urlDefault).trim();
            USER = props.getProperty("db.user", userDefault).trim();
            PASSWORD = props.getProperty("db.password", passwordDefault).trim();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            if (!errorImpreso) {
                errorImpreso = true;
                MensajesError.imprimirErrorTerminal("ERROR DE CONEXIÓN A LA BASE DE DATOS", e);
            }
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}