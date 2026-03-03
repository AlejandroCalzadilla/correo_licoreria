package org.bebidas.infraestructure.servicioemail;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class ClienteSMTPGoogle {

    private static final String SERVIDOR = "smtp.gmail.com";
    private static final int PUERTO = 465;

    private final String emisor;
    private final String appPassword;

    public ClienteSMTPGoogle(String emisor, String appPassword) {
        this.emisor = emisor;
        this.appPassword = appPassword;
    }

    public static ClienteSMTPGoogle fromConfig() {
        Properties props = new Properties();
        String origen = null;

        try (InputStream input = ClienteSMTPGoogle.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
                origen = "classpath:config.properties";
            }
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer config.properties del classpath: " + e.getMessage(), e);
        }

        if (origen == null) {
            Path[] rutasCandidatas = new Path[] {
                    Paths.get("config.properties"),
                    Paths.get("src/main/resources/config.properties"),
                    Paths.get("src/main/java/org/bebidas/resources/config.properties")
            };

            for (Path ruta : rutasCandidatas) {
                if (Files.exists(ruta)) {
                    try (InputStream input = new FileInputStream(ruta.toFile())) {
                        props.load(input);
                        origen = ruta.toAbsolutePath().toString();
                        break;
                    } catch (IOException e) {
                        throw new IllegalStateException("No se pudo leer " + ruta + ": " + e.getMessage(), e);
                    }
                }
            }
        }

        if (origen == null) {
            throw new IllegalStateException("No se encontró config.properties (classpath ni rutas locales).");
        }

        String user = props.getProperty("smtp.gmail.user", "").trim();
        String password = props.getProperty("smtp.gmail.appPassword", "").trim();

        if (user.isEmpty() || password.isEmpty()) {
            throw new IllegalStateException("Faltan smtp.gmail.user o smtp.gmail.appPassword en config.properties.");
        }

        System.out.println("S : SMTP Google cargado desde: " + origen);

        return new ClienteSMTPGoogle(user, password);
    }

    public void enviarCorreo(String usuarioReceptor, String subject, String mensaje) {
        try (SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(SERVIDOR, PUERTO);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                OutputStreamWriter salida = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) {

            leerYValidarRespuesta(entrada, "220", "Conexión inicial");

            enviarComando(salida, entrada, "EHLO localhost\r\n", "250", "EHLO");
            enviarComando(salida, entrada, "AUTH LOGIN\r\n", "334", "AUTH LOGIN");
            enviarComando(salida, entrada, base64(emisor) + "\r\n", "334", "Usuario SMTP");
            enviarComando(salida, entrada, base64(appPassword) + "\r\n", "235", "Password SMTP");

            enviarComando(salida, entrada, "MAIL FROM:<" + emisor + ">\r\n", "250", "MAIL FROM");
            enviarComando(salida, entrada, "RCPT TO:<" + usuarioReceptor + ">\r\n", "250", "RCPT TO");
            enviarComando(salida, entrada, "DATA\r\n", "354", "DATA");

            String mensajeFormateado = mensaje.replace("\n", "\r\n").replace("\r\n.", "\r\n..");
            salida.write("From: " + emisor + "\r\n");
            salida.write("To: " + usuarioReceptor + "\r\n");
            salida.write("Subject: " + subject + "\r\n");
            salida.write("MIME-Version: 1.0\r\n");

            if (mensaje.trim().toLowerCase().startsWith("<!doctype html>")) {
                salida.write("Content-Type: text/html; charset=utf-8\r\n");
            } else {
                salida.write("Content-Type: text/plain; charset=utf-8\r\n");
            }

            salida.write("\r\n");
            salida.write(mensajeFormateado + "\r\n.\r\n");
            salida.flush();

            leerYValidarRespuesta(entrada, "250", "Envío del mensaje");
            enviarComando(salida, entrada, "QUIT\r\n", "221", "QUIT");

            System.out.println("S : Correo enviado por Gmail a " + usuarioReceptor);
        } catch (Exception e) {
            System.out.println("S : Error enviando correo por Gmail: " + e.getMessage());
        }
    }

    private static void enviarComando(OutputStreamWriter salida, BufferedReader entrada, String comando,
            String codigoEsperado, String etapa) throws IOException {
        salida.write(comando);
        salida.flush();
        leerYValidarRespuesta(entrada, codigoEsperado, etapa);
    }

    private static void leerYValidarRespuesta(BufferedReader in, String codigoEsperado, String etapa)
            throws IOException {
        String respuesta = leerRespuesta(in);
        if (!respuesta.startsWith(codigoEsperado)) {
            throw new IOException("Fallo en " + etapa + ". Esperado " + codigoEsperado + " pero llegó: " + respuesta);
        }
    }

    private static String leerRespuesta(BufferedReader in) throws IOException {
        StringBuilder lines = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            lines.append(line).append("\r\n");
            if (line.length() > 3 && line.charAt(3) == ' ') {
                break;
            }
        }
        if (line == null) {
            throw new IOException("El servidor cerró la conexión inesperadamente.");
        }
        return lines.toString();
    }

    private static String base64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}