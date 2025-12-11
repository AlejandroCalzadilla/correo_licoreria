package org.bebidas.infraestructure.servicioemail;

import java.util.ArrayList;
import java.util.List;

public class CommandHelp {

        private static class CommandExample {
                String operation;
                String entity;
                String description;
                String parameters;
                String example;

                public CommandExample(String operation, String entity, String description, String parameters,
                                String example) {
                        this.operation = operation;
                        this.entity = entity;
                        this.description = description;
                        this.parameters = parameters;
                        this.example = example;
                }
        }

        private static final List<CommandExample> COMMANDS = new ArrayList<>();

        static {
                // CREATE commands
                COMMANDS.add(new CommandExample(
                                "CREATE", "CATEGORIAS",
                                "Crear una nueva categoría",
                                "nombre, descripcion",
                                "CREATECATEGORIAS[Shampoo, Productos para el cuidado del cabello]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "CLIENTES",
                                "Crear un nuevo cliente",
                                "id_usuario, fecha_nacimiento, ci",
                                "CREATECLIENTES[1, 1990-01-01, 12345678]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "PRODUCTOS",
                                "Crear un nuevo producto",
                                "id_categoria,codigo,nombre, descripcion, precio_compra,precio_venta, stock_actual, stock_minimo, imagenurl, unidad_medida",
                                "CREATEPRODUCTOS[1, SHP001, Shampoo, Shampoo para perros, 15.99, 25.99, 100, 20, https://example.com/shampoo.jpg, Litros]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "BARBEROS",
                                "Crear un nuevo barbero",
                                "id_usuario,especialidad,foto_perfil,",
                                "CREATEBARBEROS[1, Corte de cabello, Juan, Perez, 555-1234, juan.perez@example.com, Calle Falsa 123]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "SERVICIOS",
                                "Crear un nuevo servicio",
                                "id_usuario,especialidad,foto_perfil,",
                                "CREATESERVICIOS[1, Corte de cabello, Juan, Perez, 555-1234, juan.perez@example.com, Calle Falsa 123]"));

                COMMANDS.add(new CommandExample(
                                "CREATE",
                                "USUARIOS",
                                "Crear un nuevo Usuario",
                                "nombre, apellido, email , telefono , direccion , username, password",
                                "CREATEUSUARIOS[juan , perez, juanperez@gmail.com , 79845888, calle falsa 123, juanp, passowrd  ]"));

                COMMANDS.add(new CommandExample(
                                "CREATE",
                                "HORARIOS",
                                "Crear un nuevo hoario para un barbero",
                                "id_barbero, dia_semana, hora_inicio, hora_fin",
                                "CREATEHORARIOS[1, lunes , 10:30 , 11::30]"));

                COMMANDS.add(new CommandExample("CREATE",
                                "SERVICIOSPRODUCTOS",
                                "crear un nuevo servicio producto ",
                                "id_servicio ,id_producto ,cantidad ",
                                "CREATESERVICIOPRODUCTOS [1, 1, 2 ]"));

                COMMANDS.add(new CommandExample("CREATE",
                                "RESERVAS",
                                "Crear una nueva reserva (valida barbero activo y horario)",
                                "id_cliente, id_barbero, id_servicio, fecha_reserva(YYYY-MM-DD), hora_inicio(HH:MM), notas",
                                "CREATERESERVAS[1, 1, 1, 2024-12-01, 10:00, reserva para corte de cabello]"));

                COMMANDS.add(new CommandExample(
                                "CREATE",
                                "crear un nuevo pago ",
                                "crea un nuevo pago para una reserva ",
                                "id_reserva,monto_total, metodo_pago(efectivo,transferencia,otro), tipo_pago(anticipo, pago_final, pago-completo) ,notas",
                                "CREATEPAGOS[1, 100.00, efectivo, anticipo, pago inicial]"));
                // COMMANDS.add(null)

                // Agregar más UPDATE commands siguiendo el mismo patrón...
                COMMANDS.add(new CommandExample(
                                "UPDATE", "CATEGORIAS",
                                "Actualiza una  categoría",
                                "id_categoria , nombre, descripcion",
                                "UPDATETEGORIAS[id_categoria, Shampoo, Productos para el cuidado del cabello]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "CLIENTES",
                                "Actualizar un cliente",
                                "id_cliente, id_usuario, fecha_nacimiento, ci",
                                "UPDATECLIENTES[ 1 ,1, 1990-01-01, 12345678]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "PRODUCTOS",
                                "Actualizar un producto",
                                "id_producto, id_categoria,codigo,nombre, descripcion, precio_compra,precio_venta, stock_actual, stock_minimo, imagenurl, unidad_medida",
                                "UPDATEPRODUCTOS[1, 1, SHP001, Shampoo, Shampoo para perros, 15.99, 25.99, 100, 20, https://example.com/shampoo.jpg, Litros]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "BARBEROS",
                                "Actualizar un barbero",
                                "id_barbero, id_usuario, especialidad, foto_perfil",
                                "UPDATEBARBEROS[1, 1, Corte de cabello, juan.perez@example.com, Calle Falsa 123]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "SERVICIOS",
                                "Actualizar un servicio",
                                "id_servicio, nombre, descripcion, precio",
                                "UPDATESERVICIOS[1, Corte de cabello, Servicio de corte de cabello, 20.00]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE",
                                "USUARIOS",
                                "Actualizar un Usuario",
                                "id_usuario ,nombre, apellido, email , telefono , direccion , username, password",
                                "UPDATEUSUARIOS[1, juan , perez, juanperez@gmail.com , 79845888, calle falsa 123, juanp, passowrd  ]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE",
                                "HORARIOS",
                                "Actualizar un horario para un barbero",
                                "id_horario, id_barbero, dia_semana, hora_inicio, hora_fin",
                                "UPDATEHORARIOS [1 ,1, lunes , 10:30 , 11::30] "));
                COMMANDS.add(new CommandExample(
                                "UPDATE",
                                "PAGOS",
                                "Actualizar un pago",
                                "id_reserva ,metodo_pago(efectivo,transferencia,otro), tipo_pago() ,notas ",
                                "UPDATEPAGOS[1, tarjeta, servicio, pago por corte de cabello]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE",
                                "SERVICIOSPRODUCTOS",
                                "Actualizar un servicio producto ",
                                "id_servicio ,id_producto ,cantidad ",
                                "1, 1, 2"));
                COMMANDS.add(new CommandExample(
                                "UPDATE",
                                "RESERVAS",
                                "Actualizar una reserva (valida barbero activo y horario si cambian)",
                                "id_reserva, id_cliente, id_barbero, id_servicio, fecha_reserva(YYYY-MM-DD), hora_inicio(HH:MM), hora_fin(HH:MM), notas, estado(confirmada|cancelada|completada|no_asistio|en_proceso)",
                                "UPDATERESERVAS[1, 1, 1, 1, 2024-12-01, 10:00, 11:00, reserva para corte de cabello, completada]"));
                COMMANDS.add(new CommandExample("UPDATE",
                                "PAGOS",
                                "actualizar un  pago ",
                                "id_pago,id_reserva ,metodo_pago(efectivo,transferencia,otro), tipo_pago(anticipo , pago_final , completo ) ,notas, estado(pendiente,pagado , cancelado, reembolsado) ",
                                "CREATEPAGOS[1,1, tarjeta, servicio, pago por corte de cabello]"));

        }

