package org.bebidas.core.util;

import java.sql.SQLException;

public final class MensajesError {

    public static final String PREFIJO_DETALLE = "Detalle del error: ";

    private static final String RESET = "\u001B[0m";
    private static final String NEGRITA = "\u001B[1m";
    private static final String ROJO = "\u001B[31m";
    private static final String ROJO_FONDO = "\u001B[41m";
    private static final String AMARILLO = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    private MensajesError() {
    }

    public static String paraCliente(String contexto, Throwable e) {
        return "No se pudo " + contexto + ". Intentá nuevamente o contactá al administrador.\n"
                + PREFIJO_DETALLE + traducir(e);
    }

    public static String paraCliente(Throwable e) {
        return paraCliente("completar la operación", e);
    }

    private static Throwable desenrollarCausa(Throwable e) {
        Throwable causa = e;
        while (causa.getCause() != null && causa.getCause() != causa) {
            causa = causa.getCause();
        }
        return causa;
    }

    public static String traducir(Throwable e) {
        if (e == null) {
            return "Error desconocido.";
        }
        Throwable causa = desenrollarCausa(e);
        String msg = causa.getMessage() == null ? "" : causa.getMessage();
        String bajo = msg.toLowerCase();

        if (causa instanceof java.net.UnknownHostException
                || bajo.contains("unknown host")
                || bajo.contains("temporary failure in name resolution")
                || bajo.contains("no address associated with hostname")) {
            return "No se pudo resolver el servidor. Revisá que la IP o el dominio estén bien configurados.";
        }
        if (causa instanceof java.net.SocketTimeoutException
                || bajo.contains("timed out")
                || bajo.contains("timeout")) {
            return "Se agotó el tiempo de espera al conectar con el servidor.";
        }
        if (bajo.contains("relaying denied")
                || bajo.contains("relay access denied")
                || bajo.contains("ip name lookup failed")) {
            return "El servidor rechazó el envío del correo (Relaying denied): tu IP no está autorizada para usar el servidor. "
                    + "Contactá al administrador del servidor para que habilite tu rango de IP.";
        }
        if (causa instanceof java.net.ConnectException
                || bajo.contains("refused")
                || bajo.contains("connection attempt failed")
                || bajo.contains("no route to host")) {
            return "Conexión rechazada por el servidor. Revisá la IP, el puerto y que el servicio esté activo.";
        }
        if (bajo.contains("unreachable")) {
            return "La red no está disponible o el servidor está inaccesible.";
        }
        if (causa instanceof java.net.SocketException
                || bajo.contains("broken pipe")
                || bajo.contains("socket closed")
                || bajo.contains("connection reset")) {
            return "La conexión con el servidor se cortó de forma inesperada.";
        }
        if (causa instanceof SQLException) {
            SQLException sqle = (SQLException) causa;
            String estado = sqle.getSQLState();
            if ("28P01".equals(estado)
                    || bajo.contains("password authentication failed")
                    || bajo.contains("password fall")
                    || bajo.contains("autentificaci")) {
                return "Autenticación de base de datos fallida. Revisá el usuario y la contraseña.";
            }
            if ("3D000".equals(estado)
                    || bajo.contains("does not exist")
                    || bajo.contains("no existe")) {
                return "La base de datos indicada no existe en el servidor.";
            }
            return "Ocurrió un error de base de datos: " + (msg.isEmpty() ? estado : msg);
        }
        return "Ocurrió un error inesperado: " + (msg.isEmpty() ? causa.getClass().getSimpleName() : msg);
    }

    public static boolean esErrorDeRed(Throwable e) {
        if (e == null) {
            return false;
        }
        Throwable causa = desenrollarCausa(e);
        String msg = causa.getMessage() == null ? "" : causa.getMessage().toLowerCase();
        return causa instanceof java.net.UnknownHostException
                || causa instanceof java.net.ConnectException
                || causa instanceof java.net.SocketException
                || causa instanceof java.net.SocketTimeoutException
                || msg.contains("refused")
                || msg.contains("unknown host")
                || msg.contains("temporary failure in name resolution")
                || msg.contains("unreachable")
                || msg.contains("timed out")
                || msg.contains("no route to host")
                || msg.contains("no address associated with hostname")
                || msg.contains("relaying denied")
                || msg.contains("relay access denied")
                || msg.contains("ip name lookup failed");
    }

    public static void imprimirErrorTerminal(String titulo, Throwable e) {
        boolean esRed = esErrorDeRed(e);
        String detalle = traducir(e);
        String crudo = e == null ? "-" : e.toString();
        String borde = "=".repeat(esRed ? 82 : 66);
        String colorBorde = esRed ? ROJO_FONDO + NEGRITA + ROJO : ROJO;
        String tituloFinal = esRed ? "  !!!  " + titulo + "  !!!" : "  " + titulo;

        System.err.println();
        System.err.println(colorBorde + borde + RESET);
        System.err.println(colorBorde + tituloFinal + RESET);
        System.err.println(colorBorde + borde + RESET);
        System.err.println(AMARILLO + "Detalle: " + detalle + RESET);
        System.err.println(CYAN + "Técnico: " + crudo + RESET);
        System.err.println(colorBorde + borde + RESET);
        System.err.println();
    }
}
