package org.bebidas.modules.mail.crud_seleccion;

import org.bebidas.modules.carrito.repositories.CarritoDAOImpl;
import org.bebidas.modules.carrito.repositories.ItemCarritoDAOImpl;
import org.bebidas.modules.carrito.repositories.interfaces.CarritoDAO;
import org.bebidas.modules.carrito.repositories.interfaces.ItemCarritoDAO;
import org.bebidas.modules.carrito.services.CarritoServiceImpl;
import org.bebidas.modules.carrito.services.ItemCarritoServiceImpl;
import org.bebidas.modules.carrito.services.interfaces.CarritoService;
import org.bebidas.modules.carrito.services.interfaces.ItemCarritoService;
import org.bebidas.modules.categorias.repositories.CategoriaDAOImpl;
import org.bebidas.modules.categorias.repositories.interfaces.CategoriaDAO;
import org.bebidas.modules.categorias.services.CategoriaServiceImpl;
import org.bebidas.modules.categorias.services.interfaces.CategoriaService;
import org.bebidas.modules.clientes.repositories.ClienteDAOImpl;
import org.bebidas.modules.clientes.repositories.interfaces.ClienteDAO;
import org.bebidas.modules.clientes.services.ClienteServiceImpl;
import org.bebidas.modules.clientes.services.interfaces.ClienteService;
import org.bebidas.modules.compras.repositories.CompraDAOImpl;
import org.bebidas.modules.compras.repositories.DetalleCompraDAOImpl;
import org.bebidas.modules.compras.repositories.interfaces.CompraDAO;
import org.bebidas.modules.compras.repositories.interfaces.DetalleCompraDAO;
import org.bebidas.modules.compras.services.CompraServiceImpl;
import org.bebidas.modules.compras.services.DetalleCompraServiceImpl;
import org.bebidas.modules.compras.services.interfaces.ICompraService;
import org.bebidas.modules.creditos.PagoCuotaServiceImpl;
import org.bebidas.modules.creditos.services.CreditoServiceImpl;
import org.bebidas.modules.inventario.InventarioServiceImpl;
import org.bebidas.modules.inventario.repositories.InventarioDAOImpl;
import org.bebidas.modules.inventario.repositories.ProductoDAOImpl;
import org.bebidas.modules.inventario.repositories.interfaces.InventarioDAO;
import org.bebidas.modules.inventario.repositories.interfaces.ProductoDAO;
import org.bebidas.modules.inventario.services.ProductoServiceImpl;
import org.bebidas.modules.pagos.PagoCuotaService;
import org.bebidas.modules.pagos.repostiories.PagoDAO;
import org.bebidas.modules.pagos.repostiories.PagoDAOImpl;
import org.bebidas.modules.proveedor.repositories.ProveedorDAO;
import org.bebidas.modules.proveedor.repositories.ProveedorDAOImpl;
import org.bebidas.modules.proveedor.services.ProveedorServiceImpl;
import org.bebidas.modules.service.interfaces.DetalleCompraService;
import org.bebidas.modules.service.interfaces.DetalleVentaService;
import org.bebidas.modules.service.interfaces.InventarioService;
import org.bebidas.modules.service.interfaces.PagoService;
import org.bebidas.modules.service.interfaces.ProductoService;
import org.bebidas.modules.service.interfaces.ProveedorService;
import org.bebidas.modules.service.interfaces.VendedorService;
import org.bebidas.modules.service.interfaces.VentaService;
import org.bebidas.modules.usuarios.repositories.RolDAO;
import org.bebidas.modules.usuarios.repositories.RolDAOImpl;
import org.bebidas.modules.usuarios.repositories.UsuarioDAO;
import org.bebidas.modules.usuarios.repositories.UsuarioDAOImpl;
import org.bebidas.modules.usuarios.services.RolServiceImpl;
import org.bebidas.modules.usuarios.services.UsuarioServiceImpl;
import org.bebidas.modules.usuarios.services.interfaces.UsuarioService;
import org.bebidas.modules.vendedores.repositories.VendedorDAO;
import org.bebidas.modules.vendedores.repositories.VendedorDAOImpl;
import org.bebidas.modules.vendedores.services.VendedorServiceImpl;
import org.bebidas.modules.ventas.DetalleVentaServiceImpl;
import org.bebidas.modules.ventas.repositories.DetalleVentaDAO;
import org.bebidas.modules.ventas.repositories.DetalleVentaDAOImpl;
import org.bebidas.modules.ventas.repositories.VentaDAO;
import org.bebidas.modules.ventas.repositories.VentaDAOImpl;
import org.bebidas.modules.ventas.services.PagoServiceImpl;
import org.bebidas.modules.ventas.services.VentaServiceImpl;

/**
 * Clase central que provee todos los servicios necesarios para las operaciones
 * CRUD.
 * Evita la duplicación de código en las clases Listar, Actualizar, Eliminar,
 * SelectById.
 */
public class ServiceProvider {

    // DAOs
    private final CarritoDAO carritoDAO;
    private final CategoriaDAO categoriaDAO;
    private final ClienteDAO clienteDAO;
    private final CompraDAO compraDAO;
    private final DetalleCompraDAO detalleCompraDAO;
    private final DetalleVentaDAO detalleVentaDAO;
    private final ItemCarritoDAO itemCarritoDAO;
    private final PagoDAO pagoDAO;
    private final ProductoDAO productoDAO;
    private final ProveedorDAO proveedorDAO;
    private final RolDAO rolDAO;
    private final UsuarioDAO usuarioDAO;
    private final VendedorDAO vendedorDAO;
    private final VentaDAO ventaDAO;
    private final InventarioDAO inventarioDAO;