        public static String obtenerComandosDisponibles() {
                StringBuilder sb = new StringBuilder();

                // Encabezado
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n");
                sb.append("                    COMANDOS DISPONIBLES DEL SISTEMA                           \n");
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n\n");

                // Comandos generales
                sb.append("COMANDOS GENERALES:\n");
                sb.append("  • LISTAR<entidad>[*]              - Listar todos los registros\n");
                sb.append("  • CREATE<entidad>[params...]      - Crear un nuevo registro\n");
                sb.append("  • UPDATE<entidad>[id, params...]  - Actualizar un registro\n");
                sb.append("  • DELETE<entidad>[id]             - Eliminar un registro\n");
                sb.append("  • GET<entidad>[id]                - Obtener un registro por ID\n");
                sb.append("  • HELP                            - Mostrar esta ayuda\n\n");

                // Entidades disponibles
                sb.append("ENTIDADES DISPONIBLES:\n");
                sb.append("  USUARIOS, CLIENTES, BARBEROS,CATEGORIAS, PRODUCTOS, SERVICIOS,\n");
                sb.append("  HORARIOS, SERVICIOPRODUCTOS, RESERVAS, PAGOS\n\n");

                // Comandos de reportes
                sb.append("COMANDOS DE REPORTES:\n");
                sb.append("  • REPORTEDASHBOARD                      - Dashboard general del mes actual\n");
                sb.append("  • REPORTEINGRESOS[año, mes]             - Ingresos mensuales\n");
                sb.append("  • REPORTERANKINGBARBEROS[inicio, fin]   - Ranking de barberos por período\n");
                sb.append("  • REPORTESERVICIOSPOPULARES[inicio, fin, limite] - Servicios más populares\n");
                sb.append("  • REPORTECLIENTESFRECUENTES[inicio, fin, limite] - Clientes más frecuentes\n");
                sb.append("  • REPORTEDISTRIBUCIONESTADOS[inicio, fin]        - Distribución de estados\n");
                sb.append("  • REPORTEHORASPICO[inicio, fin]                  - Horas con más actividad\n");
                sb.append("  • REPORTEDIASOCUPADOS[inicio, fin]               - Días más ocupados\n");
                sb.append("  • REPORTEMETODOSPAGO[inicio, fin]                - Distribución de métodos de pago\n");
                sb.append("  • REPORTECONSUMOPRODUCTOS[inicio, fin]           - Consumo de productos\n");
                sb.append("  • REPORTEESTADISTICASBARBERO[id, inicio, fin]    - Estadísticas de un barbero\n\n");
                sb.append("  Formato de fechas: YYYY-MM-DD (Ejemplo: 2025-10-01)\n");

                // Agrupar comandos por operación
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n");
                sb.append("                           COMANDOS CREATE                                      \n");
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n\n");

                for (CommandExample cmd : COMMANDS) {
                        if (cmd.operation.equals("CREATE")) {
                                sb.append(formatCommand(cmd));
                        }
                }

                sb.append("\n═══════════════════════════════════════════════════════════════════════════════\n");
                sb.append("                           COMANDOS UPDATE                                      \n");
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n\n");

                for (CommandExample cmd : COMMANDS) {
                        if (cmd.operation.equals("UPDATE")) {
                                sb.append(formatCommand(cmd));
                        }
                }

                sb.append("\n═══════════════════════════════════════════════════════════════════════════════\n");
                sb.append("                         COMANDOS DE REPORTES                                   \n");
                sb.append("═══════════════════════════════════════════════════════════════════════════════\n\n");
                sb.append(obtenerComandosReportes());

                return sb.toString();
        }

