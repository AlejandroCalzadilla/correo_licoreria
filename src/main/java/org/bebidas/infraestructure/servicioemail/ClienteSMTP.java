package org.bebidas.infraestructure.servicioemail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClienteSMTP {

    private static final String SERVIDOR = "mail.tecnoweb.org.bo";
    private static final int PUERTO = 25;
    private static final String EMISOR = "grupo11sa@tecnoweb.org.bo";

    private static void enviarComando(OutputStreamWriter salida, BufferedReader entrada, String comando)
            throws IOException {
        salida.write(comando);
        salida.flush();
        String respuesta = leerRespuesta(entrada);

        int codigoRespuesta = Integer.parseInt(respuesta.substring(0, 3));
        if (codigoRespuesta >= 400) {
            throw new IOException(
                    "No se pudo enviar el correo, error durante el comando: " + comando + ".\nError" + respuesta);
        }
    }

    static protected String leerRespuesta(BufferedReader in) throws IOException {
        StringBuilder lines = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            lines.append(line).append("\r\n");
            if (line.length() > 3 && line.charAt(3) == ' ')
                break;
        }
        if (line == null) {
            throw new IOException("S : Server unawares closed the connection.");
        }
        return lines.toString();
    }

    public void enviarCorreo(String usuarioReceptor, String subject, String mensaje) {
        try (Socket socket = new Socket(SERVIDOR, PUERTO);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                OutputStreamWriter salida = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) {

            System.out.println("S : " + entrada.readLine());

            String mensajeFormateado = mensaje.replace("\n", "\r\n");

            enviarComando(salida, entrada, "HELO " + SERVIDOR + "\r\n");
            enviarComando(salida, entrada, "MAIL FROM: " + EMISOR + " \r\n");
            enviarComando(salida, entrada, "RCPT TO: " + usuarioReceptor + " \r\n");
            enviarComando(salida, entrada, "DATA\r\n");
            
            salida.write("Subject: " + subject + "\r\n");

            if (mensaje.trim().toLowerCase().startsWith("<!doctype html>")) {
                salida.write("Content-Type: text/html; charset=utf-8\r\n");
            } else {
                salida.write("Content-Type: text/plain; charset=utf-8\r\n");
            }
            
            salida.write("\r\n");
            salida.write(mensajeFormateado + "\r\n.\r\n");
            salida.flush();
            leerRespuesta(entrada);

            enviarComando(salida, entrada, "QUIT\r\n");

        } catch (Exception e) {
            System.out.println("S : No se pudo conectar con el servidor, error: " + e.getMessage());
        }
    }
}
