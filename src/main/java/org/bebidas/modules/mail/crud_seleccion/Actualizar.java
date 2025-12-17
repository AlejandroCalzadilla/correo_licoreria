package org.bebidas.modules.mail.crud_seleccion;

import java.sql.SQLException;

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

public class Actualizar {
    
    private final ServiceProvider services = ServiceProvider.getInstance();

    public <T> String ejecutarActualizar(String entidad, T objeto) throws SQLException {
        String respuesta = "";
        try {
            switch (entidad) {
                case "ROLES":
                    services.getRolService().save((Rol) objeto);
                    respuesta = "Rol actualizado correctamente";
                    break;

                case "USUARIOS":
                    services.getUsuarioService().save((Usuario) objeto);
                    respuesta = "Usuario actualizado correctamente";
                    break;

                case "VENDEDORES":
                    services.getVendedorService().save((Vendedor) objeto);
                    respuesta = "Vendedor actualizado correctamente";
                    break;

                case "CATEGORIAS":
                    services.getCategoriaService().save((Categoria) objeto);
                    respuesta = "Categoría actualizada correctamente";
                    break;

                case "PRODUCTOS":
                    services.getProductoService().save((Producto) objeto);
                    respuesta = "Producto actualizado correctamente";
                    break;

                case "INVENTARIO":
                    services.getInventarioService().save((Inventario) objeto);
                    respuesta = "Inventario actualizado correctamente";
                    break;

                case "CARRITOS":
                    services.getCarritoService().save((Carrito) objeto);
                    respuesta = "Carrito actualizado correctamente";
                    break;

                case "COMPRAS":
                    services.getCompraService().save((Compra) objeto);
                    respuesta = "Compra actualizada correctamente";
                    break;

                case "PROVEEDORES":
                    services.getProveedorService().save((Proveedor) objeto);
                    respuesta = "Proveedor actualizado correctamente";
                    break;

                case "CLIENTES":
                    services.getClienteService().save((Cliente) objeto);
                    respuesta = "Cliente actualizado correctamente";
                    break;

                case "VENTAS":
                    services.getVentaService().save((Venta) objeto);
                    respuesta = "Venta actualizada correctamente";
                    break;

                case "CREDITOS":
                    services.getCreditoService().save((Credito) objeto);
                    respuesta = "Crédito actualizado correctamente";
                    break;

                default:
                    respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al actualizar " + entidad + ": " + e.getMessage();
        }
    }

}
