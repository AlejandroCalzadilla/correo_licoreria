package org.bebidas.modules.mail.crud_seleccion;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.mappers.CarritoMapper;
import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.categorias.mappers.CategoriaMapper;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.clientes.mappers.ClienteMapper;
import org.bebidas.modules.compras.mappers.CompraMapper;
import org.bebidas.modules.compras.models.Compra;
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

public class Actualizar {
    
    private final ServiceProvider services = ServiceProvider.getInstance();

    /**
     * Ejecuta actualización de entidades basándose en los parámetros recibidos (como string)
     * Los parámetros vienen en formato: id,param1,param2,param3...
     */
    public String ejecutarActualizar(String entidad, String parametros) throws SQLException {
        String respuesta = "";
        try {
            String[] params = parametros.split(",");
            for (int i = 0; i < params.length; i++) {
                params[i] = params[i].trim();
            }
            switch (entidad) {
                case "ROLES":
                    respuesta = actualizarRol(params);
                    break;
                case "USUARIOS":
                    respuesta = actualizarUsuario(params);
                    break;
                case "VENDEDORES":
                    respuesta = actualizarVendedor(params);
                    break;
                case "CATEGORIAS":
                    respuesta = actualizarCategoria(params);
                    break;
                case "PRODUCTOS":
                    respuesta = actualizarProducto(params);
                    break;
                case "INVENTARIO":
                    respuesta = actualizarInventario(params);
                    break;
                case "CARRITOS":
                    respuesta = actualizarCarrito(params);
                    break;
                case "COMPRAS":
                    respuesta = actualizarCompra(params);
                    break;
                case "PROVEEDORES":
                    respuesta = actualizarProveedor(params);
                    break;
                case "CLIENTES":
                    respuesta = actualizarCliente(params);
                    break;

                case "VENTAS":
                    respuesta = actualizarVenta(params);
                    break;

                case "CREDITOS":
                    respuesta = actualizarCredito(params);
                    break;

                default:
                    respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al actualizar " + entidad + ": " + e.getMessage();
        }
    }

    private String actualizarRol(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";
            Long id = Long.parseLong(params[0]);
            Rol rol = services.getRolService().findById(id).orElse(null);
            if (rol == null) return "Rol no encontrado con ID: " + id;
            if (params.length > 1) rol.setNombre(params[1]);
            if (params.length > 2) rol.setDescripcion(params[2]);
            if (params.length > 3) rol.setActivo(Boolean.parseBoolean(params[3]));
            Rol rolActualizado = services.getRolService().save(rol);
            return "Rol actualizado correctamente con ID: " + rolActualizado.getId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarUsuario(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";
            Long id = Long.parseLong(params[0]);
            Usuario usuario = services.getUsuarioService().findById(id).orElse(null);
            if (usuario == null) return "Usuario no encontrado con ID: " + id;
            if (params.length > 1) usuario.setNombre(params[1]);
            if (params.length > 2) usuario.setCorreo(params[2]);
            if (params.length > 3) usuario.setClave(params[3]);
            if (params.length > 4) usuario.setEstado(params[4]);
            if (params.length > 5) {
                Rol rol = services.getRolService().findById(Long.parseLong(params[5])).orElse(null);
                if (rol == null) return "Rol no encontrado con ID: " + params[5];
                usuario.setRol(rol);
            }
            Usuario usuarioActualizado = services.getUsuarioService().save(usuario);
            return "Usuario actualizado correctamente con ID: \n" + UsuarioMapper.obtenerUnoTable(usuarioActualizado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarVendedor(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";
            Long id = Long.parseLong(params[0]);
            Vendedor vendedor = services.getVendedorService().findById(id).orElse(null);
            if (vendedor == null) return "Vendedor no encontrado con ID: " + id;
            if (params.length > 1) vendedor.setCi(params[1]);
            if (params.length > 2) vendedor.setNombre(params[2]);
            if (params.length > 3) {
                Usuario usuario = new Usuario();
                usuario.setId(Long.parseLong(params[3]));
                vendedor.setUsuario(usuario);
            }
            Vendedor vendedorActualizado = services.getVendedorService().save(vendedor);
            return "Vendedor actualizado correctamente con ID: \n" + VendedorMapper.obtenerUnoTable(vendedorActualizado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarCategoria(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";

            Long id = Long.parseLong(params[0]);
            Categoria categoria = services.getCategoriaService().findById(id).orElse(null);
            if (categoria == null) return "Categoría no encontrada con ID: " + id;

            if (params.length > 1) categoria.setNombre(params[1]);

            Categoria categoriaActualizada = services.getCategoriaService().save(categoria);
            return "Categoría actualizada correctamente con ID: \n" + CategoriaMapper.obtenerUnoTable(categoriaActualizada);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarProducto(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";

            Long id = Long.parseLong(params[0]);
            Producto producto = services.getProductoService().findById(id).orElse(null);
            if (producto == null) return "Producto no encontrado con ID: " + id;

            if (params.length > 1) {
                Categoria categoria = new Categoria();
                categoria.setId(Long.parseLong(params[1]));
                producto.setCategoria(categoria);
            }
            if (params.length > 2) producto.setNombre(params[2]);
            if (params.length > 3) producto.setPrecio(new BigDecimal(params[3]));
            if (params.length > 4) producto.setCodigo(params[4]);
            if (params.length > 5) producto.setDescripcion(params[5]);
            if (params.length > 6) producto.setMarca(params[6]);

            Producto productoActualizado = services.getProductoService().save(producto);
            return "Producto actualizado correctamente con ID: \n" + ProductoMapper.obtenerUnoTable(productoActualizado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarInventario(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";

            Long id = Long.parseLong(params[0]);
            Inventario inventario = services.getInventarioService().findById(id).orElse(null);
            if (inventario == null) return "Inventario no encontrado con ID: " + id;
            if (params.length > 1) {
                Producto producto = new Producto();
                producto.setId(Long.parseLong(params[1]));
                inventario.setProducto(producto);
            }
            if (params.length > 2) inventario.setCantidad(Integer.parseInt(params[2]));
            if (params.length > 3) {
                if (params[3].equalsIgnoreCase("salida") || params[3].equalsIgnoreCase("entrada")) {
                    inventario.setTipoMovimiento(params[3].toUpperCase());
                } else {
                    throw new IllegalArgumentException("Tipo de movimiento inválido. Use 'ENTRADA' o 'SALIDA'.");
                }
            }
            if (params.length > 4) inventario.setGlosa(params[4]);
            // Verificar que al menos uno de idDetalleCompra o idDetalleVenta esté presente si se pasan parámetros
            if (params.length > 5 && (params[5].isEmpty() && (params.length <= 6 || params[6].isEmpty()))) {
                return "Se requiere al menos uno de: idDetalleCompra o idDetalleVenta";
            }
            // Manejar idDetalleVenta opcional
            Inventario inventarioActualizado = services.getInventarioService().save(inventario);
            return "Inventario actualizado correctamente con ID: \n" + InventarioMapper.obtenerUnoTable(inventarioActualizado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarCarrito(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";

            Long id = Long.parseLong(params[0]);
            Carrito carrito = services.getCarritoService().findById(id).orElse(null);
            if (carrito == null) return "Carrito no encontrado con ID: " + id;

            if (params.length > 1) {
                Usuario usuario = new Usuario();
                usuario.setId(Long.parseLong(params[1]));
                carrito.setUsuario(usuario);
            }
            if (params.length > 2) carrito.setSessionId(params[2]);
            carrito.setUpdatedAt(LocalDateTime.now());

            Carrito carritoActualizado = services.getCarritoService().save(carrito);
            return "Carrito actualizado correctamente con ID: \n" + CarritoMapper.obtenerUnoTable(carritoActualizado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarCompra(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";
            Long id = Long.parseLong(params[0]);
            Compra compra = services.getCompraService().findById(id).orElse(null);
            if (compra == null) return "Compra no encontrada con ID: " + id;
            if (params.length > 1) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(Long.parseLong(params[1]));
                compra.setProveedor(proveedor);
            }
            if (params.length > 2) compra.setDescripcion(params[2]);
            if (params.length > 3) compra.setEstado(params[3]);
            Compra compraActualizada = services.getCompraService().save(compra);
            return "Compra actualizada correctamente con ID: \n" + CompraMapper.obtenerUnoTable(compraActualizada);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarProveedor(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";
            Long id = Long.parseLong(params[0]);
            Proveedor proveedor = services.getProveedorService().findById(id).orElse(null);
            if (proveedor == null) return "Proveedor no encontrado con ID: " + id;
            if (params.length > 1) proveedor.setNombre(params[1]);
            if (params.length > 2) proveedor.setTelefono(params[2]);
            if (params.length > 3) proveedor.setDireccion(params[3]);
            if (params.length > 4) proveedor.setNit(params[4]);
            if (params.length > 5) proveedor.setCorreo(params[5]);
            Proveedor proveedorActualizado = services.getProveedorService().save(proveedor);
            return "Proveedor actualizado correctamente con ID: \n" + ProveedorMapper.obtenerUnoTable(proveedorActualizado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarCliente(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";

            Long id = Long.parseLong(params[0]);
            Cliente cliente = services.getClienteService().findById(id).orElse(null);
            if (cliente == null) return "Cliente no encontrado con ID: " + id;

            if (params.length > 1) cliente.setCi(params[1]);
            if (params.length > 2) cliente.setNombre(params[2]);
            if (params.length > 3) cliente.setTelefono(params[3]);
            if (params.length > 4) cliente.setDireccion(params[4]);
            if (params.length > 5) cliente.setEstado(params[5].charAt(0));
            if (params.length > 6) cliente.setCreditoAprobado(Boolean.parseBoolean(params[6]));
            if (params.length > 7) cliente.setLimiteCredito(Double.parseDouble(params[7]));
            if (params.length > 8) {
                Usuario usuario = new Usuario();
                usuario.setId(Long.parseLong(params[8]));
                cliente.setUsuario(usuario);
            }

            Cliente clienteActualizado = services.getClienteService().save(cliente);
            return "Cliente actualizado correctamente con ID: \n" + ClienteMapper.obtenerUnoTable(clienteActualizado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarVenta(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";
            Long id = Long.parseLong(params[0]);
            Venta venta = services.getVentaService().findById(id).orElse(null);
            if (venta == null) return "Venta no encontrada con ID: " + id;
            if (params.length > 1) {
                Cliente cliente = new Cliente();
                cliente.setId(Long.parseLong(params[1]));
                venta.setCliente(cliente);
            }
            if (params.length > 2) {
                Usuario usuario = new Usuario();
                usuario.setId(Long.parseLong(params[2]));
                venta.setUsuario(usuario);
            }
            if (params.length > 3) venta.setMontoTotal(new BigDecimal(params[3]));
            if (params.length > 4) venta.setSaldo(new BigDecimal(params[4]));
            if (params.length > 5) venta.setEstado(params[5]);
            Venta ventaActualizada = services.getVentaService().save(venta);
            return "Venta actualizada correctamente con ID: \n" + VentaMapper.obtenerUnoTable(ventaActualizada);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private String actualizarCredito(String[] params) {
        try {
            if (params.length < 1) return "Se requiere: id";
            Long id = Long.parseLong(params[0]);
            Credito credito = services.getCreditoService().findById(id).orElse(null);
            if (credito == null) return "Crédito no encontrado con ID: " + id;
            if (params.length > 1) {
                Venta venta = new Venta();
                venta.setId(Long.parseLong(params[1]));
                credito.setVenta(venta);
            }
            if (params.length > 2) credito.setMontoTotal(new BigDecimal(params[2]));
            if (params.length > 3) credito.setSaldo(new BigDecimal(params[3]));
            if (params.length > 4) credito.setNumeroCuotas(params[4]);
            if (params.length > 5) credito.setEstado(params[5]);
            Credito creditoActualizado = services.getCreditoService().save(credito);
            return "Crédito actualizado correctamente con ID: \n" + CreditoMapper.obtenerUnoTable(creditoActualizado);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
