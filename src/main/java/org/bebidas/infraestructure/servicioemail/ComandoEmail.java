package org.bebidas.infraestructure.servicioemail;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ComandoEmail {

    private CategoriaRepository categoriaRepository = new JdbcCategoriaRepository();
    private CategoriaValidator categoriaValidator = new CategoriaValidator();
    private CategoriaService categoriaService = new CategoriaService(categoriaRepository, categoriaValidator);

    private ProductoRepository productoRepository = new JdbcProductoRepository();

    private ProductoValidator productoValidator = new ProductoValidator();
    private ProductoService productoService = new ProductoService(productoRepository, categoriaRepository,
            productoValidator);

    private UsuarioRepository usuarioRepository = new JdbcUsuarioRepository();
    private UsuarioValidator usuarioValidator = new UsuarioValidator();
    private UsuarioService usuarioService = new UsuarioService(usuarioRepository, usuarioValidator);

    private BarberoRepository barberoRepository = new JdbcBarberoRepository();
    private BarberoValidator barberoValidator = new BarberoValidator();
    private BarberoService barberoService = new BarberoService(barberoRepository, usuarioRepository, barberoValidator);

    private ClienteRepository clienteRepository = new JdbcClienteRepository();
    private ClienteValidator clienteValidator = new ClienteValidator();
    private ClienteService clienteService = new ClienteService(clienteRepository, usuarioRepository, clienteValidator);

    private HorarioRepository horarioRepository = new JdbcHorarioRepository();
    private HorarioValidator horarioValidator = new HorarioValidator();
    private HorarioService horarioService = new HorarioService(horarioRepository, horarioValidator);

    private ServicioRepository servicioRepository = new JdbcServicioRepository();
    private ServicioValidator servicioValidator = new ServicioValidator();
    private ServicioService servicioService = new ServicioService(servicioRepository, servicioValidator);

    private ServicioProductoRepository servicioProductoRepository = new JdbServicioProductoRepository();
    private ServicioProductoValidator servicioProductoValidator = new ServicioProductoValidator();
    private ServicioProductoService servicioProductoService = new ServicioProductoService(servicioProductoRepository,
            servicioProductoValidator);

    private ReservaRepository reservaRepository = new JdbcReservaRepository();
    private PagoRepository pagoRepository = new JdbcPagoRepository();
    private PagoValidator pagoValidator = new PagoValidator();
    private PagoService pagoService = new PagoService(pagoRepository, pagoValidator, reservaRepository);
    private ServicioProductoRepository reservaProductoRepository = new JdbServicioProductoRepository();
    private ReservaValidator reservaValidator = new ReservaValidator();
    private ReservaService reservaService = new ReservaService(reservaRepository, reservaValidator, pagoRepository,
            servicioRepository, barberoRepository, usuarioRepository, horarioRepository, servicioProductoRepository,
            productoRepository);

    private ReporteService reporteService = new ReporteService();

    // private CommandHelp commandHelp = new CommandHelp();
    public String evaluarYEjecutar(String subject) throws SQLException {

        // Decodificar el subject si viene en formato MIME encoded-word
        // (=?UTF-8?Q?...?=)
        subject = decodeMimeSubject(subject);

        System.out.println("Subject decodificado: " + subject);

        if (Objects.equals(subject, "HELP")) {
            // return CommandHelp.obtenerComandosDisponibles();
            return CommandHelpHTML.obtenerComandosDisponibles();
        }

        // Verificar si es un comando de reporte DASHBOARD
        if (subject.equals("REPORTEDASHBOARD")) {
            return wrapInHTML(ejecutarReporteDashboard());
        }

        String respuestaConsulta;

        // Definir patrones para cada operación CRUD (usando [^\\[\\]] para aceptar
        // cualquier caracter excepto corchetes)
        Pattern listarPatron = Pattern.compile("^LISTAR([A-Z]+)\\[\\*\\]$"); // Ej: LISTARCLIENTES[*]
        Pattern crearPatron = Pattern.compile("^CREATE([A-Z]+)\\[(.+)\\]$"); // Ej: CREATECLIENTES[nombre, apellido,
                                                                             // otros]
        Pattern actualizarPatron = Pattern.compile("^UPDATE([A-Z]+)\\[(.+)\\]$"); // Ej: UPDATECLIENTES[param1, param2]
        Pattern eliminarPatron = Pattern.compile("^DELETE([A-Z]+)\\[(.+)\\]$"); // Ej: DELETECLIENTES[id]
        Pattern getPatron = Pattern.compile("^GET([A-Z]+)\\[(\\d+)\\]$"); // Ej: GETMEDICAMENTOS[2]
        Pattern reportePatron = Pattern.compile("^REPORTE([A-Z]+)\\[(.+)\\]$"); // Ej: REPORTEINGRESOS[2025, 10]

        Matcher matcher;
        System.out.println("Evaluando subject: " + subject);
        // Evaluar cada patrón
        if ((matcher = listarPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1);
            respuestaConsulta = ejecutarConsultaListar(entidad);
        } else if ((matcher = crearPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1);
            String parametros = matcher.group(2);
            respuestaConsulta = ejecutarConsultaCrear(entidad, parametros);
        } else if ((matcher = actualizarPatron.matcher(subject)).matches()) {
            System.out.println("Patron de actualizacion coincide");
            String entidad = matcher.group(1);
            String parametros = matcher.group(2);
            respuestaConsulta = ejecutarConsultaActualizar(entidad, parametros);
        } else if ((matcher = eliminarPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1);
            String id = matcher.group(2);
            respuestaConsulta = ejecutarConsultaEliminar(entidad, id);
        } else if ((matcher = getPatron.matcher(subject)).matches()) {
            String entidad = matcher.group(1);
            String id = matcher.group(2);
            respuestaConsulta = ejecutarConsultaGet(entidad, id);
        } else if ((matcher = reportePatron.matcher(subject)).matches()) {
            String tipoReporte = matcher.group(1);
            String parametros = matcher.group(2);
            respuestaConsulta = ejecutarReporte(tipoReporte, parametros);
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
        String respuesta = "";
        try {
            switch (entidad) {
                case "PRODUCTOS" -> {
                    ProductoService productoService = new ProductoService(productoRepository, categoriaRepository,
                            productoValidator);
                    respuesta = org.barberia.usuarios.mapper.ProductoMapper
                            .obtenerTodosTable(productoService.getAllAsTable());
                }
                case "CATEGORIAS" -> {
                    respuesta = categoriaService.getAllAsTable();
                }
                case "USUARIOS" -> {
                    respuesta = usuarioService.getAllAsTable();
                }
                case "BARBEROS" -> {
                    respuesta = barberoService.getAllAsTable();
                }
                case "CLIENTES" -> {
                    respuesta = clienteService.getAllAsTable();
                }
                case "HORARIOS" -> {
                    respuesta = horarioService.getAllAsTable();
                }
                case "SERVICIOS" -> {
                    respuesta = servicioService.getAllAsTable();
                }
                case "RESERVAS" -> {
                    respuesta = reservaService.getAllAsTable();
                }
                case "PAGOS" -> {
                    respuesta = pagoService.getAllAsTable();
                }

                default -> respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al obtener listado de " + entidad + ": " + e.getMessage();
        }
    }

    private String ejecutarConsultaCrear(String entidad, String parametros) throws SQLException {
        String respuesta = "";
        try {
            String[] params = parametros.split(",");
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].trim();
            }

            switch (entidad) {
                case "PRODUCTOS" -> {
                    if (params.length != 10) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    Producto producto = new Producto();
                    producto.id_categoria = Integer.parseInt(params[0]);
                    producto.codigo = params[1];
                    producto.nombre = params[2];
                    producto.descripcion = params[3];
                    producto.precio_compra = new BigDecimal(params[4]);
                    producto.precio_venta = new BigDecimal(params[5]);
                    producto.stock_actual = Integer.parseInt(params[6]);
                    producto.stock_minimo = Integer.parseInt(params[7]);
                    producto.imagenurl = params[8];
                    producto.unidad_medida = params[9];

                    respuesta = productoService.create(producto.id_categoria, producto.codigo, producto.nombre,
                            producto.descripcion, producto.precio_compra, producto.precio_venta,
                            producto.stock_actual, producto.stock_minimo, producto.unidad_medida,
                            producto.imagenurl);

                }

                case "CATEGORIAS" -> {
                    if (params.length != 2) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    Categoria categoria = new Categoria();
                    categoria.nombre = params[0];
                    categoria.descripcion = params[1];
                    respuesta = categoriaService.create(categoria.nombre, categoria.descripcion).toString();
                }

                case "USUARIOS" -> {
                    if (params.length != 7) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    Usuario usuario = new Usuario();
                    usuario.nombre = params[0];
                    usuario.apellido = params[1];
                    usuario.email = params[2];
                    usuario.username = params[3];
                    usuario.telefono = params[4];
                    usuario.direccion = params[5];
                    usuario.password = params[6];

                    respuesta = usuarioService.create(
                            usuario.nombre, usuario.apellido, usuario.email, usuario.username, usuario.telefono,
                            usuario.direccion, usuario.password).toString();
                }
                case "BARBEROS" -> {
                    if (params.length != 3) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    respuesta = barberoService.create(
                            Integer.parseInt(params[0]),
                            params[1],
                            params[2]).toString();
                }
                case "CLIENTES" -> {
                    if (params.length != 3) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    respuesta = clienteService.create(
                            Integer.parseInt(params[0]),
                            params[1],
                            params[2]).toString();
                }
                case "HORARIOS" -> {
                    if (params.length != 4) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    respuesta = horarioService.create(
                            Integer.parseInt(params[0]), // id_barbero
                            params[1], // dia_semana
                            params[2], // hora_inicio
                            params[3]).toString(); // hora_fin
                }
                case "SERVICIOS" -> {
                    if (params.length != 5) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    respuesta = servicioService.create(
                            params[0],
                            params[1],
                            Integer.parseInt(params[2]),
                            new BigDecimal(params[3]),
                            params[4]).toString();
                }

                case "SERVICIOPRODUCTOS" -> {
                    if (params.length != 3) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    ServicioProducto sp = new ServicioProducto();
                    sp.id_servicio = Integer.parseInt(params[0]);
                    sp.id_producto = Integer.parseInt(params[1]);
                    sp.cantidad = Integer.parseInt(params[2]);

                    respuesta = servicioProductoService.create(sp).toString();
                }

                case "RESERVAS" -> {
                    if (params.length != 6) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    Reserva reserva = new Reserva();
                    reserva.id_cliente = Integer.parseInt(params[0]);
                    reserva.id_barbero = Integer.parseInt(params[1]);
                    reserva.id_servicio = Integer.parseInt(params[2]);
                    reserva.fecha_reserva = LocalDate.parse(params[3]);
                    reserva.hora_inicio = LocalTime.parse(params[4]);
                    reserva.notas = params[5];
                    respuesta = reservaService.create(
                            reserva.id_cliente,
                            reserva.id_barbero,
                            reserva.id_servicio,
                            reserva.fecha_reserva,
                            reserva.hora_inicio,
                            reserva.notas).toString();
                }
                case "PAGOS" -> {
                    if (params.length != 5) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    Pago pago = new Pago();
                    pago.id_reserva = Integer.parseInt(params[0]);
                    pago.monto_total = new BigDecimal(params[1]);
                    pago.metodo_pago = MetodoPago.parse(params[2]);
                    pago.tipo_pago = TipoPago.parse(params[3]);
                    pago.notas = params[4];

                    respuesta = pagoService.create(
                            pago,
                            pago.id_reserva).toString();
                }

                default -> respuesta = "Entidad no encontrada";
            }

            return respuesta;
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
            switch (entidad) {
                case "PRODUCTOS" -> {
                    if (params.length != 11) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    respuesta = this.productoService.update(
                            Integer.parseInt(params[0]),
                            Integer.parseInt(params[1]),
                            params[2],
                            params[3],
                            params[4],
                            new BigDecimal(params[5]),
                            new BigDecimal(params[6]),
                            Integer.parseInt(params[7]),
                            Integer.parseInt(params[8]),
                            params[9],
                            params[10]).toString();
                }
                case "CATEGORIAS" -> {
                    if (params.length != 3) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    respuesta = categoriaService.update(
                            Integer.parseInt(params[0]),
                            params[1],
                            params[2]);

                }
                case "USUARIOS" -> {

                    if (params.length != 8) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    respuesta = usuarioService.update(
                            Integer.parseInt(params[0]),
                            params[1],
                            params[2],
                            params[3],
                            params[4],
                            params[5],
                            params[6],
                            params[7]);

                }
                case "CLIENTES" -> {
                    if (params.length != 4) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    respuesta = clienteService.update(
                            Integer.parseInt(params[0]),
                            Integer.parseInt(params[1]),
                            params[2],
                            params[3]

                    );

                }
                case "BARBEROS" -> {
                    if (params.length != 5) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    Barbero barbero = new Barbero();
                    barbero.id_barbero = Integer.parseInt(params[0]);
                    barbero.id_usuario = Integer.parseInt(params[1]);
                    barbero.especialidad = params[2];
                    barbero.foto_perfil = params[3];
                    barbero.estado = EstadoBarbero.parse(params[4]);

                    respuesta = barberoService.update(
                            Integer.parseInt(params[0]),
                            barbero

                    );
                }

                case "HORARIOS" -> {
                    if (params.length != 5) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    respuesta = horarioService.update(
                            Integer.parseInt(params[0]),
                            Integer.parseInt(params[1]),
                            params[2],
                            LocalTime.parse(params[3]),
                            LocalTime.parse(params[4]));

                }
                case "SERVICIOS" -> {
                    if (params.length != 6) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }
                    respuesta = servicioService.update(
                            Integer.parseInt(params[0]),
                            params[1],
                            params[2],
                            Integer.parseInt(params[3]),
                            new BigDecimal(params[4]),
                            params[5]);
                    System.out.println(respuesta + "llega del update");

                }

                case "SERVICIOPRODUCTOS" -> {

                    // "id_servicio, id_producto, cantidad"
                    // 1, 2, 3

                    respuesta = servicioProductoService.update(
                            Integer.parseInt(params[0]),
                            Integer.parseInt(params[1]),
                            Integer.parseInt(params[2])).toString();

                }
                
                  case "RESERVAS" -> {
                  
                  //"id_cliente ,id_barbero ,id_servicio ,fecha_reserva ,hora_inicio, hora_fin,notas,opcional estado(confirmada, cancelada,completada,no_asistio) "
                  
                  
                    if (params.length != 8) {
                            throw new IllegalArgumentException("Número de parámetros incorrecto");
                        }
                        
                        Reserva r = new Reserva();
                        r.id_reserva = Integer.parseInt(params[0]);
                        r.id_cliente = Integer.parseInt(params[1]);
                        r.id_barbero = Integer.parseInt(params[2]);
                        r.id_servicio = Integer.parseInt(params[3]);
                        r.fecha_reserva = LocalDate.parse(params[4]);
                        r.hora_inicio = LocalTime.parse(params[5]);
                        r.notas = params[6];
                        r.estado = EstadoReserva.parse(params[7]);
    
                        respuesta = reservaService.update(
                             Integer.parseInt(params[0]),
                                r
                               ).toString();
                  
                  }
                 

                case "PAGOS" -> {
                    if (params.length != 7) {
                        throw new IllegalArgumentException("Número de parámetros incorrecto");
                    }

                    Pago p = new Pago();
                    p.id_pago = Integer.parseInt(params[0]);
                    p.id_reserva = Integer.parseInt(params[1]);
                    p.monto_total = new BigDecimal(params[2]);
                    p.metodo_pago = MetodoPago.parse(params[3]);
                    p.tipo_pago = TipoPago.parse(params[4]);
                    p.notas = params[5];
                    p.estado = EstadoPago.parse(params[6]);

                    respuesta = pagoService.update(
                            p,
                            Integer.parseInt(params[0]));

                }

                default -> respuesta = "Entidad no encontrada";
            }
            System.out.println("sale de la funcion : " + respuesta);
            return respuesta;
        } catch (Exception e) {
            return "Error al actualizar " + entidad + ": " + e.getMessage();
        }
    }

    private String ejecutarConsultaEliminar(String entidad, String id) {
        int entityId = Integer.parseInt(id);
        if (entityId <= 0) {
            return "ID inválido";
        }
        try {
            String respuesta = "";
            switch (entidad) {
                case "USUARIOS" -> {

                    respuesta = this.usuarioService.toggleActive(entityId);
                }
                case "CLIENTES" -> {
                    respuesta = this.clienteService.delete(entityId);
                    ;
                }
                case "BARBEROS" -> {
                    respuesta = this.barberoService.delete(entityId);
                }

                case "HORARIOS" -> {
                    respuesta = this.horarioService.delete(entityId);
                }
                case "CATEGORIAS" -> {
                    respuesta = this.categoriaService.toggleActive(entityId);
                }
                case "PRODUCTOS" -> {
                    respuesta = this.productoService.delete(entityId);
                }
                case "SERVICIOS" -> {
                    respuesta = this.servicioService.toggleActive(entityId);
                }

                case "RESERVAS" -> {
                    respuesta = this.reservaService.delete(entityId);
                }
                case "PAGOS" -> {
                    respuesta = pagoService.delete(entityId);
                }

                default -> respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al eliminar " + entidad + ": " + e.getMessage();
        }
    }

    private String ejecutarConsultaGet(String entidad, String stringId) {
        int id = Integer.parseInt(stringId);
        if (id <= 0) {
            return "ID inválido";
        }
        try {
            String respuesta = "";
            switch (entidad) {
                case "USUARIOS" -> {
                    respuesta = this.usuarioService.getByIdAsTable(id);
                }
                case "ClIENTES" -> {
                    respuesta = this.clienteService.getByIdAsTable(id);
                }
                case "BARBEROS" -> {
                    respuesta = this.barberoService.getByIdAsTable(id);
                }
                case "HORARIOS" -> {
                    respuesta = this.horarioService.getByIdAsTable(id);
                }
                case "CATEGORIA" -> {
                    respuesta = this.categoriaService.getByIdAsTable(id);
                }
                case "PRODUCTOS" -> {
                    respuesta = this.productoService.getByIdAsTable(id);
                }
                case "SERVICIOS" -> {
                    respuesta = this.servicioService.getByIdAsTable(id);
                }
                case "RESERVAS" -> {
                    respuesta = this.reservaService.getByIdAsTable(id);
                }
                case "PAGOS" -> {
                    respuesta = this.pagoService.getByIdAsTable(id);
                }

                default -> respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al obtener " + entidad + ": " + e.getMessage();
        }
    }


    // ==================== MÉTODOS PARA REPORTES ====================

    /**
     * Ejecuta el reporte de dashboard general (mes actual)
     */
    private String ejecutarReporteDashboard() {
        try {
            YearMonth mesActual = YearMonth.now();
            Map<String, Object> dashboard = reporteService.getDashboardGeneral(mesActual);

            StringBuilder sb = new StringBuilder();
            sb.append("\n╔═══════════════════════════════════════════════════════════════════╗\n");
            sb.append("║              DASHBOARD GENERAL - ").append(dashboard.get("periodo"))
                    .append("                          ║\n");
            sb.append("╚═══════════════════════════════════════════════════════════════════╝\n\n");

            // Ingresos
            @SuppressWarnings("unchecked")
            Map<String, Object> ingresos = (Map<String, Object>) dashboard.get("ingresos");
            sb.append(ReporteMapper.formatIngresosMensuales(ingresos)).append("\n\n");

            // Top barberos
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> barberos = (List<Map<String, Object>>) dashboard.get("top_barberos");
            sb.append(ReporteMapper.formatRankingBarberos(barberos)).append("\n\n");

            // Servicios populares
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> servicios = (List<Map<String, Object>>) dashboard.get("servicios_populares");
            sb.append(ReporteMapper.formatServiciosPopulares(servicios)).append("\n\n");

            // Clientes frecuentes
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> clientes = (List<Map<String, Object>>) dashboard.get("clientes_frecuentes");
            sb.append(ReporteMapper.formatClientesFrecuentes(clientes)).append("\n\n");

            // Distribución de estados
            @SuppressWarnings("unchecked")
            Map<String, Object> estados = (Map<String, Object>) dashboard.get("distribucion_estados");
            sb.append(ReporteMapper.formatDistribucionEstados(estados)).append("\n\n");

            // Métodos de pago
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> metodos = (List<Map<String, Object>>) dashboard.get("metodos_pago");
            sb.append(ReporteMapper.formatMetodosPago(metodos)).append("\n\n");

            // Horas pico
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> horas = (List<Map<String, Object>>) dashboard.get("horas_pico");
            sb.append(ReporteMapper.formatHorasPico(horas)).append("\n\n");

            // Días más ocupados
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> dias = (List<Map<String, Object>>) dashboard.get("dias_ocupados");
            sb.append(ReporteMapper.formatDiasMasOcupados(dias)).append("\n");

            return sb.toString();
        } catch (Exception e) {
            return "Error al generar dashboard: " + e.getMessage();
        }
    }

    /**
     * Ejecuta reportes individuales con parámetros personalizados
     */
    private String ejecutarReporte(String tipoReporte, String parametros) {
        try {
            String[] params = parametros.split(",");
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].trim();
            }

            return switch (tipoReporte) {
                case "INGRESOS" -> {
                    // REPORTEINGRESOS[2025, 10]
                    if (params.length < 2) {
                        yield "Error: Se requieren año y mes. Ejemplo: REPORTEINGRESOS[2025, 10]";
                    }
                    int año = Integer.parseInt(params[0]);
                    int mes = Integer.parseInt(params[1]);
                    Map<String, Object> ingresos = reporteService.getIngresosMensuales(año, mes);
                    yield ReporteMapper.formatIngresosMensuales(ingresos);
                }

                case "RANKINGBARBEROS" -> {
                    // REPORTERANKINGBARBEROS[2025-10-01, 2025-10-31]
                    if (params.length < 2) {
                        yield "Error: Se requieren fecha inicio y fecha fin. Ejemplo: REPORTERANKINGBARBEROS[2025-10-01, 2025-10-31]";
                    }
                    LocalDate inicio = LocalDate.parse(params[0]);
                    LocalDate fin = LocalDate.parse(params[1]);
                    List<Map<String, Object>> ranking = reporteService.getRankingBarberos(inicio, fin);
                    yield ReporteMapper.formatRankingBarberos(ranking);
                }

                case "SERVICIOSPOPULARES" -> {
                    // REPORTESERVICIOSPOPULARES[2025-01-01, 2025-12-31, 5]
                    if (params.length < 3) {
                        yield "Error: Se requieren fecha inicio, fecha fin y límite. Ejemplo: REPORTESERVICIOSPOPULARES[2025-01-01, 2025-12-31, 5]";
                    }
                    LocalDate inicio = LocalDate.parse(params[0]);
                    LocalDate fin = LocalDate.parse(params[1]);
                    int limite = Integer.parseInt(params[2]);
                    List<Map<String, Object>> servicios = reporteService.getServiciosMasPopulares(inicio, fin, limite);
                    yield ReporteMapper.formatServiciosPopulares(servicios);
                }

                case "CLIENTESFRECUENTES" -> {
                    // REPORTECLIENTESFRECUENTES[2025-01-01, 2025-12-31, 10]
                    if (params.length < 3) {
                        yield "Error: Se requieren fecha inicio, fecha fin y límite. Ejemplo: REPORTECLIENTESFRECUENTES[2025-01-01, 2025-12-31, 10]";
                    }
                    LocalDate inicio = LocalDate.parse(params[0]);
                    LocalDate fin = LocalDate.parse(params[1]);
                    int limite = Integer.parseInt(params[2]);
                    List<Map<String, Object>> clientes = reporteService.getClientesFrecuentes(inicio, fin, limite);
                    yield ReporteMapper.formatClientesFrecuentes(clientes);
                }

                case "DISTRIBUCIONESTADOS" -> {
                    // REPORTEDISTRIBUCIONESTADOS[2025-07-01, 2025-09-30]
                    if (params.length < 2) {
                        yield "Error: Se requieren fecha inicio y fecha fin. Ejemplo: REPORTEDISTRIBUCIONESTADOS[2025-07-01, 2025-09-30]";
                    }
                    LocalDate inicio = LocalDate.parse(params[0]);
                    LocalDate fin = LocalDate.parse(params[1]);
                    Map<String, Object> distribucion = reporteService.getDistribucionEstados(inicio, fin);
                    yield ReporteMapper.formatDistribucionEstados(distribucion);
                }

                case "HORASPICO" -> {
                    // REPORTEHORASPICO[2025-10-01, 2025-10-31]
                    if (params.length < 2) {
                        yield "Error: Se requieren fecha inicio y fecha fin. Ejemplo: REPORTEHORASPICO[2025-10-01, 2025-10-31]";
                    }
                    LocalDate inicio = LocalDate.parse(params[0]);
                    LocalDate fin = LocalDate.parse(params[1]);
                    List<Map<String, Object>> horas = reporteService.getHorasPico(inicio, fin);
                    yield ReporteMapper.formatHorasPico(horas);
                }

                case "DIASOCUPADOS" -> {
                    // REPORTEDIASMOCUPADOS[2025-10-01, 2025-10-31]
                    if (params.length < 2) {
                        yield "Error: Se requieren fecha inicio y fecha fin. Ejemplo: REPORTEDIASOCUPADOS[2025-10-01, 2025-10-31]";
                    }
                    LocalDate inicio = LocalDate.parse(params[0]);
                    LocalDate fin = LocalDate.parse(params[1]);
                    List<Map<String, Object>> dias = reporteService.getDiasMasOcupados(inicio, fin);
                    yield ReporteMapper.formatDiasMasOcupados(dias);
                }

                case "METODOSPAGO" -> {
                    // REPORTEMETODOSPAGO[2025-01-01, 2025-12-31]
                    if (params.length < 2) {
                        yield "Error: Se requieren fecha inicio y fecha fin. Ejemplo: REPORTEMETODOSPAGO[2025-01-01, 2025-12-31]";
                    }
                    LocalDate inicio = LocalDate.parse(params[0]);
                    LocalDate fin = LocalDate.parse(params[1]);
                    List<Map<String, Object>> metodos = reporteService.getDistribucionMetodosPago(inicio, fin);
                    yield ReporteMapper.formatMetodosPago(metodos);
                }

                case "CONSUMOPRODUCTOS" -> {
                    // REPORTECONSUMOPRODUCTOS[2025-10-01, 2025-10-31]
                    if (params.length < 2) {
                        yield "Error: Se requieren fecha inicio y fecha fin. Ejemplo: REPORTECONSUMOPRODUCTOS[2025-10-01, 2025-10-31]";
                    }
                    LocalDate inicio = LocalDate.parse(params[0]);
                    LocalDate fin = LocalDate.parse(params[1]);
                    List<Map<String, Object>> productos = reporteService.getConsumoProductos(inicio, fin);
                    yield ReporteMapper.formatConsumoProductos(productos);
                }

                case "ESTADISTICASBARBERO" -> {
                    // REPORTEESTADISTICASBARBERO[1, 2025-10-01, 2025-10-31]
                    if (params.length < 3) {
                        yield "Error: Se requieren id_barbero, fecha inicio y fecha fin. Ejemplo: REPORTEESTADISTICASBARBERO[1, 2025-10-01, 2025-10-31]";
                    }
                    int idBarbero = Integer.parseInt(params[0]);
                    LocalDate inicio = LocalDate.parse(params[1]);
                    LocalDate fin = LocalDate.parse(params[2]);
                    Map<String, Object> stats = reporteService.getEstadisticasBarbero(idBarbero, inicio, fin);
                    yield ReporteMapper.formatEstadisticasBarbero(stats);
                }

                default -> "Tipo de reporte no reconocido: " + tipoReporte;
            };

        } catch (NumberFormatException e) {
            return "Error: Formato numérico inválido en los parámetros.";
        } catch (Exception e) {
            return "Error al generar reporte: " + e.getMessage();
        }
    }

}
