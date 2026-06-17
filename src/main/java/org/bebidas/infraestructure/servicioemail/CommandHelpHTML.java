package org.bebidas.infraestructure.servicioemail;

import java.util.ArrayList;
import java.util.List;

public class CommandHelpHTML {

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
                                "CREATE", "ROLES",
                                "Crear un nuevo rol",
                                "nombre, descripcion",
                                "CREATEROLES[Administrador, Rol de administrador]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "USUARIOS",
                                "Crear un nuevo usuario",
                                "nombre, correo, clave, estado, rolId",
                                "CREATEUSUARIOS[Juan Perez, juan@email.com, pass123, activo, 1]"));

                /*
                 * COMMANDS.add(new CommandExample(
                 * "CREATE", "VENDEDORES",
                 * "Crear un nuevo vendedor con usuario",
                 * "ci, nombre, nombreUsuario, correoUsuario, claveUsuario, rolId",
                 * "CREATEVENDEDORES[12345678, Carlos Lopez, carlos, carlos@email.com, pass123, 1]"
                 * ));
                 */

                COMMANDS.add(new CommandExample(
                                "CREATE", "CATEGORIAS",
                                "Crear una nueva categoría",
                                "nombre",
                                "CREATECATEGORIAS[Cervezas]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "PRODUCTOS",
                                "Crear un nuevo producto",
                                "categoriaId,nombre, precio, codigo, descripcion, marca",
                                "CREATEPRODUCTOS[1, Cerveza Pilsen, 25.50, CER001, Cerveza pilsen premium, Pilsen]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "INVENTARIO",
                                "Crear un movimiento de inventario",
                                "productoId, cantidad, tipoMovimiento(ENTRADA/ SALIDA), glosa,[idDetalleCompra]o [idDetalleVenta]",
                                "CREATEINVENTARIO[1, 100, ENTRADA, Compra inicial, 2]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "CARRITOS",
                                "Crear un nuevo carrito de compras",
                                "clienteId",
                                "CREATECARRITOS[1]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "ITEMCARRITOS",
                                "Crear un item en el carrito de compras",
                                "carritoId, productoId, cantidad, precio",
                                "CREATEITEMCARRITOS[1, 1, 2, 25.50]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "COMPRAS",
                                "Crear una nueva compra a proveedor",
                                "proveedorId, descripcion",
                                "CREATECOMPRAS[1, Compra de cervezas]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "DETALLECOMPRAS",
                                "Crear un detalle de compra",
                                "compraId, productoId, cantidad, precioUnitario",
                                "CREATEDETALLECOMPRAS[1, 1, 10, 25.50]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "PROVEEDORES",
                                "Crear un nuevo proveedor",
                                "nombre, telefono, direccion, nit, correo",
                                "CREATEPROVEEDORES[Cerveceria Nacional, 555-1234, Calle Principal 123, 123456789, proveedor@email.com]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "CLIENTES",
                                "Crear un nuevo cliente con usuario",
                                "ci, nombre, telefono, direccion, estado, nombreUsuario, correoUsuario, claveUsuario, [rolId]",
                                "CREATECLIENTES[87654321, Maria Garcia, 555-5678, Calle Secundaria 456, A, maria, maria@email.com, pass123, 1]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "VENTAS",
                                "Crear una nueva venta",
                                "clienteId, tipo(credito/contado), [numeroCuotas]",
                                "CREATEVENTAS[1, credito,2]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "VENTASCARRITO",
                                "Crear venta desde carrito",
                                "tipo(credito/contado), carritoId, [numeroCuotas]",
                                "CREATEVENTASCARRITO[credito ,5,2]"));

                /*
                 * COMMANDS.add(new CommandExample(
                 * "CREATE", "CREDITOS",
                 * "Crear un nuevo crédito",
                 * "ventaId, montoTotal, [numeroCuotas], [estado]",
                 * "CREATECREDITOS[1, 100.00, 3, ACTIVO]"));
                 */

                COMMANDS.add(new CommandExample(
                                "CREATE", "DETALLEVENTAS",
                                "Crear un detalle de venta",
                                "ventaId, productoId, cantidad, precioUnitario",
                                "CREATEDETALLEVENTAS[1, 1, 5, 30.00]"));

                COMMANDS.add(new CommandExample(
                                "CREATE", "PAGOS",
                                "Crear un pago de venta",
                                "ventaId, tipoPago(qr,efectivo), monto, nombrePersona, email",
                                "CREATEPAGOS[1, efectivo, 100.00, Juan Perez, juan@email.com]"));

                /*
                 * COMMANDS.add(new CommandExample(
                 * "CREATE", "VENTA_COMPLETA",
                 * "Crear venta completa con detalle, crédito y pago",
                 * "clienteId, tipo, numeroCuotas, metodoPago, productoId, cantidad, precioUnitario, tipoPago, montoPago"
                 * ,
                 * "CREATEVENTA_COMPLETA[1, contado, , efectivo, 1, 2, 50.00, efectivo, 100.00]"
                 * ));
                 */
                // UPDATE commands
                COMMANDS.add(new CommandExample(
                                "UPDATE", "ROLES",
                                "Actualizar un rol",
                                "id, nombre, descripcion, [activo]",
                                "UPDATEROLES[1, Administrador, Rol administrativo, true]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "USUARIOS",
                                "Actualizar un usuario",
                                "id, nombre, correo, clave, estado, rolId",
                                "UPDATEUSUARIOS[1, Juan Perez, juan@email.com, newpass, activo, 1]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "VENDEDORES",
                                "Actualizar un vendedor",
                                "id, ci, nombre",
                                "UPDATEVENDEDORES[1, 12345678, Carlos Lopez]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "CATEGORIAS",
                                "Actualizar una categoría",
                                "id, nombre",
                                "UPDATECATEGORIAS[1, Vinos]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "PRODUCTOS",
                                "Actualizar un producto",
                                "id, nombre, precio, codigo, categoriaId, descripcion",
                                "UPDATEPRODUCTOS[1, Cerveza Lager, 28.00, CER002, 1, Cerveza lager premium]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "INVENTARIO",
                                "Actualizar un movimiento de inventario",
                                "id, productoId, cantidad, [tipoMovimiento], glosa",
                                "UPDATEINVENTARIO[1, 1, 150, ENTRADA, Actualización de stock]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "CARRITOS",
                                "Actualizar un carrito",
                                "id, usuarioId, sessionId",
                                "UPDATECARRITOS[1, 1, session456]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "COMPRAS",
                                "Actualizar una compra",
                                "id, proveedorId, descripcion, estado",
                                "UPDATECOMPRAS[1, 1, Compra de vinos, COMPLETADA]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "PROVEEDORES",
                                "Actualizar un proveedor",
                                "id, nombre, telefono, direccion, nit, correo",
                                "UPDATEPROVEEDORES[1, Cerveceria Nacional, 555-1234, Calle Nueva 789, 123456789, nuevo@email.com]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "CLIENTES",
                                "Actualizar un cliente",
                                "id, ci, nombre, telefono, direccion, estado",
                                "UPDATECLIENTES[1, 87654321, Maria Garcia, 555-5678, Calle Actualizada 101, A]"));

                COMMANDS.add(new CommandExample(
                                "UPDATE", "VENTAS",
                                "Actualizar una venta",
                                "id, clienteId, usuarioId, montoTotal, saldo, estado",
                                "UPDATEVENTAS[1, 1, 2, 75.00, 25.00, PENDIENTE]"));

                /*
                 * COMMANDS.add(new CommandExample(
                 * "UPDATE", "CREDITOS",
                 * "Actualizar un crédito",
                 * "id, ventaId, montoTotal, numeroCuotas, estado",
                 * "UPDATECREDITOS[1, 1, 150.00, 4, ACTIVO]"));
                 */
        }

        public static String obtenerComandosDisponibles() {
                StringBuilder html = new StringBuilder();

                html.append("<!DOCTYPE html>\n");
                html.append("<html>\n<head>\n");
                html.append("<meta charset='UTF-8'>\n");
                html.append("<style>\n");
                html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }\n");
                html.append(".container { max-width: 900px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }\n");
                html.append("h1 { color: #2c3e50; text-align: center; border-bottom: 3px solid #3498db; padding-bottom: 15px; }\n");
                html.append("h2 { color: #34495e; margin-top: 30px; border-left: 4px solid #3498db; padding-left: 15px; }\n");
                html.append("h3 { color: #16a085; margin-top: 25px; }\n");
                html.append(".command-box { background: #ecf0f1; border-left: 4px solid #3498db; padding: 15px; margin: 15px 0; border-radius: 5px; }\n");
                html.append(".command-title { font-weight: bold; color: #2980b9; font-size: 1.1em; margin-bottom: 8px; }\n");
                html.append(".command-desc { color: #555; margin: 5px 0; }\n");
                html.append(".command-params { color: #7f8c8d; font-size: 0.9em; margin: 5px 0; }\n");
                html.append(".command-example { background: #fff; padding: 10px; border-radius: 3px; font-family: monospace; color: #c0392b; margin-top: 8px; border: 1px solid #ddd; }\n");
                html.append("ul { list-style-type: none; padding-left: 0; }\n");
                html.append("li { background: #fff; margin: 8px 0; padding: 10px; border-radius: 5px; border-left: 3px solid #95a5a6; }\n");
                html.append(".entity-list { background: #e8f4f8; padding: 15px; border-radius: 5px; margin: 10px 0; }\n");
                html.append(".report-box { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 10px 0; border-radius: 5px; }\n");
                html.append(".general-command-box { background: #e8f8f5; border-left: 4px solid #1abc9c; padding: 15px; margin: 10px 0; border-radius: 5px; }\n");
                html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }\n");
                html.append("th { background: #3498db; color: white; padding: 12px; text-align: left; font-weight: bold; }\n");
                html.append("td { padding: 12px; border-bottom: 1px solid #ddd; background: #fff; }\n");
                html.append("tr:hover td { background: #f5f5f5; }\n");
                html.append(".entity-name { font-weight: bold; color: #2c3e50; background: #ecf0f1 !important; padding: 10px; }\n");
                html.append(".example-code { font-family: monospace; color: #c0392b; background: #fef9e7; padding: 5px; border-radius: 3px; }\n");
                html.append("</style>\n");
                html.append("</head>\n<body>\n");
                html.append("<div class='container'>\n");

                // Encabezado
                html.append("<h1>📋 COMANDOS DISPONIBLES DEL SISTEMA DE LICORERÍA</h1>\n");

                // Comandos generales
                html.append("<h2>⚙️ COMANDOS GENERALES</h2>\n");
                html.append("<table>\n");
                html.append("<thead>\n");
                html.append("<tr>\n");
                html.append("<th>Comando</th>\n");
                html.append("<th>Descripción</th>\n");
                html.append("<th>Ejemplo</th>\n");
                html.append("</tr>\n");
                html.append("</thead>\n");
                html.append("<tbody>\n");

                html.append("<tr>\n");
                html.append("<td class='entity-name'>📋 LISTAR&lt;entidad&gt;[*]</td>\n");
                html.append("<td>Listar todos los registros de una entidad</td>\n");
                html.append("<td><span class='example-code'>LISTARUSUARIOS[*]</span></td>\n");
                html.append("</tr>\n");

                html.append("<tr>\n");
                html.append("<td class='entity-name'>➕ CREATE&lt;entidad&gt;[params...]</td>\n");
                html.append("<td>Crear un nuevo registro en la entidad especificada</td>\n");
                html.append("<td><span class='example-code'>CREATEUSUARIOS[juan, perez, juan@mail.com, ...]</span></td>\n");
                html.append("</tr>\n");

                html.append("<tr>\n");
                html.append("<td class='entity-name'>✏️ UPDATE&lt;entidad&gt;[id, params...]</td>\n");
                html.append("<td>Actualizar un registro existente por su ID</td>\n");
                html.append("<td><span class='example-code'>UPDATEUSUARIOS[1, juan, lopez, ...]</span></td>\n");
                html.append("</tr>\n");

                html.append("<tr>\n");
                html.append("<td class='entity-name'>🗑️ DELETE&lt;entidad&gt;[id]</td>\n");
                html.append("<td>Eliminar un registro por su ID</td>\n");
                html.append("<td><span class='example-code'>DELETEUSUARIOS[1]</span></td>\n");
                html.append("</tr>\n");

                html.append("<tr>\n");
                html.append("<td class='entity-name'>🔍 GET&lt;entidad&gt;[id]</td>\n");
                html.append("<td>Obtener un registro específico por su ID</td>\n");
                html.append("<td><span class='example-code'>GETUSUARIOS[1]</span></td>\n");
                html.append("</tr>\n");

                html.append("<tr>\n");
                html.append("<td class='entity-name'>❓ HELP</td>\n");
                html.append("<td>Mostrar esta ayuda con todos los comandos disponibles</td>\n");
                html.append("<td><span class='example-code'>HELP</span></td>\n");
                html.append("</tr>\n");

                html.append("</tbody>\n");
                html.append("</table>\n");

                // Entidades disponibles
                html.append("<h2>🗂️ ENTIDADES DISPONIBLES</h2>\n");
                html.append("<div class='entity-list'>\n");
                html.append("<strong>ROLES</strong>, <strong>USUARIOS</strong>, <strong>VENDEDORES</strong>, <strong>CATEGORIAS</strong>, <strong>PRODUCTOS</strong>, <strong>INVENTARIO</strong>, <strong>CARRITOS</strong>, <strong>COMPRAS</strong>, <strong>PROVEEDORES</strong>, <strong>CLIENTES</strong>, <strong>VENTAS</strong>, <strong>CREDITOS</strong>\n");
                html.append("</div>\n");

                // Comandos CREATE
                html.append("<h2>➕ COMANDOS CREATE</h2>\n");
                html.append("<table>\n");
                html.append("<thead>\n");
                html.append("<tr>\n");
                html.append("<th>Entidad</th>\n");
                html.append("<th>Descripción</th>\n");
                html.append("<th>Parámetros</th>\n");
                html.append("<th>Ejemplo</th>\n");
                html.append("</tr>\n");
                html.append("</thead>\n");
                html.append("<tbody>\n");
                for (CommandExample cmd : COMMANDS) {
                        if (cmd.operation.equals("CREATE")) {
                                html.append("<tr>\n");
                                html.append("<td class='entity-name'>").append(cmd.entity).append("</td>\n");
                                html.append("<td>").append(cmd.description).append("</td>\n");
                                html.append("<td>").append(cmd.parameters).append("</td>\n");
                                html.append("<td><span class='example-code'>").append(cmd.example)
                                                .append("</span></td>\n");
                                html.append("</tr>\n");
                        }
                }
                html.append("</tbody>\n");
                html.append("</table>\n");

                // Comandos UPDATE
                html.append("<h2>✏️ COMANDOS UPDATE</h2>\n");
                html.append("<table>\n");
                html.append("<thead>\n");
                html.append("<tr>\n");
                html.append("<th>Entidad</th>\n");
                html.append("<th>Descripción</th>\n");
                html.append("<th>Parámetros</th>\n");
                html.append("<th>Ejemplo</th>\n");
                html.append("</tr>\n");
                html.append("</thead>\n");
                html.append("<tbody>\n");
                for (CommandExample cmd : COMMANDS) {
                        if (cmd.operation.equals("UPDATE")) {
                                html.append("<tr>\n");
                                html.append("<td class='entity-name'>").append(cmd.entity).append("</td>\n");
                                html.append("<td>").append(cmd.description).append("</td>\n");
                                html.append("<td>").append(cmd.parameters).append("</td>\n");
                                html.append("<td><span class='example-code'>").append(cmd.example)
                                                .append("</span></td>\n");
                                html.append("</tr>\n");
                        }
                }
                html.append("</tbody>\n");
                html.append("</table>\n");

                html.append("</div>\n</body>\n</html>");

                return html.toString();
        }

        private static String obtenerComandosReportesHTML() {
                return ""; // Reportes no implementados en este sistema
        }

        private static String formatCommandHTML(CommandExample cmd) {
                StringBuilder html = new StringBuilder();

                html.append("<div class='command-box'>\n");
                html.append("<div class='command-title'>").append(cmd.operation).append(" ").append(cmd.entity)
                                .append("</div>\n");
                html.append("<div class='command-desc'>").append(cmd.description).append("</div>\n");
                html.append("<div class='command-params'>Parámetros: ").append(cmd.parameters).append("</div>\n");
                html.append("<div class='command-example'>").append(cmd.example).append("</div>\n");
                html.append("</div>\n");

                return html.toString();
        }

        public static String buscarComando(String entidad, String operacion) {
                for (CommandExample cmd : COMMANDS) {
                        if (cmd.entity.equalsIgnoreCase(entidad) && cmd.operation.equalsIgnoreCase(operacion)) {
                                return formatCommandHTML(cmd);
                        }
                }
                return "<p style='color: red;'>Comando no encontrado para " + operacion + " " + entidad + "</p>";
        }
}
