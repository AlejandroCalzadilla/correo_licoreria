package org.bebidas.modules.mail.crud_seleccion;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.ItemCarrito;
import org.bebidas.modules.carrito.mappers.CarritoMapper;
import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.categorias.mappers.CategoriaMapper;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.clientes.mappers.ClienteMapper;
import org.bebidas.modules.compras.Compra;
import org.bebidas.modules.compras.DetalleCompra;
import org.bebidas.modules.compras.mappers.CompraMapper;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.creditos.mappers.CreditoMapper;
import org.bebidas.modules.inventario.Inventario;
import org.bebidas.modules.inventario.Producto;
import org.bebidas.modules.inventario.mappers.InventarioMapper;
import org.bebidas.modules.inventario.mappers.ProductoMapper;
import org.bebidas.modules.proveedor.Proveedor;
import org.bebidas.modules.proveedor.mappers.ProveedorMapper;
import org.bebidas.modules.usuarios.Rol;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.usuarios.mappers.UsuarioMapper;
import org.bebidas.modules.vendedores.Vendedor;
import org.bebidas.modules.vendedores.mappers.VendedorMapper;
import org.bebidas.modules.ventas.Venta;
import org.bebidas.modules.ventas.VentaMapper;
import org.bebidas.modules.ventas.DetalleVenta;
import org.bebidas.modules.ventas.Pago;

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

                case "VENDEDORES":
                    // CREATEVENDEDORES[ci, nombre, nombreUsuario, correoUsuario, claveUsuario,
                    // [rolId]]
                    respuesta = crearVendedor(params);
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

                case "VENTASCONDETALLE":
                    // CREATEVENTASCONDETALLE[clienteId, tipo, carritoId, numeroCuotas, metodoPago]
                    respuesta = crearVentaConDetalle(params);
                    break;

                case "CREDITOS":
                    // CREATECREDITOS[ventaId, montoTotal, numeroCuotas, estado]
                    respuesta = crearCredito(params);
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
                    respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al crear " + entidad + ": " + e.getMessage();
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
            return "Error: " + e.getMessage();
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
            return "Error: " + e.getMessage();
        }
    }

    private String crearVendedor(String[] params) {
        try {
            if (params.length < 5)
                return "Se requieren: ci, nombre, nombreUsuario, correoUsuario, claveUsuario, [rolId]";

            // Crear usuario primero
            Usuario usuario = new Usuario();
            usuario.setNombre(params[2]);
            usuario.setCorreo(params[3]);
            usuario.setClave(params[4]);
            usuario.setEstado("activo");
            Rol rol = services.getRolService().findById(5L).orElse(null);
            if (rol == null)
                return "Rol no encontrado con ID: 5";
            usuario.setRol(rol);
            Usuario usuarioCreado = services.getUsuarioService().save(usuario);

            // Crear vendedor
            Vendedor vendedor = new Vendedor();
            vendedor.setCi(params[0]);
            vendedor.setNombre(params[1]);
            vendedor.setUsuario(usuarioCreado);

            Vendedor vendedorCreado = services.getVendedorService().save(vendedor);
            return "Vendedor creado correctamente con ID: \n" + VendedorMapper.obtenerUnoTable(vendedorCreado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearCategoria(String[] params) {
        try {
            if (params.length < 1)
                return "Se requieren: nombre,";

            Categoria categoria = new Categoria();
            categoria.setNombre(params[0]);
            Categoria categoriaCreada = services.getCategoriaService().save(categoria);
            return "Categoría creada correctamente con ID: \n" + CategoriaMapper.obtenerUnoTable(categoriaCreada);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearProducto(String[] params) {
        try {
            if (params.length < 6)
                return "Se requieren: nombre, precio,codigo, categoriaId, descripcion";

            Producto producto = new Producto();
            Categoria categoria = new Categoria();
            categoria.setId(Long.parseLong(params[0]));
            producto.setNombre(params[1]);
            producto.setPrecio(new BigDecimal(params[2]));
            producto.setCodigo(params[3]);
            producto.setCategoria(categoria);
            producto.setDescripcion(params[4]);
            producto.setMarca(params[5]);
            Producto productoCreado = services.getProductoService().save(producto);
            return "Producto creado correctamente con ID: \n" + ProductoMapper.obtenerUnoTable(productoCreado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearInventario(String[] params) {
        try {
            if (params.length < 4)
                return "Se requieren: productoId, cantidad, tipoMovimiento, glosa, [idDetalleCompra], [idDetalleVenta]";

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
            return "Error: " + e.getMessage();
        }
    }

    private String crearCarrito(String[] params) {
        try {
            if (params.length < 1)
                return "Se requieren: usuarioId, [sessionId]";

            Carrito carrito = new Carrito();

            Usuario usuario = new Usuario();
            usuario.setId(Long.parseLong(params[0]));
            carrito.setUsuario(usuario);
            carrito.setSessionId(new Random().nextInt(100000, 999999) + "");
            carrito.setCreatedAt(LocalDateTime.now());
            carrito.setUpdatedAt(LocalDateTime.now());

            Carrito carritoCreado = services.getCarritoService().save(carrito);
            return "Carrito creado correctamente con ID: \n" + CarritoMapper.obtenerUnoTable(carritoCreado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
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
            return "Error: " + e.getMessage();
        }
    }

    private String crearCompra(String[] params) {
        try {
            if (params.length < 2)
                return "Se requieren: proveedorId, descripcion";
            Compra compra = new Compra();
            // Generar número de compra
            String nroCompra = generarSiguienteNroCompra();
            compra.setNroCompra(nroCompra);
            Proveedor proveedor = new Proveedor();
            proveedor.setId(Long.parseLong(params[0]));
            compra.setProveedor(proveedor);
            compra.setDescripcion(params[1]);
            compra.setEstado("completada");
            compra.setFecha(LocalDate.now());

            Compra compraCreada = services.getCompraService().save(compra);
            return "Compra creada correctamente con ID: \n" + CompraMapper.obtenerUnoTable(compraCreada);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String generarSiguienteNroCompra() {
        try {
            List<Compra> compras = services.getCompraService().findAll();
            int maxNumero = 0;
            for (Compra c : compras) {
                if (c.getNroCompra() != null && c.getNroCompra().startsWith("C-")) {
                    try {
                        int numero = Integer.parseInt(c.getNroCompra().substring(2));
                        if (numero > maxNumero) {
                            maxNumero = numero;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar si no es válido
                    }
                }
            }
            int siguiente = maxNumero + 1;
            return "C-" + String.format("%06d", siguiente);
        } catch (Exception e) {
            // En caso de error, usar un número por defecto
            return "C-000001";
        }
    }

    private String generarSiguienteNroVenta() {
        try {
            List<Venta> ventas = services.getVentaService().findAll();
            int maxNumero = 0;
            for (Venta v : ventas) {
                if (v.getNroVenta() != null && v.getNroVenta().startsWith("V-")) {
                    try {
                        int numero = Integer.parseInt(v.getNroVenta().substring(2));
                        if (numero > maxNumero) {
                            maxNumero = numero;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar si no es válido
                    }
                }
            }
            int siguiente = maxNumero + 1;
            return "V-" + String.format("%06d", siguiente);
        } catch (Exception e) {
            // En caso de error, usar un número por defecto
            return "V-000001";
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
            return "Error: " + e.getMessage();
        }
    }

    private String crearCliente(String[] params) {
        try {
            if (params.length < 7)
                return "Se requieren: ci, nombre, telefono, direccion, estado, nombreUsuario, correoUsuario, claveUsuario, [rolId]";

            // Crear usuario primero
            Usuario usuario = new Usuario();
            usuario.setNombre(params[5]);
            usuario.setCorreo(params[6]);
            usuario.setClave(params[7]);
            usuario.setEstado("activo");

            
                Rol rol = services.getRolService().findById(5L).orElse(null);
                if (rol == null)
                    return "Rol no encontrado con ID: 5";
                usuario.setRol(rol);
            

            Usuario usuarioCreado = services.getUsuarioService().save(usuario);

            // Crear cliente
            Cliente cliente = new Cliente();
            cliente.setCi(params[0]);
            cliente.setNombre(params[1]);
            cliente.setTelefono(params[2]);
            cliente.setDireccion(params[3]);
            cliente.setEstado(params[4].charAt(0));
            cliente.setCreditoAprobado(false);
            cliente.setLimiteCredito(0.0);
            cliente.setUsuario(usuarioCreado);
            Cliente clienteCreado = services.getClienteService().save(cliente);
            return "Cliente creado correctamente con ID: \n" + ClienteMapper.obtenerUnoTable(clienteCreado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearVenta(String[] params) {
        try {
            if (params.length < 2)
                return "Se requieren: clienteId, tipo, [numeroCuotas], [metodoPago]";

            Venta venta = new Venta();
            
            String nroVenta = generarSiguienteNroVenta();
            venta.setNroVenta(nroVenta);
            Cliente cliente = new Cliente();
            cliente.setId(Long.parseLong(params[0]));
            venta.setCliente(cliente);
            String tipo = params[1].toLowerCase();
            if (!tipo.equals("credito") && !tipo.equals("contado")) {
                return "Tipo debe ser 'credito' o 'contado'";
            }
            venta.setTipo(tipo);
            if (tipo.equals("credito")) {
                if (params.length < 3)
                    return "Se requiere numeroCuotas para tipo credito";
                venta.setNumeroCuotas(params[2]);
            }

            if (tipo.equals("contado")) {
                if (params.length < 4)
                    return "Se requiere metodoPago para tipo contado";
                venta.setMetodoPago(params[3]);
            }
            venta.setEstado("pendiente");
            venta.setEstadoPago("pendiente");
            venta.setFecha(LocalDate.now());
            Venta ventaCreada = services.getVentaService().save(venta);
            return "Venta creada correctamente con ID: \n" + VentaMapper.obtenerUnoTable(ventaCreada);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearVentaConDetalle(String[] params) {
        try {
            System.out.println("DEBUG: crearVentaConDetalle - Parámetros recibidos: " + String.join(",", params));
            
            if (params.length < 3)
                return "Se requieren: clienteId, tipo, carritoId, [numeroCuotas], [metodoPago]";

            Long carritoId = Long.parseLong(params[2]);
            Carrito carrito = services.getCarritoService().findById(carritoId).orElse(null);
            if (carrito == null)
                return "Carrito no encontrado con ID: " + carritoId;

            List<ItemCarrito> items = carrito.getItems();
            if (items == null || items.isEmpty())
                return "El carrito no tiene items";

            // Crear venta cabecera
            Venta venta = new Venta();
            String nroVenta = generarSiguienteNroVenta();
            venta.setNroVenta(nroVenta);

            Cliente cliente = new Cliente();
            cliente.setId(Long.parseLong(params[0]));
            venta.setCliente(cliente);

            String tipo = params[1].toLowerCase();
            System.out.println("DEBUG: Tipo de venta configurado: " + tipo);
            if (!tipo.equals("credito") && !tipo.equals("contado")) {
                return "Tipo debe ser 'credito' o 'contado'";
            }
            venta.setTipo(tipo);

            if (tipo.equals("credito")) {
                if (params.length < 4)
                    return "Se requiere numeroCuotas para tipo credito";
                venta.setNumeroCuotas(params[3]);
            }

            if (tipo.equals("contado")) {
                if (params.length < 5)
                    return "Se requiere metodoPago para tipo contado";
                venta.setMetodoPago(params[4]);
            }

            venta.setEstado("pendiente");
            venta.setEstadoPago("pendiente");
            venta.setFecha(LocalDate.now());

            Venta ventaCreada = services.getVentaService().save(venta);

            // Procesar items del carrito y crear detalles
            BigDecimal montoTotal = BigDecimal.ZERO;
            for (ItemCarrito item : items) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(ventaCreada);
                detalle.setProducto(item.getProducto());
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnitario(item.getPrecio());

                services.getDetalleVentaService().save(detalle);

                // Sumar monto
                BigDecimal subtotal = item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
                montoTotal = montoTotal.add(subtotal);
            }
            
            System.out.println("DEBUG: Monto total calculado: " + montoTotal + ", Items procesados: " + items.size());

            // Actualizar venta con monto total
            ventaCreada.setMontoTotal(montoTotal);
            ventaCreada.setSaldo(montoTotal);
            Venta ventaActualizada = services.getVentaService().save(ventaCreada);

            // Si es crédito, crear crédito
            if (venta.getTipo() != null && venta.getTipo().equals("credito")) {
                System.out.println("DEBUG: Creando crédito para venta tipo: " + venta.getTipo());
                System.out.println("DEBUG: Venta ID: " + ventaActualizada.getId() + ", MontoTotal: " + montoTotal);
                
                Credito credito = new Credito();
                credito.setVenta(ventaActualizada);
                credito.setMontoTotal(montoTotal);
                credito.setSaldo(montoTotal);
                credito.setNumeroCuotas(ventaActualizada.getNumeroCuotas() != null ? ventaActualizada.getNumeroCuotas() : "1");
                credito.setEstado("ACTIVO");
                credito.setFechaInicio(LocalDate.now());
                
                System.out.println("DEBUG: Guardando crédito...");
                Credito creditoCreado = services.getCreditoService().save(credito);
                System.out.println("DEBUG: Crédito creado con ID: " + (creditoCreado != null ? creditoCreado.getId() : "NULL"));
            } else {
                System.out.println("DEBUG: No se crea crédito - Tipo de venta: " + venta.getTipo());
            }

            return "Venta con detalle creada correctamente:\n" + VentaMapper.obtenerUnoTable(ventaActualizada) +
                   "\nDetalles procesados: " + items.size();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearCredito(String[] params) {
        try {
            if (params.length < 2)
                return "Se requieren: ventaId, montoTotal, [numeroCuotas], [estado]";

            Credito credito = new Credito();

            Venta venta = new Venta();
            venta.setId(Long.parseLong(params[0]));
            credito.setVenta(venta);

            credito.setMontoTotal(new BigDecimal(params[1]));
            credito.setSaldo(new BigDecimal(params[1]));
            credito.setNumeroCuotas(params.length > 2 ? params[2] : "1");
            credito.setEstado(params.length > 3 ? params[3] : "ACTIVO");
            credito.setFechaInicio(LocalDate.now());

            Credito creditoCreado = services.getCreditoService().save(credito);
            return "Crédito creado correctamente con ID: \n " + CreditoMapper.obtenerUnoTable(creditoCreado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
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

            DetalleCompra detalleCreado = services.getDetalleCompraService().save(detalle);
            return "DetalleCompra creado correctamente con ID: " + detalleCreado.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearDetalleVenta(String[] params) {
        try {
            if (params.length < 4)
                return "Se requieren: ventaId, productoId, cantidad, precioUnitario";

            // Obtener venta existente
            Long ventaId = Long.parseLong(params[0]);
            Venta venta = services.getVentaService().findById(ventaId).orElse(null);
            if (venta == null)
                return "Venta no encontrada con ID: " + ventaId;

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);

            Producto producto = new Producto();
            producto.setId(Long.parseLong(params[1]));
            detalle.setProducto(producto);

            int cantidad = Integer.parseInt(params[2]);
            BigDecimal precioUnitario = new BigDecimal(params[3]);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);

            DetalleVenta detalleCreado = services.getDetalleVentaService().save(detalle);

            // Actualizar venta con los montos
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
            BigDecimal montoActual = venta.getMontoTotal() != null ? venta.getMontoTotal() : BigDecimal.ZERO;
            BigDecimal nuevoMonto = montoActual.add(subtotal);

            venta.setMontoTotal(nuevoMonto);
            venta.setSaldo(nuevoMonto); // Saldo igual a monto total inicialmente

            services.getVentaService().save(venta);

            // Si es crédito, crear crédito
            if (venta.getTipo() != null && venta.getTipo().equals("credito")) {
                Credito credito = new Credito();
                credito.setVenta(venta);
                credito.setMontoTotal(nuevoMonto);
                credito.setSaldo(nuevoMonto);
                credito.setNumeroCuotas(venta.getNumeroCuotas() != null ? venta.getNumeroCuotas() : "1");
                credito.setEstado("ACTIVO");
                credito.setFechaInicio(LocalDate.now());
                services.getCreditoService().save(credito);
            }

            return "DetalleVenta creado correctamente con ID: " + detalleCreado.getId() +
                    "\nVenta actualizada - Monto Total: " + nuevoMonto + " | Saldo: " + nuevoMonto;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearPago(String[] params) {
        try {
            if (params.length < 5)
                return "Se requieren: ventaId, tipoPago, monto, nombrePersona, email";

            Long ventaId = Long.parseLong(params[0]);
            Venta venta = services.getVentaService().findById(ventaId).orElse(null);
            if (venta == null)
                return "Venta no encontrada con ID: " + ventaId;

            BigDecimal monto = new BigDecimal(params[2]);

            // Restricción: Si es crédito, no permite pagos >= al monto total
            if (venta.getTipo() != null && venta.getTipo().equals("credito")) {
                if (venta.getMontoTotal() != null && monto.compareTo(venta.getMontoTotal()) >= 0) {
                    return "RESTRICCIÓN: Para ventas a crédito, el pago no puede ser igual o mayor al monto total ("
                            + venta.getMontoTotal() + ")";
                }
            }

            Pago pago = new Pago();
            pago.setVenta(venta);
            pago.setTipoPago(params[1]);
            pago.setMonto(monto);
            pago.setNombrePersona(params[3]);
            pago.setEmail(params[4]);
            pago.setEstado("pendiente");
            pago.setFechaPago(LocalDateTime.now());
            pago.setCreatedAt(LocalDateTime.now());
            pago.setUpdatedAt(LocalDateTime.now());

            // Generar número de pago
            String nroPago = generarSiguienteNroPago();
            pago.setNroPago(nroPago);

            Pago pagoCreado = services.getPagoService().save(pago);

            // Actualizar saldo de venta
            BigDecimal nuevoSaldo = venta.getSaldo().subtract(monto);
            venta.setSaldo(nuevoSaldo);
            services.getVentaService().save(venta);

            // Actualizar saldo de crédito si existe
            if (venta.getTipo() != null && venta.getTipo().equals("credito")) {
                Credito credito = services.getCreditoService().findAll().stream()
                        .filter(c -> c.getVenta().getId().equals(ventaId))
                        .findFirst()
                        .orElse(null);

                if (credito != null) {
                    credito.setSaldo(nuevoSaldo);
                    services.getCreditoService().save(credito);
                }
            }

            return "Pago creado correctamente con ID: " + pagoCreado.getId() +
                    "\nNúmero de pago: " + nroPago +
                    "\nNuevo saldo: " + nuevoSaldo;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String generarSiguienteNroPago() {
        try {
            List<Pago> pagos = services.getPagoService().findAll();
            int maxNumero = 0;
            for (Pago p : pagos) {
                if (p.getNroPago() != null && p.getNroPago().startsWith("P-")) {
                    try {
                        int numero = Integer.parseInt(p.getNroPago().substring(2));
                        if (numero > maxNumero) {
                            maxNumero = numero;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar si no es válido
                    }
                }
            }
            int siguiente = maxNumero + 1;
            return "P-" + String.format("%06d", siguiente);
        } catch (Exception e) {
            return "P-000001";
        }
    }

}
