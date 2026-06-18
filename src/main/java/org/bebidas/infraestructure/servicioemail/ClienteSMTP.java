package org.bebidas.infraestructure.servicioemail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class ClienteSMTP {

    private static final String SERVIDOR = "mail.tecnoweb.org.bo";
    private static final int PUERTO = 25;
    private static final String EMISOR = "grupo22sa@tecnoweb.org.bo";

    private static void enviarComando(OutputStreamWriter salida, BufferedReader entrada, String comando)
            throws IOException {
        salida.write(comando);
        salida.flush();
        String respuesta = leerRespuesta(entrada);

        int codigoRespuesta = Integer.parseInt(respuesta.substring(0, 3));
        if (codigoRespuesta >= 400) {
            throw new IOException(
                    "No se pudo enviar el correo, error durante el comando: " + comando.trim() + ".\nRespuesta: " + respuesta);
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
            throw new IOException("S : Server closed the connection unexpectedly.");
        }
        return lines.toString();
    }

    public void enviarCorreo(String usuarioReceptor, String subject, String mensaje) {
        String dominio = obtenerDominio(usuarioReceptor);
        List<String> servidoresMX = obtenerServidoresMX(dominio);

        boolean enviado = false;
        for (String servidor : servidoresMX) {
            System.out.println("S : Intentando envío directo a MX de " + dominio + ": " + servidor);
            try (Socket socket = new Socket(servidor, 25);
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    OutputStreamWriter salida = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) {

                ejecutarEnvio(socket, entrada, salida, usuarioReceptor, subject, mensaje);
                enviado = true;
                System.out.println("S : Correo enviado exitosamente a " + usuarioReceptor + " vía MX: " + servidor);
                break;
            } catch (Exception e) {
                System.out.println("S : Falló el envío directo a " + servidor + ", error: " + e.getMessage());
            }
        }

        if (!enviado) {
            System.out.println("S : Intentando envío de respaldo a través de " + SERVIDOR);
            try (Socket socket = new Socket(SERVIDOR, PUERTO);
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                    OutputStreamWriter salida = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)) {

                ejecutarEnvio(socket, entrada, salida, usuarioReceptor, subject, mensaje);
                System.out.println("S : Correo enviado exitosamente a " + usuarioReceptor + " vía " + SERVIDOR);
            } catch (Exception e) {
                System.out.println("S : No se pudo conectar con el servidor de respaldo, error final: " + e.getMessage());
            }
        }
    }

    private void ejecutarEnvio(Socket socket, BufferedReader entrada, OutputStreamWriter salida,
            String usuarioReceptor, String subject, String mensaje) throws IOException {
        
        System.out.println("S : " + entrada.readLine());

        String mensajeFormateado = mensaje.replace("\n", "\r\n");

        enviarComando(salida, entrada, "HELO " + SERVIDOR + "\r\n");
        enviarComando(salida, entrada, "MAIL FROM:<" + EMISOR + ">\r\n");
        enviarComando(salida, entrada, "RCPT TO:<" + usuarioReceptor + ">\r\n");
        enviarComando(salida, entrada, "DATA\r\n");

        salida.write("From: " + EMISOR + "\r\n");
        salida.write("To: " + usuarioReceptor + "\r\n");
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
    }

    private String obtenerDominio(String email) {
        int index = email.indexOf('@');
        if (index != -1 && index < email.length() - 1) {
            return email.substring(index + 1).trim();
        }
        return "";
    }

    private List<String> obtenerServidoresMX(String domain) {
        List<String> mxRecords = new ArrayList<>();
        if (domain.isEmpty()) {
            return mxRecords;
        }
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ictx = new InitialDirContext(env);
            Attributes attrs = ictx.getAttributes(domain, new String[] { "MX" });
            Attribute attr = attrs.get("MX");
            if (attr != null) {
                for (int i = 0; i < attr.size(); i++) {
                    String mxAttr = (String) attr.get(i);
                    String[] parts = mxAttr.split("\\s+");
                    if (parts.length > 1) {
                        String record = parts[1];
                        if (record.endsWith(".")) {
                            record = record.substring(0, record.length() - 1);
                        }
                        mxRecords.add(record);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("S : Error resolviendo registros MX para " + domain + ": " + e.getMessage());
        }
        return mxRecords;
    }
}
