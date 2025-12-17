package org.bebidas.modules.mail.crud_seleccion;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.compras.Compra;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.inventario.Inventario;
import org.bebidas.modules.inventario.Producto;
import org.bebidas.modules.proveedor.Proveedor;
import org.bebidas.modules.usuarios.Rol;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.vendedores.Vendedor;
import org.bebidas.modules.ventas.Venta;

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
                    // CREATEVENDEDORES[ci, nombre, usuarioId]
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
                    // CREATEINVENTARIO[productoId, cantidad, movimiento, descripcion]
                    respuesta = crearInventario(params);
                    break;

                case "CARRITOS":
                    // CREATECARRITOS[usuarioId, sessionId]
                    respuesta = crearCarrito(params);
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
                    // CREATECLIENTES[ci, nombre, telefono, direccion, estado, usuarioId]
                    respuesta = crearCliente(params);
                    break;

                case "VENTAS":
                    // CREATEVENTAS[clienteId, usuarioId, montoTotal]
                    respuesta = crearVenta(params);
                    break;

                case "CREDITOS":
                    // CREATECREDITOS[ventaId, montoTotal, numeroCuotas, estado]
                    respuesta = crearCredito(params);
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
            if (params.length < 2)
                return "Se requieren: nombre, descripcion, [activo]";

            Rol rol = new Rol();
            rol.setNombre(params[0]);
            rol.setDescripcion(params[1]);
            rol.setActivo(params.length > 2 ? Boolean.parseBoolean(params[2]) : true);

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
            return "Usuario creado correctamente con ID: " + usuarioCreado.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearVendedor(String[] params) {
        try {
            if (params.length < 2)
                return "Se requieren: ci, nombre, [usuarioId]";

            Vendedor vendedor = new Vendedor();
            vendedor.setCi(params[0]);
            vendedor.setNombre(params[1]);

            if (params.length > 2) {
                Usuario usuario = new Usuario();
                usuario.setId(Long.parseLong(params[2]));
                vendedor.setUsuario(usuario);
            }

            Vendedor vendedorCreado = services.getVendedorService().save(vendedor);
            return "Vendedor creado correctamente con ID: " + vendedorCreado.getId();
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
            return "Categoría creada correctamente con ID: " + categoriaCreada.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearProducto(String[] params) {
        try {
            if (params.length < 5)
                return "Se requieren: nombre, precio,codigo, categoriaId, descripcion";

            Producto producto = new Producto();
            producto.setNombre(params[0]);
            producto.setPrecio(new BigDecimal(params[1]));
            producto.setCodigo(params[2]);
            Categoria categoria = new Categoria();
            categoria.setId(Long.parseLong(params[3]));
            producto.setCategoria(categoria);
            producto.setDescripcion(params[4]);
            Producto productoCreado = services.getProductoService().save(producto);
            return "Producto creado correctamente con ID: " + productoCreado.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearInventario(String[] params) {
        try {
            if (params.length < 4)
                return "Se requieren: productoId, cantidad, [tipoMovimiento], [glosa]";

            Inventario inventario = new Inventario();

            Producto producto = new Producto();
            producto.setId(Long.parseLong(params[0]));
            inventario.setProducto(producto);
            inventario.setCantidad(Integer.parseInt(params[1]));
            inventario.setTipoMovimiento(params.length > 2 ? params[2] : "ENTRADA");
            inventario.setGlosa(params[3]);

            Inventario inventarioCreado = services.getInventarioService().save(inventario);
            return "Inventario creado correctamente con ID: " + inventarioCreado.getId();
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
            return "Carrito creado correctamente con ID: " + carritoCreado.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearCompra(String[] params) {
        try {
            if (params.length < 3)
                return "Se requieren: proveedorId, descripcion, estado";

            Compra compra = new Compra();

            Proveedor proveedor = new Proveedor();
            proveedor.setId(Long.parseLong(params[0]));
            compra.setProveedor(proveedor);
            compra.setDescripcion(params[1]);
            compra.setEstado(params[2]);
            compra.setFecha(LocalDate.now());

            Compra compraCreada = services.getCompraService().save(compra);
            return "Compra creada correctamente con ID: " + compraCreada.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
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
            return "Proveedor creado correctamente con ID: " + proveedorCreado.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearCliente(String[] params) {
        try {
            if (params.length < 6)
                return "Se requieren: ci, nombre, telefono, direccion, estado, usuarioId";

            Cliente cliente = new Cliente();
            cliente.setCi(params[0]);
            cliente.setNombre(params[1]);
            cliente.setTelefono(params[2]);
            cliente.setDireccion(params.length > 3 ? params[3] : "");
            cliente.setEstado(params[4].charAt(0));
            cliente.setCreditoAprobado(false);
            cliente.setLimiteCredito(0.0);
            Usuario usuario = new Usuario();
            usuario.setId(Long.parseLong(params[5]));
            cliente.setUsuario(usuario);
            Cliente clienteCreado = services.getClienteService().save(cliente);
            return "Cliente creado correctamente con ID: " + clienteCreado.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String crearVenta(String[] params) {
        try {
            if (params.length < 5)
                return "Se requieren: clienteId, usuarioId, [montoTotal], [saldo], [estado]";

            Venta venta = new Venta();
            Cliente cliente = new Cliente();
            cliente.setId(Long.parseLong(params[0]));
            venta.setCliente(cliente);
            Usuario usuario = new Usuario();
            usuario.setId(Long.parseLong(params[1]));
            venta.setUsuario(usuario);
            venta.setMontoTotal( new BigDecimal(params[2]));
            venta.setSaldo(new BigDecimal(params[3]));
            venta.setEstado(params[4]);
            venta.setNroVenta(new Random().nextInt(100000, 999999) + "");
            venta.setFecha(LocalDate.now());

            Venta ventaCreada = services.getVentaService().save(venta);
            return "Venta creada correctamente con ID: " + ventaCreada.getId();
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
            return "Crédito creado correctamente con ID: " + creditoCreado.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
