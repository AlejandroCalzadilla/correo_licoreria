package org.bebidas.modules.mail.crud_seleccion;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import org.bebidas.core.util.MensajesError;
import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.ItemCarrito;
import org.bebidas.modules.carrito.mappers.CarritoMapper;
import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.categorias.mappers.CategoriaMapper;
import org.bebidas.modules.categorias.validators.CategoriaValidator;
import org.bebidas.modules.compras.validators.CompraValidator;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.clientes.mappers.ClienteMapper;
import org.bebidas.modules.compras.mappers.CompraMapper;
import org.bebidas.modules.compras.models.Compra;
import org.bebidas.modules.compras.models.DetalleCompra;
import org.bebidas.modules.inventario.Inventario;
import org.bebidas.modules.inventario.Producto;
import org.bebidas.modules.inventario.mappers.InventarioMapper;
import org.bebidas.modules.inventario.mappers.ProductoMapper;
import org.bebidas.modules.pagos.Pago;
import org.bebidas.modules.proveedor.Proveedor;
import org.bebidas.modules.proveedor.mappers.ProveedorMapper;
import org.bebidas.modules.usuarios.Rol;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.usuarios.mappers.UsuarioMapper;
import org.bebidas.modules.ventas.Venta;
import org.bebidas.modules.ventas.mappers.DetalleVentaMapper;
import org.bebidas.modules.ventas.mappers.VentaMapper;

public class Crear {

    private final ServiceProvider services = ServiceProvider.getInstance();

    /**
     * Ejecuta creación de entidades basándose en los parámetros recibidos (como
     * string)
     * Los parámetros vienen en formato: param1,param2,param3...
     */
    public String ejecutarCrear(String entidad, String parametros) throws SQLException {
        String respuesta = "";
        try {
            String[] params = parametros.split(",");
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].trim();
            }

