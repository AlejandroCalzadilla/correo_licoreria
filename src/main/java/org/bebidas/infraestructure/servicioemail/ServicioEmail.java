package org.bebidas.infraestructure.servicioemail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bebidas.core.util.MensajesError;

public class ServicioEmail {
    private boolean conectado;
    private ClientePOP clientePOP;
    private ClienteSMTP clienteSMTP;
    private ClienteSMTPGoogle clienteSMTPGoogle;
    private boolean usarSoloTecnoweb;
    private ComandoEmail comandoEmail;

    public ServicioEmail() {
        this.conectado = true;
        this.clientePOP = new ClientePOP();
        this.clienteSMTP = new ClienteSMTP();
        this.usarSoloTecnoweb = false;
        try {
            this.clienteSMTPGoogle = ClienteSMTPGoogle.fromConfig();
            System.out.println("S : SMTP Google habilitado para pruebas.");
        } catch (IllegalStateException e) {
            this.clienteSMTPGoogle = null;
            System.out.println("S : SMTP Google no configurado, se usará SMTP por defecto.");
        }
        this.comandoEmail = new ComandoEmail();
    }

    public void revisarCorreos() throws IOException, SQLException {
        clientePOP.conectar();
        int totalCorreos = clientePOP.obtenerTotalDeCorreos();
        for (int i = 1; i <= totalCorreos; i++) {
            //String correo = clientePOP.obtenerCorreo(i);
            String correo = clientePOP.obtenerCorreoYEliminar(i);
            guardarCorreo(correo);
            evaluarYResponderCorreo(correo);
        }
        clientePOP.desconectar();
    }

    private void guardarCorreo(String correo) {
        String messageId = extraerMessageId(correo);
        // TODO: Guardar el correo en la base de datos
    }

    public void detener() {
        conectado = false;
        System.out.println("S : El cliente POP se ha detenido automáticamente.");
    }

    public static String extraerMessageId(String correo) {
        for (String line : correo.split("\n")) {
            if (line.startsWith("Message-ID:")) {
                return line.substring(11).trim();
            }
        }
        return null;
    }

