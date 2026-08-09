package org.bebidas.modules.mail.crud_seleccion;

import java.sql.SQLException;

import org.bebidas.core.util.MensajesError;
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
import org.bebidas.modules.ventas.mappers.DetalleVentaMapper;
import org.bebidas.modules.ventas.mappers.VentaMapper;

public class Listar {

    private final ServiceProvider services = ServiceProvider.getInstance();

    public String ejecutarConsultaListar(String entidad) throws SQLException {
        String respuesta = "";
        try {
            switch (entidad) {
                case "ROLES":
                    respuesta = "Roles \n" + RolMapper.obtenerTodosTable(services.getRolService().findAll());
                    break;

                case "USUARIOS":
                    respuesta = "Usuarios \n"
                            + UsuarioMapper.obtenerTodosTable(services.getUsuarioService().findAll());
                    break;
                case "VENDEDORES":
                    respuesta = "Vendedores \n"
                            + VendedorMapper.obtenerTodosTable(services.getVendedorService().findAll());
                    break;
                case "CATEGORIAS":
                    respuesta = "Categorias \n"
                            + CategoriaMapper.obtenerTodosTable(services.getCategoriaService().findAll());
                    break;
                case "PRODUCTOS":
                    respuesta = "Productos \n"
                            + ProductoMapper.obtenerTodosTable(services.getProductoService().findAll());
                    break;
                case "INVENTARIO":
                    respuesta = "Inventario \n"
                            + InventarioMapper.obtenerTodosTable(services.getInventarioService().findAll());
                    break;
                case "CARRITOS":
                    respuesta = "Carritos \n" + CarritoMapper.obtenerTodosTable(services.getCarritoService().findAll());
                    break;
                case "ITEMCARRITOS":
                    respuesta = "Item Carritos \n"
                            + CarritoMapper.obtenerTodosTable(services.getCarritoService().findAll());
                    break;
                case "COMPRAS":
                    System.out.println("LLEGUE A LISTAR COMPRAS" + services.getCompraService().findAll().toString());
                    respuesta = "Compras \n" + CompraMapper.obtenerTodosTable(services.getCompraService().findAll());
                    break;
                case "PROVEEDORES":
                    respuesta = "Proveeodres \n"
                            + ProveedorMapper.obtenerTodosTable(services.getProveedorService().findAll());
                    break;
                case "CLIENTES":
                    respuesta = "Clientes \n" + ClienteMapper.obtenerTodosTable(services.getClienteService().findAll());
                    break;
                case "VENTAS":
                    respuesta = "Ventas \n" + VentaMapper.obtenerTodosTable(services.getVentaService().findAll());
                    break;
                case "DETALLEVENTAS":
                    respuesta = "Detalle de Ventas \n"
                            + DetalleVentaMapper.obtenerTodosTable(services.getDetalleVentaService().findAll());
                    break;
                case "CREDITOS":
                    respuesta = "Creditos \n" + CreditoMapper.obtenerTodosTable(services.getCreditoService().findAll());
                    break;
                case "PAGOS_CUOTAS":
                    respuesta = "Pagos \n" + CreditoMapper.obtenerTodosTable(services.getCreditoService().findAll());
                    break;

                default:
                    respuesta = "Entidad no encontrada 'Comando Incorrecto Favor Revisar Help'";
            }
            return respuesta;
        } catch (Exception e) {
            return MensajesError.paraCliente("listar " + entidad, e);
        }
    }

}
