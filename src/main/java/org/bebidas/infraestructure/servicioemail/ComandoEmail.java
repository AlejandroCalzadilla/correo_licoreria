package org.bebidas.infraestructure.servicioemail;

import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bebidas.modules.mail.crud_seleccion.Crear;
import org.bebidas.modules.mail.crud_seleccion.Listar;

public class ComandoEmail {
    // private CommandHelp commandHelp = new CommandHelp();
    public String evaluarYEjecutar(String subject) throws SQLException {
        // Decodificar el subject si viene en formato MIME encoded-word
        // (=?UTF-8?Q?...?=)
        subject = decodeMimeSubject(subject);
        System.out.println("Subject decodificado: " + subject);
        if (subject != null && subject.trim().equalsIgnoreCase("HELP")) {
            // return CommandHelp.obtenerComandosDisponibles();
            return CommandHelpHTML.obtenerComandosDisponibles();
        }
        // Verificar si es un comando de reporte DASHBOARD
        /*
         * if (subject.equalsIgnoreCase("REPORTEDASHBOARD")) {
         * return wrapInHTML(ejecutarReporteDashboard());
         * }
         */

        String respuestaConsulta;

        // Definir patrones para cada operación CRUD en modo CASE_INSENSITIVE
        Pattern listarPatron = Pattern.compile("^LISTAR([A-Z]+)\\[\\*\\]$", Pattern.CASE_INSENSITIVE); // Ej: LISTARCLIENTES[*]
        Pattern crearPatron = Pattern.compile("^CREATE([A-Z]+)\\[(.+)\\]$", Pattern.CASE_INSENSITIVE); // Ej: CREATECLIENTES[nombre, apellido, otros]
        Pattern actualizarPatron = Pattern.compile("^UPDATE([A-Z]+)\\[(.+)\\]$", Pattern.CASE_INSENSITIVE); // Ej: UPDATECLIENTES[param1, param2]
        Pattern eliminarPatron = Pattern.compile("^DELETE([A-Z]+)\\[(.+)\\]$", Pattern.CASE_INSENSITIVE); // Ej: DELETECLIENTES[id]
        Pattern getPatron = Pattern.compile("^GET([A-Z]+)\\[(\\d+)\\]$", Pattern.CASE_INSENSITIVE); // Ej: GETMEDICAMENTOS[2]
        Pattern reportePatron = Pattern.compile("^REPORTE([A-Z]+)\\[(.+)\\]$", Pattern.CASE_INSENSITIVE); // Ej: REPORTEINGRESOS[2025, 10]

        Matcher matcher;
        System.out.println("Evaluando subject: " + subject);
        // Evaluar cada patrón
        if (subject != null && (matcher = listarPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1).toUpperCase();
            respuestaConsulta = ejecutarConsultaListar(entidad);
        } else if (subject != null && (matcher = crearPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1).toUpperCase();
            String parametros = matcher.group(2);
            respuestaConsulta = ejecutarConsultaCrear(entidad, parametros);
        } else if (subject != null && (matcher = actualizarPatron.matcher(subject)).matches()) {
            System.out.println("Patron de actualizacion coincide");
            String entidad = matcher.group(1).toUpperCase();
            String parametros = matcher.group(2);
            respuestaConsulta = ejecutarConsultaActualizar(entidad, parametros);
        } else if (subject != null && (matcher = eliminarPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1).toUpperCase();
            String id = matcher.group(2);
            respuestaConsulta = ejecutarConsultaEliminar(entidad, id);
        } else if (subject != null && (matcher = getPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1).toUpperCase();
            String id = matcher.group(2);
            respuestaConsulta = ejecutarConsultaGet(entidad, id);
        } else if (subject != null && (matcher = reportePatron.matcher(subject)).matches()) {
            String tipoReporte = matcher.group(1).toUpperCase();
            String parametros = matcher.group(2);
            // respuestaConsulta = ejecutarReporte(tipoReporte, parametros);
            respuestaConsulta = " wrapInHTML(respuestaConsulta);";
        } else {
            respuestaConsulta = "Comando no reconocido.";
        }

        System.out.println("Respuesta consulta: " + respuestaConsulta);
        return wrapInHTML(respuestaConsulta);
    }