    // Servicios
    private final ClienteService clienteService;
    private final CarritoService carritoService;
    private final CategoriaService categoriaService;
    private final InventarioService inventarioService;
    private final ICompraService compraService;
    private final DetalleCompraService detalleCompraService;
    private final DetalleVentaService detalleVentaService;
    private final ProductoService productoService;
    private final ItemCarritoService itemCarritoService;
    private final ProveedorService proveedorService;
    private final RolServiceImpl rolService;
    private final UsuarioService usuarioService;
    private final VendedorService vendedorService;
    private final PagoCuotaService pagoCuotaService;
    private final PagoService pagoService;
    private final VentaService ventaService;
    private final CreditoServiceImpl creditoService;

    // Instancia única (Singleton)
    private static ServiceProvider instance;

    private ServiceProvider() {
        // Inicializar DAOs
        this.carritoDAO = new CarritoDAOImpl();
        this.categoriaDAO = new CategoriaDAOImpl();
        this.clienteDAO = new ClienteDAOImpl();
        this.compraDAO = new CompraDAOImpl();
        this.detalleCompraDAO = new DetalleCompraDAOImpl();
        this.detalleVentaDAO = new DetalleVentaDAOImpl();
        this.itemCarritoDAO = new ItemCarritoDAOImpl();
        this.pagoDAO = new PagoDAOImpl();
        this.productoDAO = new ProductoDAOImpl();
        this.proveedorDAO = new ProveedorDAOImpl();
        this.rolDAO = new RolDAOImpl();
        this.usuarioDAO = new UsuarioDAOImpl();
        this.vendedorDAO = new VendedorDAOImpl();
        this.ventaDAO = new VentaDAOImpl();
        this.inventarioDAO = new InventarioDAOImpl();

        // Inicializar Servicios
        this.clienteService = new ClienteServiceImpl(clienteDAO);
        this.rolService = new RolServiceImpl(rolDAO);
        this.usuarioService = new UsuarioServiceImpl(usuarioDAO);
        this.carritoService = new CarritoServiceImpl(carritoDAO, itemCarritoDAO, clienteService, usuarioService);
        this.categoriaService = new CategoriaServiceImpl(categoriaDAO);
        this.inventarioService = new InventarioServiceImpl(inventarioDAO, productoDAO);
        this.compraService = new CompraServiceImpl(compraDAO);
        this.detalleCompraService = new DetalleCompraServiceImpl(detalleCompraDAO, inventarioService);
        this.detalleVentaService = new DetalleVentaServiceImpl(detalleVentaDAO, inventarioService);
        this.productoService = new ProductoServiceImpl(productoDAO, categoriaService);
        this.itemCarritoService = new ItemCarritoServiceImpl(itemCarritoDAO, carritoService, productoService,
                (InventarioServiceImpl) inventarioService);
        this.proveedorService = new ProveedorServiceImpl(proveedorDAO);
        this.vendedorService = new VendedorServiceImpl(vendedorDAO);
        this.creditoService = new CreditoServiceImpl();
        this.pagoCuotaService = new PagoCuotaServiceImpl();
        this.pagoService = new PagoServiceImpl(pagoDAO);
        this.ventaService = new VentaServiceImpl(ventaDAO, carritoService, detalleVentaService, pagoService,
                creditoService, pagoCuotaService, itemCarritoService, clienteService);
        ((PagoServiceImpl) pagoService).setVentaService(ventaService);
        ((PagoServiceImpl) pagoService).setCreditoService(creditoService);
        ((DetalleVentaServiceImpl) detalleVentaService).setVentaService(ventaService);
        ((DetalleVentaServiceImpl) detalleVentaService).setPagoService(pagoService);
        ((DetalleVentaServiceImpl) detalleVentaService).setCreditoService(creditoService);
    }

    public static ServiceProvider getInstance() {
        if (instance == null) {
            instance = new ServiceProvider();
        }
        return instance;
    }

    // Getters para los servicios
    public ClienteService getClienteService() {
        return clienteService;
    }

    public CarritoService getCarritoService() {
        return carritoService;
    }

    public CategoriaService getCategoriaService() {
        return categoriaService;
    }

    public InventarioService getInventarioService() {
        return inventarioService;
    }

    public ICompraService getCompraService() {
        return compraService;
    }

    public DetalleCompraService getDetalleCompraService() {
        return detalleCompraService;
    }

    public DetalleVentaService getDetalleVentaService() {
        return detalleVentaService;
    }

    public ProductoService getProductoService() {
        return productoService;
    }

    public ItemCarritoService getItemCarritoService() {
        return itemCarritoService;
    }

    public ProveedorService getProveedorService() {
        return proveedorService;
    }

    public RolServiceImpl getRolService() {
        return rolService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public VendedorService getVendedorService() {
        return vendedorService;
    }

    public CreditoServiceImpl getCreditoService() {
        return creditoService;
    }

    public PagoCuotaService getPagoCuotaService() {
        return pagoCuotaService;
    }

    public PagoService getPagoService() {
        return pagoService;
    }

    public VentaService getVentaService() {
        return ventaService;
    }
}