        public static String obtenerComandosReportes() {
                StringBuilder sb = new StringBuilder();
                int width = 80;

                // Dashboard
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE DASHBOARD", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Dashboard general del mes actual con todas las estadísticas")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s", "Parámetros: Ninguno")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s", "Ejemplo: REPORTEDASHBOARD"))
                                .append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Ingresos
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE INGRESOS", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Obtiene los ingresos totales de un mes específico")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s", "Parámetros: año, mes")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s", "Ejemplo: REPORTEINGRESOS[2025, 10]"))
                                .append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Ranking Barberos
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE RANKING BARBEROS", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ranking de barberos por ingresos en un período")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Parámetros: fecha_inicio (YYYY-MM-DD), fecha_fin (YYYY-MM-DD)")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ejemplo: REPORTERANKINGBARBEROS[2025-10-01, 2025-10-31]")).append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Servicios Populares
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE SERVICIOS POPULARES", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Top de servicios más solicitados en un período")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(
                                String.format("%-" + (width - 4) + "s", "Parámetros: fecha_inicio, fecha_fin, limite"))
                                .append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ejemplo: REPORTESERVICIOSPOPULARES[2025-01-01, 2025-12-31, 5]")).append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Clientes Frecuentes
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE CLIENTES FRECUENTES", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Top de clientes con más visitas en un período")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(
                                String.format("%-" + (width - 4) + "s", "Parámetros: fecha_inicio, fecha_fin, limite"))
                                .append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ejemplo: REPORTECLIENTESFRECUENTES[2025-01-01, 2025-12-31, 10]")).append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Distribución Estados
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE DISTRIBUCIÓN ESTADOS", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Distribución de reservas por estado en un período")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s", "Parámetros: fecha_inicio, fecha_fin"))
                                .append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ejemplo: REPORTEDISTRIBUCIONESTADOS[2025-07-01, 2025-09-30]")).append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Horas Pico
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE HORAS PICO", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Horas del día con más actividad en un período")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s", "Parámetros: fecha_inicio, fecha_fin"))
                                .append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ejemplo: REPORTEHORASPICO[2025-10-01, 2025-10-31]")).append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Días Ocupados
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE DÍAS OCUPADOS", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Días de la semana con más reservas en un período")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s", "Parámetros: fecha_inicio, fecha_fin"))
                                .append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ejemplo: REPORTEDIASOCUPADOS[2025-10-01, 2025-10-31]")).append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Métodos de Pago
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE MÉTODOS DE PAGO", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Distribución de pagos por método en un período")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s", "Parámetros: fecha_inicio, fecha_fin"))
                                .append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ejemplo: REPORTEMETODOSPAGO[2025-01-01, 2025-12-31]")).append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Consumo Productos
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE CONSUMO PRODUCTOS", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Consumo de productos en servicios durante un período")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s", "Parámetros: fecha_inicio, fecha_fin"))
                                .append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ejemplo: REPORTECONSUMOPRODUCTOS[2025-10-01, 2025-10-31]")).append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                // Estadísticas Barbero
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");
                sb.append("│ ").append(centerText("REPORTE ESTADÍSTICAS BARBERO", width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Estadísticas detalladas de un barbero específico")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Parámetros: id_barbero, fecha_inicio, fecha_fin")).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");
                sb.append("│ ").append(String.format("%-" + (width - 4) + "s",
                                "Ejemplo: REPORTEESTADISTICASBARBERO[1, 2025-10-01, 2025-10-31]")).append(" │\n");
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                return sb.toString();
        }

        private static String formatCommand(CommandExample cmd) {
                StringBuilder sb = new StringBuilder();
                int width = 80;
                String title = cmd.operation + " " + cmd.entity;
                String description = cmd.description;
                String parameters = "Parámetros: " + cmd.parameters;
                String example = "Ejemplo: " + cmd.example;

                // Borde superior
                sb.append("┌").append("─".repeat(width - 2)).append("┐\n");

                // Título centrado
                sb.append("│ ").append(centerText(title, width - 4)).append(" │\n");
                sb.append("├").append("─".repeat(width - 2)).append("┤\n");

                // Descripción (dividida si es necesario)
                List<String> descLines = wrapText(description, width - 6);
                for (String line : descLines) {
                        sb.append("│ ").append(String.format("%-" + (width - 4) + "s", line)).append(" │\n");
                }

                sb.append("├").append("─".repeat(width - 2)).append("┤\n");

                // Parámetros (divididos si es necesario)
                List<String> paramLines = wrapText(parameters, width - 6);
                for (String line : paramLines) {
                        sb.append("│ ").append(String.format("%-" + (width - 4) + "s", line)).append(" │\n");
                }

                sb.append("├").append("─".repeat(width - 2)).append("┤\n");

                // Ejemplo (dividido si es necesario)
                List<String> exampleLines = wrapText(example, width - 6);
                for (String line : exampleLines) {
                        sb.append("│ ").append(String.format("%-" + (width - 4) + "s", line)).append(" │\n");
                }

                // Borde inferior
                sb.append("└").append("─".repeat(width - 2)).append("┘\n\n");

                return sb.toString();
        }

        private static String centerText(String text, int width) {
                if (text.length() >= width)
                        return text;
                int leftPad = (width - text.length()) / 2;
                int rightPad = width - text.length() - leftPad;
                return " ".repeat(leftPad) + text + " ".repeat(rightPad);
        }

        private static List<String> wrapText(String text, int maxWidth) {
                List<String> lines = new ArrayList<>();
                if (text == null || text.isEmpty()) {
                        lines.add("");
                        return lines;
                }

                String[] words = text.split(" ");
                StringBuilder currentLine = new StringBuilder();

                for (String word : words) {
                        if (currentLine.length() + word.length() + 1 > maxWidth) {
                                if (currentLine.length() > 0) {
                                        lines.add(currentLine.toString());
                                        currentLine = new StringBuilder();
                                }
                        }
                        if (currentLine.length() > 0) {
                                currentLine.append(" ");
                        }
                        currentLine.append(word);
                }

                if (currentLine.length() > 0) {
                        lines.add(currentLine.toString());
                }

                return lines;
        }

        public static String buscarComando(String entidad, String operacion) {
                for (CommandExample cmd : COMMANDS) {
                        if (cmd.entity.equalsIgnoreCase(entidad) && cmd.operation.equalsIgnoreCase(operacion)) {
                                return formatCommand(cmd);
                        }
                }
                return "Comando no encontrado para " + operacion + " " + entidad;
        }
}