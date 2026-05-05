package org.bebidas.modules.mail.crud_seleccion;

import java.sql.SQLException;
import java.util.Optional;

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
import org.bebidas.modules.usuarios.mappers.RolMapper;
import org.bebidas.modules.usuarios.mappers.UsuarioMapper;
import org.bebidas.modules.vendedores.Vendedor;
import org.bebidas.modules.vendedores.mappers.VendedorMapper;
import org.bebidas.modules.ventas.Venta;
import org.bebidas.modules.ventas.VentaMapper;

public class SelectById {
    
    private final ServiceProvider services = ServiceProvider.getInstance();

    public String ejecutarConsultaSelectById(String entidad, Long id) throws SQLException {
        String respuesta = "";
        try {
            switch (entidad) {
                case "ROLES":
                    Optional<Rol> rol = services.getRolService().findById(id);
                    respuesta = rol.isPresent() ? RolMapper.obtenerUnoTable(rol.get()) : "Rol no encontrado";
                    break;

                case "USUARIOS":
                    Optional<Usuario> usuario = services.getUsuarioService().findById(id);
                    respuesta = usuario.isPresent() ? UsuarioMapper.obtenerUnoTable(usuario.get()) : "Usuario no encontrado";
                    break;

                case "VENDEDORES":
                    Optional<Vendedor> vendedor = services.getVendedorService().findById(id);
                    respuesta = vendedor.isPresent() ? VendedorMapper.obtenerUnoTable(vendedor.get()) : "Vendedor no encontrado";
                    break;

                case "CATEGORIAS":
                    Optional<Categoria> categoria = services.getCategoriaService().findById(id);
                    respuesta = categoria.isPresent() ? CategoriaMapper.obtenerUnoTable(categoria.get()) : "Categoría no encontrada";
                    break;

                case "PRODUCTOS":
                    Optional<Producto> producto = services.getProductoService().findById(id);
                    respuesta = producto.isPresent() ? ProductoMapper.obtenerUnoTable(producto.get()) : "Producto no encontrado";
                    break;

                case "INVENTARIO":
                    Optional<Inventario> inventario = services.getInventarioService().findById(id);
                    respuesta = inventario.isPresent() ? InventarioMapper.obtenerUnoTable(inventario.get()) : "Inventario no encontrado";
                    break;

                case "CARRITOS":
                    Optional<Carrito> carrito = services.getCarritoService().findById(id);
                    respuesta = carrito.isPresent() ? CarritoMapper.obtenerUnoTable(carrito.get()) : "Carrito no encontrado";
                    break;

                case "COMPRAS":
                    Optional<Compra> compra = services.getCompraService().findById(id);
                    respuesta = compra.isPresent() ? CompraMapper.obtenerUnoTable(compra.get()) : "Compra no encontrada";
                    break;

                case "PROVEEDORES":
                    Optional<Proveedor> proveedor = services.getProveedorService().findById(id);
                    respuesta = proveedor.isPresent() ? ProveedorMapper.obtenerUnoTable(proveedor.get()) : "Proveedor no encontrado";
                    break;

                case "CLIENTES":
                    Optional<Cliente> cliente = services.getClienteService().findById(id);
                    respuesta = cliente.isPresent() ? ClienteMapper.obtenerUnoTable(cliente.get()) : "Cliente no encontrado";
                    break;

                case "VENTAS":
                    Optional<Venta> venta = services.getVentaService().findById(id);
                    respuesta = venta.isPresent() ? VentaMapper.obtenerUnoTable(venta.get()) : "Venta no encontrada";
                    break;

                case "CREDITOS":
                    Optional<Credito> credito = services.getCreditoService().findById(id);
                    respuesta = credito.isPresent() ? CreditoMapper.obtenerUnoTable(credito.get()) : "Crédito no encontrado";
                    break;

                default:
                    respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al obtener " + entidad + " con ID " + id + ": " + e.getMessage();
        }
    }

}