            switch (entidad) {
                case "ROLES":
                    // CREATEROLESROLES[nombre, descripcion, activo]
                    respuesta = crearRol(params);
                    break;

                case "USUARIOS":
                    // CREATEUSUARIOS[nombre, correo, clave, estado]
                    respuesta = crearUsuario(params);
                    break;
                case "CATEGORIAS":
                    // CREATECATEGORIAS[nombre, descripcion, activo, tipo]
                    respuesta = crearCategoria(params);
                    break;
                case "PRODUCTOS":
                    // CREATEPRODUCTOS[nombre, precio, categoriaId, descripcion]
                    respuesta = crearProducto(params);
                    break;
                case "INVENTARIO":
                    // CREATEINVENTARIO[productoId, cantidad, tipoMovimiento, glosa,
                    // idDetalleCompra, idDetalleVenta]
                    respuesta = crearInventario(params);
                    break;
                case "CARRITOS":
                    // CREATECARRITOS[usuarioId, sessionId]
                    respuesta = crearCarrito(params);
                    break;
                case "ITEMCARRITOS":
                    // CREATEITEMCARRITOS[carritoId, productoId, cantidad, precio]
                    respuesta = crearItemCarrito(params);
                    break;
                case "COMPRAS":
                    // CREATECOMPRAS[proveedorId, descripcion, estado]
                    respuesta = crearCompra(params);
                    break;
                case "PROVEEDORES":
                    // CREATEPROVEEDORES[nombre, telefono, direccion, usuarioId]
                    respuesta = crearProveedor(params);
                    break;
                case "CLIENTES":
                    // CREATECLIENTES[ci, nombre, telefono, direccion, estado, nombreUsuario,
                    // correoUsuario, claveUsuario, [rolId]]
                    respuesta = crearCliente(params);
                    break;
                case "VENTAS":
                    // CREATEVENTAS[clienteId, tipo, numeroCuotas, metodoPago]
                    respuesta = crearVenta(params);
                    break;
                case "VENTASCARRITO":
                    // CREATEVENTASCONDETALLE[tipo, carritoId, numeroCuotas, metodoPago]
                    respuesta = crearVentaConDetalle(params);
                    break;
                case "DETALLECOMPRAS":
                    // CREATEDETALLECOMPRAS[compraId, productoId, cantidad, precioUnitario]
                    respuesta = crearDetalleCompra(params);
                    break;
                case "DETALLEVENTAS":
                    // CREATEDETALLEVENTAS[ventaId, productoId, cantidad, precioUnitario]
                    respuesta = crearDetalleVenta(params);
                    break;
                case "PAGOS":
                    // CREATEPAGOS[ventaId, tipoPago, monto, nombrePersona, email]
                    respuesta = crearPago(params);
                    break;
                default:
                    respuesta = "Entidad no encontrada 'Comando Incorrecto Favor Revisar Help'";
            }
            return respuesta;
        } catch (Exception e) {
            return MensajesError.paraCliente("crear " + entidad, e);
        }
    }

    private String crearCategoria(String[] params) {
        try {
            String error = CategoriaValidator.validarCrear(params);
            if (error != null)
                return error;
            Categoria categoriaCreada = services.getCategoriaService().save(new Categoria(params[0]));
            return "Categoría creada correctamente con ID: \n" + CategoriaMapper.obtenerUnoTable(categoriaCreada);
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearRol(String[] params) {
        try {
            if (params.length < 1)
                return "Se requieren: nombre, descripcion";
            Rol rol = new Rol();
            rol.setNombre(params[0]);
            rol.setDescripcion(params[1]);
            Rol rolCreado = services.getRolService().save(rol);
            return "Rol creado correctamente con ID: " + rolCreado.getId();
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearUsuario(String[] params) {
        try {
            if (params.length < 4)
                return "Se requieren: nombre, correo, clave, [estado]";
            Rol rol = services.getRolService().findById(Long.parseLong(params[4])).orElse(null);
            if (rol == null)
                return "Rol no encontrado con ID: " + params[4];
            Usuario usuario = new Usuario();
            usuario.setNombre(params[0]);
            usuario.setCorreo(params[1]);
            usuario.setClave(params[2]);
            usuario.setEstado(params.length > 3 ? params[3] : "activo");
            usuario.setRol(rol);
            Usuario usuarioCreado = services.getUsuarioService().save(usuario);
            return "Usuario creado correctamente con ID: \n" + UsuarioMapper.obtenerUnoTable(usuarioCreado);
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearProducto(String[] params) {
        try {
            if (params.length < 6)
                return "Se requieren:codigo, nombre,descripcion , precio, marca, categoriaId";
            Producto producto = new Producto();
            Categoria categoria = new Categoria();
            producto.setCodigo(params[0]);
            producto.setNombre(params[1]);
            producto.setDescripcion(params[2]);
            producto.setPrecio(new BigDecimal(params[3]));
            producto.setMarca(params[4]);
            categoria.setId(Long.parseLong(params[5]));
            producto.setCategoria(categoria);
            Producto productoCreado = services.getProductoService().save(producto);
            return "Producto creado correctamente con ID: \n" + ProductoMapper.obtenerUnoTable(productoCreado);
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearProveedor(String[] params) {
        try {
            if (params.length < 5)
                return "Se requieren: nombre, telefono, direccion";
            Proveedor proveedor = new Proveedor();
            proveedor.setNombre(params[0]);
            proveedor.setTelefono(params[1]);
            proveedor.setDireccion(params[2]);
            proveedor.setNit(params[3]);
            proveedor.setCorreo(params[4]);
            Proveedor proveedorCreado = services.getProveedorService().save(proveedor);
            return "Proveedor creado correctamente con ID: \n" + ProveedorMapper.obtenerUnoTable(proveedorCreado);
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearCliente(String[] params) {
        try {
            if (params.length < 8)
                return "Se requieren: ci, nombre, telefono, direccion, nombreUsuario, correoUsuario, claveUsuario, [rolId]";
            // Crear usuario primero
            Usuario usuario = new Usuario();
            usuario.setNombre(params[4]);
            usuario.setCorreo(params[5]);
            usuario.setClave(params[6]);
            usuario.setEstado("activo");
            Long rolId = params.length > 7 && !params[7].trim().isEmpty() ? Long.parseLong(params[7].trim()) : 4L;
            Rol rol = services.getRolService().findById(rolId).orElse(null);
            if (rol == null)
                return "Rol no encontrado con ID: " + rolId;
            usuario.setRol(rol);
            Usuario usuarioCreado = services.getUsuarioService().save(usuario);
            // Crear cliente
            Cliente cliente = new Cliente();
            cliente.setCi(params[0]);
            cliente.setNombre(params[1]);
            cliente.setTelefono(params[2]);
            cliente.setDireccion(params[3]);
            cliente.setEstado('A');
            cliente.setCreditoAprobado(false);
            cliente.setLimiteCredito(0.0);
            cliente.setUsuario(usuarioCreado);
            Cliente clienteCreado = services.getClienteService().save(cliente);
            return "Cliente creado correctamente con ID: \n" + ClienteMapper.obtenerUnoTable(clienteCreado);
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearCompra(String[] params) {
        try {
            String error = CompraValidator.validarCrear(params);
            if (error != null)
                return error;
            Compra compra = new Compra();
            Proveedor proveedor = new Proveedor();
            proveedor.setId(Long.parseLong(params[0]));
            compra.setProveedor(proveedor);
            compra.setDescripcion(params[1]);
            compra.setEstado("completada");
            compra.setFecha(LocalDate.now());
            Compra compraCreada = services.getCompraService().crearCompra(compra);

            return "Compra creada correctamente con ID: \n" + CompraMapper.obtenerUnoTable(compraCreada);
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearDetalleCompra(String[] params) {
        try {
            if (params.length < 4)
                return "Se requieren: compraId, productoId, cantidad, precioUnitario";
            DetalleCompra detalle = new DetalleCompra();
            Compra compra = new Compra();
            compra.setId(Long.parseLong(params[0]));
            detalle.setCompra(compra);
            Producto producto = new Producto();
            producto.setId(Long.parseLong(params[1]));
            detalle.setProducto(producto);
            detalle.setCantidad(Integer.parseInt(params[2]));
            detalle.setPrecioUnitario(new BigDecimal(params[3]));
            DetalleCompra detalleCreado = services.getDetalleCompraService().insertar(detalle);
            return "DetalleCompra creado correctamente con ID: " + detalleCreado.getId();
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearInventario(String[] params) {
        try {
            if (params.length < 4)
                return "Se requieren: productoId, cantidad, tipoMovimiento, glosa, [idDetalleCompra] o [idDetalleVenta]";
            Inventario inventario = new Inventario();
            Producto producto = new Producto();
            producto.setId(Long.parseLong(params[0]));
            inventario.setProducto(producto);
            inventario.setCantidad(Integer.parseInt(params[1]));
            if (params[2].equalsIgnoreCase("salida") || params[2].equalsIgnoreCase("entrada")) {
                inventario.setTipoMovimiento(params[2].toUpperCase());
            } else {
                throw new IllegalArgumentException("Tipo de movimiento inválido. Use 'ENTRADA' o 'SALIDA'.");
            }
            inventario.setGlosa(params[3]);
            // Verificar que al menos uno de idDetalleCompra o idDetalleVenta esté presente
            if (params.length <= 4 || (params[4].isEmpty() && (params.length <= 5 || params[5].isEmpty()))) {
                return "Se requiere al menos uno de: idDetalleCompra o idDetalleVenta";
            }
            Inventario inventarioCreado = services.getInventarioService().save(inventario);
            return "Inventario creado correctamente con ID: \n" + InventarioMapper.obtenerUnoTable(inventarioCreado);
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearCarrito(String[] params) {
        try {
            if (params.length < 1)
                return "Se requieren: clienteId";
            Carrito carrito = new Carrito();
            Usuario usuario = new Usuario();
            Optional<Cliente> cliente = services.getClienteService().findById(Long.parseLong(params[0].trim()));
            if (cliente.isEmpty())
                return "Cliente no encontrado con ID: " + params[0];

            Carrito carritoExistente = services.getCarritoService().buscarActivoPorCliente(cliente.get().getId());
            if (carritoExistente != null)
                return "Error: El cliente ya tiene un carrito activo con ID: " + carritoExistente.getId();

            Usuario usuarioEncontrado = services.getUsuarioService().findById(cliente.get().getUsuario().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Usuario no encontrado para el cliente ID: " + params[0]));
            usuario.setId(usuarioEncontrado.getId());
            carrito.setUsuario(usuario);
            carrito.setClienteId(cliente.get().getId());
            carrito.setSessionId(new Random().nextInt(100000, 999999) + "");
            carrito.setCreatedAt(LocalDateTime.now());
            carrito.setUpdatedAt(LocalDateTime.now());
            Carrito carritoCreado = services.getCarritoService().save(carrito);
            return "Carrito creado correctamente con ID: \n" + CarritoMapper.obtenerUnoTable(carritoCreado);
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearItemCarrito(String[] params) {
        try {
            if (params.length < 4)
                return "Se requieren: carritoId, productoId, cantidad, precio";
            ItemCarrito item = new ItemCarrito();
            Carrito carrito = new Carrito();
            carrito.setId(Long.parseLong(params[0]));
            item.setCarrito(carrito);
            Producto producto = new Producto();
            producto.setId(Long.parseLong(params[1]));
            item.setProducto(producto);
            item.setCantidad(Integer.parseInt(params[2]));
            item.setPrecio(new BigDecimal(params[3]));
            item.setCreatedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());
            ItemCarrito itemCreado = services.getItemCarritoService().save(item);
            return "ItemCarrito creado correctamente con ID: " + itemCreado.getId();
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearVenta(String[] params) {
        try {
            if (params.length < 2)
                return "Se requieren: clienteId, tipo, si es al credito:[numeroCuotas]";
            Long clienteId = Long.parseLong(params[0]);
            String tipo = params[1];
            String numeroCuotas = null;
            if (tipo.equalsIgnoreCase("credito")) {
                if (params.length < 3) {
                    return "Se requiere numeroCuotas";
                }
                numeroCuotas = params[2];
            } else {
                numeroCuotas = "0";
            }
            String metodoPago = null;
            if (services.getClienteService().findById(clienteId).isEmpty())
                return "Cliente no encontrado con ID: " + clienteId;
            Venta ventaCreada = services.getVentaService().crearVentaBasica(clienteId, tipo, numeroCuotas, metodoPago);
            return "Venta creada correctamente con ID: \n" + VentaMapper.obtenerUnoTable(ventaCreada);
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearDetalleVenta(String[] params) {
        try {

            if (params.length < 4)
                return "Se requieren: ventaId, productoId, cantidad, precioUnitario";
            Long ventaId = Long.parseLong(params[0]);
            Long productoId = Long.parseLong(params[1]);
            Integer cantidad = Integer.parseInt(params[2]);
            BigDecimal precioUnitario = new BigDecimal(params[3]);
            Venta ventaActualizada = services.getDetalleVentaService()
                    .procesarCreacionDetalleVenta(ventaId, productoId, cantidad, precioUnitario);

            String planPagosMsg = "";
            if (ventaActualizada.getTipo() != null && ventaActualizada.getTipo().equals("credito")) {
                try {
                    int numCuotas = Integer.parseInt(ventaActualizada.getNumeroCuotas());
                    String plan = services.getPagoService().obtenerPlanPagosFormateado(ventaActualizada.getMontoTotal(),
                            numCuotas);
                    planPagosMsg = "\nRESTRICCIÓN: Para ventas a crédito, cada pago de cuota debe ser exacto según el plan.\nPlan completo: "
                            + plan + " esto se actualiza por cada detalle creado" + "\n";
                } catch (Exception ex) {
                    // Ignorar
                }
            }

            System.out.println("DetalleVenta creado correctamente" +
                    "\nVenta actualizada - Monto Total: " + ventaActualizada.getMontoTotal() +
                    " | Saldo: " + ventaActualizada.getSaldo() + "\n"
                    + (ventaActualizada.getDetalles().isEmpty() ? ""
                            : DetalleVentaMapper.obtenerUnoTable(ventaActualizada.getDetalles().get(0)))
                    + planPagosMsg);
            return "DetalleVenta creado correctamente" +
                    "\nVenta actualizada - Monto Total: " + ventaActualizada.getMontoTotal() +
                    " | Saldo: " + ventaActualizada.getSaldo() + " \n"
                    + (ventaActualizada.getDetalles().isEmpty() ? ""
                            : DetalleVentaMapper.obtenerUnoTable(ventaActualizada.getDetalles().get(0)))
                    + planPagosMsg;
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearVentaConDetalle(String[] params) {
        try {
            System.out.println("DEBUG: crearVentaConDetalle - Parámetros recibidos: " + String.join(",", params));
            if (params.length < 2)
                return "Se requieren: tipo, carritoId, [numeroCuotas] si es credito";

            String tipo = params[0].trim().toLowerCase();
            if (!tipo.equals("credito") && !tipo.equals("contado")) {
                return "Tipo de venta invalido";
            }
            Long carritoId = Long.parseLong(params[1].trim());
            String numeroCuotas = null;
            String metodoPago = null;
            if (tipo.equals("credito")) {
                if (params.length < 3)
                    return "Se requiere numeroCuotas si es credito";
                numeroCuotas = params[2].trim();
            }
            Venta ventaCreada = services.getVentaService().crearVentaConDetalle(tipo, carritoId,
                    numeroCuotas, metodoPago);

            String planPagosMsg = "";
            if (tipo.equals("credito") && ventaCreada.getNumeroCuotas() != null) {
                try {
                    int numCuotas = Integer.parseInt(ventaCreada.getNumeroCuotas());
                    String plan = services.getPagoService().obtenerPlanPagosFormateado(ventaCreada.getMontoTotal(),
                            numCuotas);
                    planPagosMsg = "\nRESTRICCIÓN: Para ventas a crédito, cada pago de cuota debe ser exacto según el plan.\nPlan completo: "
                            + plan + "\n";
                } catch (Exception ex) {
                    // Ignorar
                }
            }

            return "Venta con detalle creada correctamente:\n" + VentaMapper.obtenerUnoTable(ventaCreada) +
                    planPagosMsg + "\nDetalles procesados desde carrito.";
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

    private String crearPago(String[] params) {
        try {
            if (params.length < 5)
                return "Se requieren: ventaId, tipoPago, monto, nombrePersona, email";
            Long ventaId = Long.parseLong(params[0]);
            String tipoPago = params[1].trim().toLowerCase();
            BigDecimal monto = new BigDecimal(params[2]);
            Pago pagoCreado = services.getPagoService()
                    .procesarPagoVenta(ventaId, tipoPago, monto, params[3], params[4]);
            Venta venta = services.getVentaService().findById(ventaId).orElse(null);
            BigDecimal nuevoSaldo = venta != null ? venta.getSaldo() : BigDecimal.ZERO;

            if (tipoPago.equals("qr")) {
                String qrSrc = pagoCreado.getQrImage();
                if (qrSrc != null && !qrSrc.startsWith("data:image")) {
                    qrSrc = "data:image/png;base64," + qrSrc;
                }
                
                return "<!-- HTML -->\n" +
                       "<div style=\"font-family: Arial, sans-serif; text-align: center; color: #2c3e50; padding: 20px;\">\n" +
                       "  <h2 style=\"color: #27ae60;\">¡Pago QR Generado Exitosamente!</h2>\n" +
                       "  <p>Escaneá este código QR con la app de tu banco para realizar el pago:</p>\n" +
                       "  <div style=\"margin: 20px 0;\">\n" +
                       "    <img src=\"" + qrSrc + "\" alt=\"Código QR de Pago\" style=\"max-width: 300px; border: 1px solid #ddd; padding: 10px; border-radius: 5px;\"/>\n" +
                       "  </div>\n" +
                       "  <p style=\"font-size: 14px; color: #7f8c8d;\"><strong>ID Pago:</strong> " + pagoCreado.getId() + " | <strong>Nro Pago:</strong> " + pagoCreado.getNroPago() + "</p>\n" +
                       "  <p style=\"font-size: 14px; color: #7f8c8d;\"><strong>Monto:</strong> Bs. " + pagoCreado.getMonto() + " | <strong>Estado:</strong> " + pagoCreado.getEstado() + "</p>\n" +
                       "  <p style=\"font-size: 14px; color: #7f8c8d;\"><strong>Nro Transacción:</strong> " + pagoCreado.getNroTransaccion() + "</p>\n" +
                       "  <hr style=\"border: none; border-top: 1px solid #eee; margin: 20px 0;\"/>\n" +
                       "  <p style=\"font-size: 12px; color: #95a5a6;\">Una vez que realices el pago, podés verificar el estado respondiendo con el comando <code>GETPAGOS[" + pagoCreado.getId() + "]</code>.</p>\n" +
                       "</div>";
            }

            return "Pago creado correctamente con ID: " + pagoCreado.getId() +
                    "\nNúmero de pago: " + pagoCreado.getNroPago() +
                    "\nNuevo saldo: " + nuevoSaldo;
        } catch (Exception e) {
            return MensajesError.paraCliente(e);
        }
    }

}