    private String extraerRemitente(String correo) {
        for (String line : correo.split("\n")) {
            if (line.startsWith("Return-Path:")) {
                int start = line.indexOf('<') + 1;
                int end = line.indexOf('>');
                if (start > 0 && end > start) {
                    return line.substring(start, end).trim();
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException, SQLException {
        ServicioEmail servicioEmail = new ServicioEmail();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Seleccione proveedor SMTP:");
        System.out.println("1) TecnoWeb");
        System.out.println("2) Google (si está configurado)");
        System.out.print("Opción: ");
        String opcion = scanner.nextLine().trim();

        if ("1".equals(opcion)) {
            servicioEmail.usarSoloTecnoweb = true;
            System.out.println("S : Proveedor seleccionado: TecnoWeb");
        } else {
            servicioEmail.usarSoloTecnoweb = false;
            if (servicioEmail.clienteSMTPGoogle != null) {
                System.out.println("S : Proveedor seleccionado: Google");
            } else {
                System.out.println("S : Google no está configurado, se usará TecnoWeb.");
            }
        }
        //servicioEmail.revisarCorreos();

        // Programar el apagado automático después de 600 segundos
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(servicioEmail::detener, 600, TimeUnit.SECONDS);
        while (servicioEmail.conectado) {
            try {
                servicioEmail.revisarCorreos();
            } catch (IOException e) {
                MensajesError.imprimirErrorTerminal("ERROR DE CONEXIÓN CON EL SERVIDOR POP", e);
            } catch (SQLException e) {
                MensajesError.imprimirErrorTerminal("ERROR DE BASE DE DATOS AL REVISAR CORREOS", e);
            }

            try {
                Thread.sleep(10000); // Esperar 10 segundos entre revisiones
            } catch (InterruptedException e) {
                System.out.println("Interrupción en el ciclo de revisión de correos: " + e.getMessage());
            }
        }
        scheduler.shutdown();
    }

    private void evaluarYResponderCorreo(String correo) throws SQLException {
        String subject = extraerSubject(correo);
        String remitente = extraerRemitente(correo);
        if (subject != null && !subject.isEmpty() && remitente != null) {
            System.out.println("S : Procesando correo con subject: " + subject + " de: " + remitente);
            System.out.println("Evaluando subject: " + subject);
            String respuesta = procesarCorreo(subject);
            if (respuesta != null && !respuesta.isEmpty()) {
                System.out.println("Respuesta consulta: " + respuesta);
                if (!usarSoloTecnoweb && clienteSMTPGoogle != null) {
                    clienteSMTPGoogle.enviarCorreo(remitente, "Resultado de la Consulta", respuesta);
                } else {
                    clienteSMTP.enviarCorreo(remitente, "Resultado de la Consulta", respuesta);
                }
            } else {
                System.out.println("No se generó respuesta para el subject: " + subject);
            }
        } else {
            System.out.println("S : Correo ignorado - Subject vacío o sin remitente válido");
        }
    }

    private String extraerSubject(String correo) {
        StringBuilder subjectBuilder = new StringBuilder();
        boolean subjectFound = false;

        for (String line : correo.split("\n")) {
            if (line.startsWith("Subject:")) {
                subjectBuilder.append(line.substring(8).trim());
                subjectFound = true;
            } else if (subjectFound && (line.startsWith(" ") || line.startsWith("\t"))) {
                subjectBuilder.append(" ").append(line.trim());
            } else if (subjectFound) {
                break;
            }
        }
        
        String rawSubject = subjectBuilder.toString();
        String decodedSubject = decodificarMIME(rawSubject);
        System.out.println("Subject decodificado: " + decodedSubject);
        return decodedSubject;
    }

    /**
     * Decodifica subjects codificados en formato MIME (RFC 2047)
     * Soporta Base64 (=?charset?B?...?=) y Quoted-Printable (=?charset?Q?...?=)
     */
    private String decodificarMIME(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return encoded;
        }
        
        // Patrón para detectar encoded-words: =?charset?encoding?encoded_text?=
        Pattern pattern = Pattern.compile("=\\?([^?]+)\\?([BbQq])\\?([^?]*)\\?=");
        Matcher matcher = pattern.matcher(encoded);
        StringBuffer decoded = new StringBuffer();
        
        while (matcher.find()) {
            String charset = matcher.group(1);
            String encoding = matcher.group(2).toUpperCase();
            String text = matcher.group(3);
            
            String decodedText;
            try {
                if ("B".equals(encoding)) {
                    // Base64 decoding
                    byte[] bytes = Base64.getDecoder().decode(text);
                    decodedText = new String(bytes, charset);
                } else if ("Q".equals(encoding)) {
                    // Quoted-Printable decoding
                    decodedText = decodeQuotedPrintable(text, charset);
                } else {
                    decodedText = text;
                }
            } catch (Exception e) {
                System.out.println("Error decodificando MIME: " + e.getMessage());
                decodedText = text;
            }
            
            matcher.appendReplacement(decoded, Matcher.quoteReplacement(decodedText));
        }
        matcher.appendTail(decoded);
        
        // Eliminar espacios entre encoded-words consecutivos
        return decoded.toString().replaceAll("\\s+", " ").trim();
    }
    
    /**
     * Decodifica texto en formato Quoted-Printable
     */
    private String decodeQuotedPrintable(String text, String charset) throws UnsupportedEncodingException {
        // En QP para headers, los underscores representan espacios
        text = text.replace("_", " ");
        
        StringBuilder result = new StringBuilder();
        byte[] bytes = new byte[text.length()];
        int byteIndex = 0;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '=' && i + 2 < text.length()) {
                try {
                    int hex = Integer.parseInt(text.substring(i + 1, i + 3), 16);
                    bytes[byteIndex++] = (byte) hex;
                    i += 2;
                } catch (NumberFormatException e) {
                    bytes[byteIndex++] = (byte) c;
                }
            } else {
                bytes[byteIndex++] = (byte) c;
            }
        }
        
        return new String(bytes, 0, byteIndex, charset);
    }

    private String extraerSubjectOld(String correo) {
        for (String line : correo.split("\n")) {
            if (line.startsWith("Subject:")) {
                return line.substring(9).trim();
            }
        }
        return null;
    }

    public String procesarCorreo(String subject) throws SQLException {
        return comandoEmail.evaluarYEjecutar(subject);
       //return null;
    }

    private static String parsearQuery(String subject) {
        // Implementa la lógica para parsear el subject y generar la consulta SQL
        // Por ejemplo, si el subject es "PATTERN: SELECT * FROM users"
        if (subject.startsWith("PATTERN:")) {
            return subject.substring(8).trim();
        }
        return null;
    }
}
