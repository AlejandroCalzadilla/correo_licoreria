package org.bebidas.infraestructure.servicioemail;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServicioEmail {
    private boolean conectado;
    private ClientePOP clientePOP;
    private ClienteSMTP clienteSMTP;
    private ComandoEmail comandoEmail;

    public ServicioEmail() {
        this.conectado = true;
        this.clientePOP = new ClientePOP();
        this.clienteSMTP = new ClienteSMTP();
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
        //servicioEmail.revisarCorreos();

        // Programar el apagado automático después de 15 segundos
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(servicioEmail::detener, 320, TimeUnit.SECONDS);
        while (servicioEmail.conectado) {
            servicioEmail.revisarCorreos();

            try {
                Thread.sleep(10000); // Esperar 10 segundos entre revisiones
            } catch (InterruptedException e) {
                System.out.println("Interrupción en el ciclo de revisión: " + e.getMessage());
            }
        }
        scheduler.shutdown();
    }

    private void evaluarYResponderCorreo(String correo) throws SQLException {
        String subject = extraerSubject(correo);
        String remitente = extraerRemitente(correo);
        if (subject != null && remitente != null) {
            System.out.println("S : Procesando correo con subject: " + subject + " de: " + remitente);
            String respuesta = procesarCorreo(subject);
            if (!respuesta.isEmpty()) {
                System.out.println(respuesta);
                clienteSMTP.enviarCorreo(remitente, "Resultado de la Consulta", respuesta);
            }
        }
    }

    private String extraerSubject(String correo) {
        StringBuilder subjectBuilder = new StringBuilder();
        boolean subjectFound = false;

        for (String line : correo.split("\n")) {
            if (line.startsWith("Subject:")) {
                subjectBuilder.append(line.substring(9).trim());
                subjectFound = true;
            } else if (subjectFound && (line.startsWith(" ") || line.startsWith("\t"))) {
                subjectBuilder.append(" ").append(line.trim());
            } else if (subjectFound) {
                break;
            }
        }
        return subjectBuilder.toString();
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