    /**
     * Envuelve el contenido de texto en una estructura HTML completa
     */
    private String wrapInHTML(String content) {
        // Si el contenido ya es HTML completo, devolverlo tal cual
        if (content != null && content.trim().toLowerCase().startsWith("<!doctype html>")) {
            return content;
        }

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head>\n");
        html.append("<meta charset='UTF-8'>\n");
        html.append("<style>\n");
        html.append("body { font-family: 'Courier New', monospace; background-color: #f4f4f4; padding: 20px; }\n");
        html.append(
                ".container { max-width: 1200px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
        html.append(
                "pre { background: #f8f9fa; padding: 15px; border-radius: 5px; border-left: 4px solid #3498db; overflow-x: auto; white-space: pre-wrap; word-wrap: break-word; }\n");
        html.append("h1, h2, h3 { color: #2c3e50; }\n");
        html.append(".success { color: #27ae60; font-weight: bold; }\n");
        html.append(".error { color: #e74c3c; font-weight: bold; }\n");
        html.append(".info { color: #3498db; font-weight: bold; }\n");
        html.append("</style>\n");
        html.append("</head>\n<body>\n");
        html.append("<div class='container'>\n");
        html.append("<pre>");
        html.append(escapeHTML(content));
        html.append("</pre>\n");
        html.append("</div>\n</body>\n</html>");

        return html.toString();
    }

    /**
     * Escapa caracteres especiales HTML
     */
    private String escapeHTML(String text) {
        if (text == null)
            return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /**
     * Decodifica un subject que viene en formato MIME encoded-word
     * Ejemplo: =?UTF-8?Q?UPDATECATEGORIAS=5B1=2Chasf=C3=B1=2Cdasdsads=5D?=
     * Se convierte en: UPDATECATEGORIAS[1,hasfñ,dasdsads]
     */
    private String decodeMimeSubject(String subject) {
        if (subject == null || !subject.contains("=?")) {
            return subject;
        }

        try {
            // Patrón para detectar formato MIME encoded-word:
            // =?charset?encoding?encoded-text?=
            Pattern pattern = Pattern.compile("=\\?([^?]+)\\?([QB])\\?([^?]+)\\?=", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(subject);

            StringBuffer result = new StringBuffer();
            while (matcher.find()) {
                String charset = matcher.group(1);
                String encoding = matcher.group(2).toUpperCase();
                String encodedText = matcher.group(3);

                String decoded;
                if (encoding.equals("Q")) {
                    // Quoted-Printable decoding
                    decoded = decodeQuotedPrintable(encodedText);
                } else if (encoding.equals("B")) {
                    // Base64 decoding
                    decoded = new String(java.util.Base64.getDecoder().decode(encodedText), charset);
                } else {
                    decoded = encodedText;
                }
                matcher.appendReplacement(result, Matcher.quoteReplacement(decoded));
            }
            matcher.appendTail(result);

            return result.toString();
        } catch (Exception e) {
            System.err.println("Error decodificando subject: " + e.getMessage());
            return subject;
        }
    }

    /**
     * Decodifica texto en formato Quoted-Printable
     * Ejemplo: =5B se convierte en [, =C3=B1 se convierte en ñ
     */
    private String decodeQuotedPrintable(String text) {
        try {
            StringBuilder result = new StringBuilder();
            int i = 0;

            while (i < text.length()) {
                char c = text.charAt(i);

                if (c == '=') {
                    if (i + 2 < text.length()) {
                        String hex = text.substring(i + 1, i + 3);
                        try {
                            int value = Integer.parseInt(hex, 16);
                            result.append((char) value);
                            i += 3;
                        } catch (NumberFormatException e) {
                            result.append(c);
                            i++;
                        }
                    } else {
                        result.append(c);
                        i++;
                    }
                } else if (c == '_') {
                    // En Quoted-Printable, _ representa espacio
                    result.append(' ');
                    i++;
                } else {
                    result.append(c);
                    i++;
                }
            }

            // Convertir bytes a String UTF-8
            byte[] bytes = new byte[result.length()];
            for (int j = 0; j < result.length(); j++) {
                bytes[j] = (byte) result.charAt(j);
            }
            return new String(bytes, "UTF-8");

        } catch (Exception e) {
            System.err.println("Error en decodeQuotedPrintable: " + e.getMessage());
            return text;
        }
    }

    // Métodos para ejecutar consultas CRUD simuladas
    private String ejecutarConsultaListar(String entidad) throws SQLException {
        try {
            Listar listar = new Listar();
            return listar.ejecutarConsultaListar(entidad);
        } catch (Exception e) {
            return "Error al obtener listado de " + entidad + ": " + e.getMessage();
        }
    }

    private String ejecutarConsultaCrear(String entidad, String parametros) throws SQLException {
        try {
            Crear crear = new Crear();
            return crear.ejecutarCrear(entidad, parametros);
        } catch (Exception e) {
            return "Error al crear " + entidad + ": " + e.getMessage();
        }
    }

    private String ejecutarConsultaActualizar(String entidad, String parametros) {
        String respuesta = "";
        try {
            String[] params = parametros.split(",");
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].trim();
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al actualizar " + entidad + ": " + e.getMessage();
        }
    }

    private String ejecutarConsultaEliminar(String entidad, String id) {
        int entityId = Integer.parseInt(id);
        if (entityId <= 0)
            return "ID inválido";
        try {
            String respuesta = "";
            return respuesta;
        } catch (Exception e) {
            return "Error al eliminar " + entidad + ": " + e.getMessage();
        }
    }

    private String ejecutarConsultaGet(String entidad, String stringId) {
        int id = Integer.parseInt(stringId);
        if (id <= 0)
            return "ID inválido";
        try {
            String respuesta = "";
            return respuesta;
        } catch (Exception e) {
            return "Error al obtener " + entidad + ": " + e.getMessage();
        }
    }

}
