package org.bebidas.modules.mail.crud_seleccion;

import java.sql.SQLException;

import org.bebidas.core.util.MensajesError;

public class Eliminar {
    
    private final ServiceProvider services = ServiceProvider.getInstance();

    public String ejecutarEliminar(String entidad, Long id) throws SQLException {
        String respuesta = "";
        try {
            switch (entidad) {
                case "ROLES":
                    services.getRolService().delete(id);
                    respuesta = "Rol eliminado correctamente";
                    break;

                case "USUARIOS":
                    services.getUsuarioService().delete(id);
                    respuesta = "Usuario eliminado correctamente";
                    break;

                case "VENDEDORES":
                    services.getVendedorService().delete(id);
                    respuesta = "Vendedor eliminado correctamente";
                    break;

                case "CATEGORIAS":
                    services.getCategoriaService().delete(id);
                    respuesta = "Categoría eliminada correctamente";
                    break;

                case "PRODUCTOS":
                    services.getProductoService().delete(id);
                    respuesta = "Producto eliminado correctamente";
                    break;

                case "INVENTARIO":
                    services.getInventarioService().delete(id);
                    respuesta = "Inventario eliminado correctamente";
                    break;

                case "CARRITOS":
                    services.getCarritoService().delete(id);
                    respuesta = "Carrito eliminado correctamente";
                    break;

                case "COMPRAS":
                    services.getCompraService().delete(id);
                    respuesta = "Compra eliminada correctamente";
                    break;

                case "PROVEEDORES":
                    services.getProveedorService().delete(id);
                    respuesta = "Proveedor eliminado correctamente";
                    break;

                case "CLIENTES":
                    services.getClienteService().delete(id);
                    respuesta = "Cliente eliminado correctamente";
                    break;

                case "VENTAS":
                    services.getVentaService().delete(id);
                    respuesta = "Venta eliminada correctamente";
                    break;

                case "CREDITOS":
                    services.getCreditoService().delete(id);
                    respuesta = "Crédito eliminado correctamente";
                    break;

                default:
                    respuesta = "Entidad no encontrada 'Comando Incorrecto Favor Revisar Help' ";
            }
            return respuesta;
        } catch (Exception e) {
            return MensajesError.paraCliente("eliminar " + entidad + " con ID " + id, e);
        }
    }

}
