package org.bebidas.infraestructure.servicioemail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PagoFacilGateway {

    private String baseUrl;
    private String tokenService;
    private String tokenSecret;
    private String callbackUrl;
    private int timeout;
    private boolean enableLogs;

    private final HttpClient httpClient;

    public PagoFacilGateway() {
        loadConfig();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(java.time.Duration.ofSeconds(timeout))
                .build();
    }

    private void loadConfig() {
        Properties props = new Properties();
        String origen = null;

        try (InputStream input = PagoFacilGateway.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
                origen = "classpath:config.properties";
            }
        } catch (IOException e) {
            // Ignorar y buscar en rutas locales
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
                        // Continuar buscando
                    }
                }
            }
        }

        this.baseUrl = props.getProperty("PAGOFACIL_BASE_URL", "https://masterqr.pagofacil.com.bo/api/services/v2").trim();
        this.tokenService = props.getProperty("PAGOFACIL_TOKEN_SERVICE", "").trim();
        this.tokenSecret = props.getProperty("PAGOFACIL_TOKEN_SECRET", "").trim();
        this.callbackUrl = props.getProperty("PAGOFACIL_CALLBACK_URL", "").trim();
        this.timeout = Integer.parseInt(props.getProperty("PAGOFACIL_TIMEOUT", "30").trim());
        this.enableLogs = Boolean.parseBoolean(props.getProperty("PAGOFACIL_ENABLE_LOGS", "true").trim());

        if (enableLogs) {
            System.out.println("PagoFacilGateway: Configuración cargada desde: " + origen);
        }
    }

    private String obtenerToken() throws Exception {
        String url = baseUrl + "/login";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("tcTokenService", tokenService)
                .header("tcTokenSecret", tokenSecret)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error al autenticar con PagoFácil. HTTP status: " + response.statusCode());
        }

        String json = response.body();
        String token = extractJsonString(json, "accessToken");
        if (token == null || token.isEmpty()) {
            throw new IOException("Token no encontrado en la respuesta de PagoFácil: " + json);
        }

        return token;
    }

    private int obtenerPaymentMethodId(String accessToken) {
        String url = baseUrl + "/list-enabled-services";
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                String json = response.body();
                String[] blocks = json.split("\\{");
                for (String block : blocks) {
                    if (block.contains("paymentMethodId") && block.contains("paymentMethodName")) {
                        String idStr = extractJsonRawValue("{" + block, "paymentMethodId");
                        String nameStr = extractJsonString("{" + block, "paymentMethodName");
                        if (idStr != null && nameStr != null && nameStr.toUpperCase().contains("QR")) {
                            int id = Integer.parseInt(idStr);
                            if (enableLogs) {
                                System.out.println("PagoFacilGateway: Encontrado método QR habilitado: ID=" + id + ", Nombre=" + nameStr);
                            }
                            return id;
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (enableLogs) {
                System.out.println("PagoFacilGateway: Warning al listar servicios habilitados: " + e.getMessage());
            }
        }
        
        if (enableLogs) {
            System.out.println("PagoFacilGateway: Usando ID de método por defecto (4)");
        }
        return 4; // default
    }

    public QrResult generarQr(String paymentNumber, BigDecimal amount, String concepto, 
                              Long clientId, String clientName, String clientCi, 
                              String clientPhone, String clientEmail) throws Exception {
        String accessToken = obtenerToken();
        int paymentMethodId = obtenerPaymentMethodId(accessToken);
        String url = baseUrl + "/generate-qr";

        // Cuerpo JSON construido a mano para evitar dependencias
        String jsonBody = String.format(
            "{" +
            "\"paymentMethod\": %d," +
            "\"clientName\": \"%s\"," +
            "\"documentType\": 1," +
            "\"documentId\": \"%s\"," +
            "\"phoneNumber\": \"%s\"," +
            "\"email\": \"%s\"," +
            "\"paymentNumber\": \"%s\"," +
            "\"amount\": %s," +
            "\"currency\": 2," +
            "\"clientCode\": \"%d\"," +
            "\"callbackUrl\": \"%s\"," +
            "\"orderDetail\": [{" +
                "\"serial\": 1," +
                "\"product\": \"%s\"," +
                "\"quantity\": 1," +
                "\"price\": %s," +
                "\"discount\": 0," +
                "\"total\": %s" +
            "}]" +
            "}",
            paymentMethodId,
            escapeJson(clientName),
            escapeJson(clientCi != null ? clientCi : "0"),
            escapeJson(clientPhone != null ? clientPhone : "0"),
            escapeJson(clientEmail != null ? clientEmail : "no-email@email.com"),
            escapeJson(paymentNumber),
            amount.toString(),
            clientId != null ? clientId : 0,
            escapeJson(callbackUrl),
            escapeJson(concepto),
            amount.toString(),
            amount.toString()
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error al generar QR. HTTP status: " + response.statusCode() + " | Response: " + response.body());
        }

        String json = response.body();
        
        // El QR puede venir en "qrImage" o "qrBase64"
        String qrBase64 = extractJsonString(json, "qrImage");
        if (qrBase64 == null) {
            qrBase64 = extractJsonString(json, "qrBase64");
        }

        // Transaccion ID puede venir en "transactionId", "idTransaccion", "codigoTransaccion" o "id"
        String transactionId = extractJsonString(json, "transactionId");
        if (transactionId == null) {
            transactionId = extractJsonString(json, "idTransaccion");
        }
        if (transactionId == null) {
            transactionId = extractJsonString(json, "codigoTransaccion");
        }
        if (transactionId == null) {
            // Intentar extraer como entero/raw si no viene entre comillas
            transactionId = extractJsonRawValue(json, "id");
        }

        if (qrBase64 == null || transactionId == null) {
            throw new IOException("No se pudo obtener el QR o ID de transacción. Respuesta de PagoFácil: " + json);
        }

        return new QrResult(qrBase64, transactionId);
    }

    public QueryResult consultarTransaccion(String transactionId) throws Exception {
        String accessToken = obtenerToken();
        String url = baseUrl + "/query-transaction";

        String jsonBody = String.format("{\"pagofacilTransactionId\":\"%s\"}", escapeJson(transactionId));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error al consultar transacción. HTTP status: " + response.statusCode());
        }

        String json = response.body();
        
        String errorVal = extractJsonRawValue(json, "error");
        if (errorVal != null && !errorVal.equals("0")) {
            String message = extractJsonString(json, "message");
            throw new IOException("Error de la API PagoFácil: " + (message != null ? message : json));
        }

        String paymentStatus = extractJsonRawValue(json, "paymentStatus");
        String description = extractJsonString(json, "paymentStatusDescription");
        if (description == null) {
            description = "";
        }

        String statusNormalizado = normalizarEstado(paymentStatus, description);

        return new QueryResult(statusNormalizado, description);
    }

    private String normalizarEstado(String paymentStatus, String description) {
        if (paymentStatus == null) {
            return "PENDIENTE";
        }

        String lowerDesc = description.toLowerCase();
        
        if (paymentStatus.equals("2") || lowerDesc.contains("complet") || lowerDesc.contains("procesado") || lowerDesc.contains("pagado")) {
            return "PAGADO";
        }

        if (paymentStatus.equals("3") || lowerDesc.contains("rechaz") || lowerDesc.contains("cancel") || lowerDesc.contains("anulado")) {
            return "CANCELADO";
        }

        return "PENDIENTE";
    }

    private String extractJsonString(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"[\\s:]+\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return extractJsonRawValue(json, key);
    }

    private String extractJsonRawValue(String json, String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\"[\\s:]+([^,}\\s]+)");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String val = matcher.group(1).trim();
            if (val.startsWith("\"") && val.endsWith("\"")) {
                val = val.substring(1, val.length() - 1);
            }
            return val;
        }
        return null;
    }

    private String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static class QrResult {
        public final String qrImage;
        public final String transactionId;

        public QrResult(String qrImage, String transactionId) {
            this.qrImage = qrImage;
            this.transactionId = transactionId;
        }
    }

    public static class QueryResult {
        public final String status;
        public final String description;

        public QueryResult(String status, String description) {
            this.status = status;
            this.description = description;
        }
    }
}
