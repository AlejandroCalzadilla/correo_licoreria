package org.bebidas.infraestructure.servicioemail;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.bebidas.core.util.MensajesError;

public class ClientePOP {
    static final String HOST = "mail.tecnoweb.org.bo";
    static final int PORT = 110;
    static final String USER = "grupo22sa";
    static final String PASS = "grup022grup022*";
    static final String DB_URL = "jdbc:postgresql://mail.tecnoweb.org.bo/db_agenda";
    static final String DB_USER = "agenda";
    static final String DB_PASS = "agendaagenda";

    private Socket socket;
    private BufferedReader entrada;
    private DataOutputStream salida;

    public void conectar() throws IOException {
        try {
            socket = new Socket(HOST, PORT);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new DataOutputStream(socket.getOutputStream());
            System.out.println("S : " + entrada.readLine());

            enviarComando(salida, entrada, "USER " + USER + "\r\n");
            enviarComando(salida, entrada, "PASS " + PASS + "\r\n");
        } catch (java.net.SocketException e) {
            MensajesError.imprimirErrorTerminal("ERROR AL CONECTAR AL SERVIDOR POP", e);
            throw new IOException("No se pudo conectar al servidor POP (Socket reset/refused)", e);
        } catch (IOException e) {
            MensajesError.imprimirErrorTerminal("ERROR DE E/S AL CONECTAR AL SERVIDOR POP", e);
            throw e;
        }
    }

    private static String enviarComando(DataOutputStream salida, BufferedReader entrada, String comando) throws IOException {
        try {
            System.out.print("C : " + comando);
            salida.writeBytes(comando);
            String response;
            if (comando.startsWith("RETR") || comando.startsWith("LIST")) {
                response = leerRespuestaMultilinea(entrada);
                if (comando.startsWith("RETR")) {
                    //evaluarCorreo(response);
                }
                return response;
            }
            return entrada.readLine();
        } catch (java.net.SocketException e) {
            MensajesError.imprimirErrorTerminal("ERROR DE CONEXIÓN POP - SOCKET CERRADO O RESET", e);
            throw new IOException("Socket cerrado o reset por el servidor: " + e.getMessage(), e);
        } catch (IOException e) {
            MensajesError.imprimirErrorTerminal("ERROR DE E/S EN LA COMUNICACIÓN POP", e);
            throw e;
        }
    }

    static protected String leerRespuestaMultilinea(BufferedReader in) throws IOException {
        StringBuilder lines = new StringBuilder();
        while (true) {
            String line = in.readLine();
            if (line == null) throw new IOException("S : El servidor cerró la conexión inesperadamente.");
            if (line.equals(".")) break;
            if (line.startsWith(".")) line = line.substring(1);
            lines.append("\n").append(line);
        }
        return lines.toString();
    }

    private static void evaluarCorreo(String correo) {
        String subject = extraerSubject(correo);
        if (subject != null) {
            ejecutarConsulta(subject);
        }
    }

    private static String extraerSubject(String correo) {
        for (String line : correo.split("\n")) {
            if (line.startsWith("Subject:")) {
                return line.substring(8).trim();
            }
        }
        return null;
    }

    private static void ejecutarConsulta(String subject) {
        String query = parsearQuery(subject);
        if (query != null && subject.contains("PATTERN")) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                StringBuilder queryResult = new StringBuilder();
                int rowCount = 1;

                while (rs.next()) {
                    queryResult.append("Row ").append(rowCount).append(": ");
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        queryResult.append(rs.getString(i).trim()).append(" ");
                    }
                    queryResult.append("\r\n");
                    rowCount++;
                }
                //ClienteSMTP.enviarCorreo(SMTP_SERVER, SMTP_PORT, SMTP_USER_EMISOR, SMTP_USER_RECEPTOR, "Resultado de la Consulta", queryResult.toString());
                System.out.print(queryResult.toString());
            } catch (Exception e) {
                MensajesError.imprimirErrorTerminal("ERROR AL EJECUTAR CONSULTA EN LA BASE DE DATOS AGENDA", e);
            }
        }
    }

    private static String parsearQuery(String subject) {
        // Implementa la lógica para parsear el subject y generar la consulta SQL
        // Por ejemplo, si el subject es "PATTERN: SELECT * FROM users"
        if (subject.startsWith("PATTERN:")) {
            return subject.substring(8).trim();
        }
        return null;
    }

    public int obtenerTotalDeCorreos() throws IOException {
        String response = enviarComando(salida, entrada, "STAT\r\n");
        response = response.substring(4, response.length());
        int i = 1;
        while (response.charAt(i) != ' ') {
            i++;
        }
        response = response.substring(0, i);
        return Integer.parseInt(response);
    }

    public void desconectar() {
        try {
            enviarComando(salida, entrada, "QUIT\r\n");
            entrada.close();
            salida.close();
            socket.close();
            System.out.println("S : Conexión finalizada.");
        } catch (IOException e) {
            MensajesError.imprimirErrorTerminal("ERROR AL CERRAR LA CONEXIÓN POP", e);
        }
    }

    public void revisarCorreos() {
        try {
            conectar();
            String x;
            x = enviarComando(salida, entrada, "LIST\r\n");
            System.out.println(x);
            desconectar();
        } catch (Exception e) {
            MensajesError.imprimirErrorTerminal("ERROR AL REVISAR CORREOS POP", e);
        }
    }

    public String obtenerCorreo(int posicion) {
        try {
            return enviarComando(salida, entrada, "RETR " + posicion + "\r\n");
        } catch (IOException e) {
            MensajesError.imprimirErrorTerminal("ERROR AL OBTENER EL CORREO POP", e);
            return null;
        }
    }

    public String obtenerCorreoYEliminar(int posicion) {
        try {
            String correo = enviarComando(salida, entrada, "RETR " + posicion + "\r\n");
            enviarComando(salida, entrada, "DELE " + posicion + "\r\n");
            return correo;
        } catch (IOException e) {
            MensajesError.imprimirErrorTerminal("ERROR AL OBTENER EL CORREO POP", e);
            return null;
        }
    }

    public void eliminarTodosLosCorreos() throws IOException {
        try {
            conectar();
            int totalCorreos = obtenerTotalDeCorreos();
            System.out.println("Marcando " + totalCorreos + " correos para eliminación...");

            for (int i = 1; i <= totalCorreos; i++) {
                enviarComando(salida, entrada, "DELE " + i + "\r\n");
                System.out.println("Correo " + i + " marcado para eliminación");
            }

            desconectar();
            System.out.println("Todos los correos han sido marcados para eliminación y la conexión cerrada.");
        } catch (IOException e) {
            MensajesError.imprimirErrorTerminal("ERROR AL ELIMINAR CORREOS POP", e);
            throw e;
        }
    }

    public static void main(String[] args) {
        ClientePOP cliente = new ClientePOP();

        try {
            System.out.println("Iniciando eliminación de todos los correos...");
            cliente.eliminarTodosLosCorreos();
            System.out.println("Eliminación completada exitosamente.");
        } catch (IOException e) {
            MensajesError.imprimirErrorTerminal("ERROR DURANTE LA ELIMINACIÓN DE CORREOS POP", e);
        }
    }

}