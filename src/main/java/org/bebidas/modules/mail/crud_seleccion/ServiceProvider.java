package org.bebidas.modules.mail.crud_seleccion;

import org.bebidas.modules.carrito.repositories.CarritoDAOImpl;
import org.bebidas.modules.carrito.repositories.interfaces.CarritoDAO;
import org.bebidas.modules.carrito.services.CarritoServiceImpl;
import org.bebidas.modules.carrito.services.ItemCarritoServiceImpl;
import org.bebidas.modules.carrito.services.interfaces.CarritoService;
import org.bebidas.modules.carrito.services.interfaces.ItemCarritoService;
import org.bebidas.modules.categorias.repositories.interfaces.CategoriaDAO;
import org.bebidas.modules.categorias.services.CategoriaServiceImpl;
import org.bebidas.modules.categorias.services.interfaces.CategoriaService;
import org.bebidas.modules.clientes.repositories.interfaces.ClienteDAO;
import org.bebidas.modules.clientes.services.ClienteServiceImpl;
import org.bebidas.modules.clientes.services.interfaces.ClienteService;
import org.bebidas.modules.compras.repositories.interfaces.CompraDAO;
import org.bebidas.modules.compras.services.CompraServiceImpl;
import org.bebidas.modules.compras.services.DetalleCompraServiceImpl;
import org.bebidas.modules.compras.services.interfaces.CompraService;
import org.bebidas.modules.creditos.PagoCuotaServiceImpl;
import org.bebidas.modules.creditos.services.CreditoServiceImpl;
import org.bebidas.modules.creditos.services.interfaces.CreditoService;
import org.bebidas.modules.dao.impl.*;
import org.bebidas.modules.dao.interfaces.*;
import org.bebidas.modules.inventario.InventarioServiceImpl;
import org.bebidas.modules.inventario.services.ProductoServiceImpl;
import org.bebidas.modules.proveedor.services.ProveedorServiceImpl;
import org.bebidas.modules.service.PagoCuotaService;
import org.bebidas.modules.service.interfaces.*;
import org.bebidas.modules.usuarios.services.RolServiceImpl;
import org.bebidas.modules.usuarios.services.UsuarioServiceImpl;
import org.bebidas.modules.usuarios.services.interfaces.UsuarioService;
import org.bebidas.modules.vendedores.services.VendedorServiceImpl;
import org.bebidas.modules.ventas.DetalleVentaServiceImpl;
import org.bebidas.modules.ventas.services.PagoServiceImpl;
import org.bebidas.modules.ventas.services.VentaServiceImpl;

/**
 * Clase central que provee todos los servicios necesarios para las operaciones CRUD.
 * Evita la duplicación de código en las clases Listar, Actualizar, Eliminar, SelectById.
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
    private final CompraService compraService;
    private final DetalleCompraService detalleCompraService;
    private final DetalleVentaService detalleVentaService;
    private final ProductoService productoService;
    private final ItemCarritoService itemCarritoService;
    private final ProveedorService proveedorService;
    private final RolService rolService;
    private final UsuarioService usuarioService;
    private final VendedorService vendedorService;
    private final CreditoService creditoService;
    private final PagoCuotaService pagoCuotaService;
    private final PagoService pagoService;
    private final VentaService ventaService;

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
        this.clienteService = new ClienteServiceImpl(clienteDAO, usuarioDAO);
        this.carritoService = new CarritoServiceImpl(carritoDAO, itemCarritoDAO, clienteService);
        this.categoriaService = new CategoriaServiceImpl(categoriaDAO);
        this.inventarioService = new InventarioServiceImpl(inventarioDAO, productoDAO);
        this.compraService = new CompraServiceImpl(compraDAO);
        this.detalleCompraService = new DetalleCompraServiceImpl(detalleCompraDAO, inventarioService);
        this.detalleVentaService = new DetalleVentaServiceImpl(detalleVentaDAO, inventarioService);
        this.productoService = new ProductoServiceImpl(productoDAO);
        this.itemCarritoService = new ItemCarritoServiceImpl(itemCarritoDAO, carritoService, productoService,
                (InventarioServiceImpl) inventarioService);
        this.proveedorService = new ProveedorServiceImpl(proveedorDAO);
        this.rolService = new RolServiceImpl(rolDAO);
        this.usuarioService = new UsuarioServiceImpl(usuarioDAO);
        this.vendedorService = new VendedorServiceImpl(vendedorDAO);
        this.creditoService = (CreditoService)new CreditoServiceImpl();
        this.pagoCuotaService = new PagoCuotaServiceImpl();
        this.pagoService = new PagoServiceImpl(pagoDAO);
        this.ventaService = new VentaServiceImpl(ventaDAO, carritoService, detalleVentaService, pagoService,
                (CreditoServiceImpl) creditoService, pagoCuotaService);
        ((PagoServiceImpl) pagoService).setVentaService(ventaService);
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

    public CompraService getCompraService() {
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

    public RolService getRolService() {
        return rolService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public VendedorService getVendedorService() {
        return vendedorService;
    }

    public CreditoService getCreditoService() {
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
