package org.bebidas.modules.mail.crud_seleccion;

import java.sql.SQLException;

import org.bebidas.modules.carrito.mappers.CarritoMapper;
import org.bebidas.modules.categorias.mappers.CategoriaMapper;
import org.bebidas.modules.clientes.mappers.ClienteMapper;
import org.bebidas.modules.compras.mappers.CompraMapper;
import org.bebidas.modules.creditos.mappers.CreditoMapper;
import org.bebidas.modules.inventario.mappers.InventarioMapper;
import org.bebidas.modules.inventario.mappers.ProductoMapper;
import org.bebidas.modules.proveedor.mappers.ProveedorMapper;
import org.bebidas.modules.usuarios.mappers.RolMapper;
import org.bebidas.modules.usuarios.mappers.UsuarioMapper;
import org.bebidas.modules.vendedores.mappers.VendedorMapper;
import org.bebidas.modules.ventas.VentaMapper;

public class Listar {

    private final ServiceProvider services = ServiceProvider.getInstance();

    public String ejecutarConsultaListar(String entidad) throws SQLException {
        String respuesta = "";
        try {
            switch (entidad) {
                case "ROLES":
                    respuesta = RolMapper.obtenerTodosTable(services.getRolService().findAll());
                    break;

                case "USUARIOS":
                    respuesta = UsuarioMapper.obtenerTodosTable(services.getUsuarioService().findAll());
                    break;
                case "VENDEDORES":
                    respuesta = VendedorMapper.obtenerTodosTable(services.getVendedorService().findAll());
                    break;
                case "CATEGORIAS":
                    respuesta = CategoriaMapper.obtenerTodosTable(services.getCategoriaService().findAll());
                    break;
                case "PRODUCTOS":
                    respuesta = ProductoMapper.obtenerTodosTable(services.getProductoService().findAll());
                    break;
                case "INVENTARIO":
                    respuesta = InventarioMapper.obtenerTodosTable(services.getInventarioService().findAll());
                    break;
                case "CARRITOS":
                    respuesta = CarritoMapper.obtenerTodosTable(services.getCarritoService().findAll());
                    break;
                case "COMPRAS":
                    respuesta = CompraMapper.obtenerTodosTable(services.getCompraService().findAll());
                    break;
                case "PROVEEDORES":
                    respuesta = ProveedorMapper.obtenerTodosTable(services.getProveedorService().findAll());
                    break;
                case "CLIENTES":
                    respuesta = ClienteMapper.obtenerTodosTable(services.getClienteService().findAll());
                    break;
                case "VENTAS":
                    respuesta = VentaMapper.obtenerTodosTable(services.getVentaService().findAll());
                    break;
                case "CREDITOS":
                    respuesta = CreditoMapper.obtenerTodosTable(services.getCreditoService().findAll());
                    break;
                case "PAGOS_CUOTAS":
                    respuesta = CreditoMapper.obtenerTodosTable(services.getCreditoService().findAll());
                    break;

                default:
                    respuesta = "Entidad no encontrada";
            }
            return respuesta;
        } catch (Exception e) {
            return "Error al obtener listado de " + entidad + ": " + e.getMessage();
        }
    }

}
