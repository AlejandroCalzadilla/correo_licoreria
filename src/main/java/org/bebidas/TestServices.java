package org.bebidas;

import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.ItemCarrito;
import org.bebidas.modules.carrito.mappers.CarritoMapper;
import org.bebidas.modules.carrito.services.CarritoServiceImpl;
import org.bebidas.modules.carrito.services.ItemCarritoServiceImpl;
import org.bebidas.modules.carrito.services.interfaces.CarritoService;
import org.bebidas.modules.carrito.services.interfaces.ItemCarritoService;
import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.categorias.mappers.CategoriaMapper;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.clientes.mappers.ClienteMapper;
import org.bebidas.modules.compras.Compra;
import org.bebidas.modules.compras.DetalleCompra;
import org.bebidas.modules.compras.mappers.CompraMapper;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.creditos.mappers.CreditoMapper;
import org.bebidas.modules.dao.impl.*;
import org.bebidas.modules.dao.interfaces.CarritoDAO;
import org.bebidas.modules.dao.interfaces.CategoriaDAO;
import org.bebidas.modules.dao.interfaces.ClienteDAO;
import org.bebidas.modules.dao.interfaces.CompraDAO;
import org.bebidas.modules.dao.interfaces.DetalleCompraDAO;
import org.bebidas.modules.dao.interfaces.DetalleVentaDAO;
import org.bebidas.modules.dao.interfaces.InventarioDAO;
import org.bebidas.modules.dao.interfaces.ItemCarritoDAO;
import org.bebidas.modules.dao.interfaces.PagoDAO;
import org.bebidas.modules.dao.interfaces.ProductoDAO;
import org.bebidas.modules.dao.interfaces.ProveedorDAO;
import org.bebidas.modules.dao.interfaces.RolDAO;
import org.bebidas.modules.dao.interfaces.UsuarioDAO;
import org.bebidas.modules.dao.interfaces.VendedorDAO;
import org.bebidas.modules.dao.interfaces.VentaDAO;
import org.bebidas.modules.inventario.Producto;
import org.bebidas.modules.model.*;
import org.bebidas.modules.service.CreditoService;
import org.bebidas.modules.service.PagoCuotaService;
import org.bebidas.modules.service.impl.*;
import org.bebidas.modules.service.interfaces.*;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.usuarios.services.UsuarioServiceImpl;
import org.bebidas.modules.usuarios.services.interfaces.UsuarioService;
import org.bebidas.modules.ventas.DetalleVenta;
import org.bebidas.modules.ventas.Pago;
import org.bebidas.modules.ventas.Venta;
import org.bebidas.mapper.DetalleCompraMapper;
import org.bebidas.mapper.DetalleVentaMapper;
import org.bebidas.mapper.InventarioMapper;
import org.bebidas.mapper.ItemCarritoMapper;
import org.bebidas.mapper.PagoMapper;
import org.bebidas.mapper.ProductoMapper;
import org.bebidas.mapper.ProveedorMapper;
import org.bebidas.mapper.RolMapper;
import org.bebidas.mapper.UsuarioMapper;
import org.bebidas.mapper.VendedorMapper;
import org.bebidas.mapper.VentaMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestServices {

    public static void main(String[] args) {
        // Inicializar DAOs
        CarritoDAO carritoDAO = new CarritoDAOImpl();
        CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
        ClienteDAO clienteDAO = new ClienteDAOImpl();
        CompraDAO compraDAO = new CompraDAOImpl();
        DetalleCompraDAO detalleCompraDAO = new DetalleCompraDAOImpl();
        DetalleVentaDAO detalleVentaDAO = new DetalleVentaDAOImpl();
        ItemCarritoDAO itemCarritoDAO = new ItemCarritoDAOImpl();
        PagoDAO pagoDAO = new PagoDAOImpl();
        ProductoDAO productoDAO = new ProductoDAOImpl();
        ProveedorDAO proveedorDAO = new ProveedorDAOImpl();
        RolDAO rolDAO = new RolDAOImpl();
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
        VendedorDAO vendedorDAO = new VendedorDAOImpl();
        VentaDAO ventaDAO = new VentaDAOImpl();
        InventarioDAO inventarioDAO = new InventarioDAOImpl();  
        // Inicializar Servicios
        ClienteService clienteService = new ClienteServiceImpl(clienteDAO, usuarioDAO);
        CarritoService carritoService = new CarritoServiceImpl(carritoDAO, itemCarritoDAO, clienteService);
        CategoriaService categoriaService = new CategoriaServiceImpl(categoriaDAO);
        InventarioService inventarioService = new InventarioServiceImpl(inventarioDAO,productoDAO);
        CompraService compraService = new CompraServiceImpl(compraDAO);
        DetalleCompraService detalleCompraService = new DetalleCompraServiceImpl(detalleCompraDAO,inventarioService);
        DetalleVentaService detalleVentaService = new DetalleVentaServiceImpl(detalleVentaDAO, inventarioService);
        ProductoService productoService = new ProductoServiceImpl(productoDAO);
        ItemCarritoService itemCarritoService = new ItemCarritoServiceImpl(itemCarritoDAO, carritoService, productoService, (InventarioServiceImpl) inventarioService);
        ProveedorService proveedorService = new ProveedorServiceImpl(proveedorDAO);
        RolService rolService = new RolServiceImpl(rolDAO);
        UsuarioService usuarioService = new UsuarioServiceImpl(usuarioDAO);
        VendedorService vendedorService = new VendedorServiceImpl(vendedorDAO);
        CreditoService creditoService = new CreditoServiceImpl();
        PagoCuotaService pagoCuotaService = new PagoCuotaServiceImpl();
        PagoService pagoService = new PagoServiceImpl(pagoDAO);
        VentaService ventaService = new VentaServiceImpl(ventaDAO, carritoService, detalleVentaService, pagoService, creditoService, pagoCuotaService);
        ((PagoServiceImpl) pagoService).setVentaService(ventaService);

        // Nota: Para pruebas completas, necesitarías instanciar todos los servicios
        // restantes
        // y tener una base de datos MySQL corriendo con las tablas creadas.

        // Variables para compartir entre pruebas
        Usuario usuario1 = null;
        Categoria categoria1 = null;
        Cliente cliente1 = null;
        Carrito carrito1 = null;
        Compra compra1 = null;
        Venta venta1 = null;
        Producto producto1 = null;
        Proveedor proveedor1 = null;
        Rol rol1 = null;
        Vendedor vendedor1 = null;
        DetalleCompra detalleCompra1 = null;
        DetalleVenta detalleVenta1 = null;
        Pago pago1 = null;
        ItemCarrito itemCarrito1 = null;

        System.out.println("=== PRUEBA DE SERVICIOS ===\n");
 


        // 1 Prueba RolService - CRUD completo
      
        System.out.println("0 Probando RolService (CRUD):");
        try {
            // Crear dos roles
            Rol rolTemp1 = new Rol();
            rolTemp1.setNombre("propietario");
            rolTemp1.setDescripcion("Acceso completo admin");
            rolTemp1.setActivo(true);
            rol1 = rolService.save(rolTemp1);
            System.out.println("Rol 1 creado con ID: " + rol1.getId());

            Rol rol2 = new Rol();
            rol2.setNombre("Proveedor test");
            rol2.setDescripcion("Encargado de realizar compras");
            rol2.setActivo(true);
            rol2 = rolService.save(rol2);
            System.out.println("Rol 2 creado con ID: " + rol2.getId());
           
            Rol rol3 = new Rol();
            rol3.setNombre("Vendedor");
            rol3.setDescripcion("Registra las ventas");
            rol3.setActivo(true);
            rol3 = rolService.save(rol3);
            System.out.println("Rol 3 creado con ID: " + rol3.getId());

            Rol rol4 = new Rol();
            rol4.setNombre("cliente");
            rol4.setDescripcion("Cliente ecommerce");
            rol4.setActivo(true);   
            rol4 = rolService.save(rol4);
            System.out.println("Rol 4 creado con ID: " + rol4.getId());
           


            // Listar todos
            List<Rol> roles = rolService.findAll();
            System.out.println("Total roles: " + roles.size());

            // Editar rol1
            rol1.setNombre("Administrador E");
            rol1 = rolService.save(rol1);
            System.out.println("Rol 1 editado: " + rol1.getNombre());

            // Borrar rol2
            rolService.delete(rol2.getId());
            System.out.println("Rol 2 borrado");

            // Listar de nuevo
            roles = rolService.findAll();
            System.out.println("Roles restantes: " + roles.size());
            
            // Mostrar tabla de roles
            System.out.println("\nTabla de roles:");
            System.out.println(RolMapper.obtenerTodosTable(roles));

        } catch (Exception e) {
            System.out.println("Error en RolService: " + e.getMessage());
        }
        System.out.println();








        // 1. Prueba UsuarioService - CRUD completo
        System.out.println("1. Probando UsuarioService (CRUD):");
        try {
            // Crear dos usuarios
            Usuario usuarioTemp1 = new Usuario();
            usuarioTemp1.setNombre("Juan Pérez");
            usuarioTemp1.setCorreo("juan@example.com");
            usuarioTemp1.setClave("password123");
            usuarioTemp1.setEstado("activo");
            usuario1 = usuarioService.save(usuarioTemp1);
            System.out.println("Usuario 1 creado con ID: " + usuario1.getId());

            Usuario usuario2 = new Usuario();
            usuario2.setNombre("María García");
            usuario2.setCorreo("maria@example.com");
            usuario2.setClave("password456");
            usuario2.setEstado("activo");
            usuario2 = usuarioService.save(usuario2);
            System.out.println("Usuario 2 creado con ID: " + usuario2.getId());

            // Listar todos
            List<Usuario> usuarios = usuarioService.findAll();
            System.out.println("Total usuarios: " + usuarios.toString());

            // Editar usuario1
            usuario1.setNombre("Juan Pérez Editado");
            usuario1 = usuarioService.save(usuario1);
            System.out.println("Usuario 1 editado: " + usuario1.getNombre());

            // Borrar usuario2
            usuarioService.delete(usuario2.getId());
            System.out.println("Usuario 2 borrado");

            // Listar de nuevo
            usuarios = usuarioService.findAll();
            System.out.println("Usuarios restantes: " + usuarios.toString());
            
            // Mostrar tabla de usuarios
            System.out.println("\nTabla de usuarios:");
            System.out.println(UsuarioMapper.obtenerTodosTable(usuarios));

        } catch (Exception e) {
            System.out.println("Error en UsuarioService: " + e.getMessage());
        }
        System.out.println();







        // 2. Prueba CategoriaService - CRUD completo
        System.out.println("2. Probando CategoriaService (CRUD):");
        try {
            // Crear dos categorías
            Categoria categoriaTemp1 = new Categoria();
            categoriaTemp1.setNombre("Bebidas Alcohólicas");
            categoriaTemp1.setActivo(true);
            categoriaTemp1.setTipo("ALCOHOL");
            categoria1 = categoriaService.save(categoriaTemp1);
            System.out.println("Categoría 1 creada con ID: " + categoria1.getId());

            Categoria categoria2 = new Categoria();
            categoria2.setNombre("Bebidas No Alcohólicas");
            categoria2.setActivo(true);
            categoria2.setTipo("NO_ALCOHOL");
            categoria2 = categoriaService.save(categoria2);
            System.out.println("Categoría 2 creada con ID: " + categoria2.getId());

            // Listar todas
            List<Categoria> categorias = categoriaService.findAll();
            System.out.println("Total categorías: " + categorias.size());

            // Editar categoria1
            categoria1.setNombre("Bebidas Alcohólicas Editadas");
            categoria1 = categoriaService.save(categoria1);
            System.out.println("Categoría 1 editada: " + categoria1.getNombre());

            // Borrar categoria2
            categoriaService.delete(categoria2.getId());
            System.out.println("Categoría 2 borrada");

            // Listar de nuevo
            categorias = categoriaService.findAll();
            System.out.println("Categorías restantes: " + categorias.size());
            
            // Mostrar tabla de categorías
            System.out.println("\nTabla de categorías:");
            System.out.println(CategoriaMapper.obtenerTodosTable(categorias));

        } catch (Exception e) {
            System.out.println("Error en CategoriaService: " + e.getMessage());
        }
        System.out.println();







       






        // 3. Prueba ClienteService - CRUD completo
        System.out.println("3. Probando ClienteService (CRUD):");
        try {
            // Crear dos clientes (usando el usuario1 creado)
            Cliente clienteTemp1 = new Cliente();
            clienteTemp1.setCi("12345678");
            clienteTemp1.setNombre("Pedro López");
            clienteTemp1.setTelefono("0987654321");
            clienteTemp1.setDireccion("Calle Falsa 123");
            clienteTemp1.setEstado('A');
            clienteTemp1.setUsuario(usuario1); // Asumiendo usuario1 existe
            clienteTemp1.setCreditoAprobado(false);
            clienteTemp1.setLimiteCredito(0.0);
            clienteTemp1.setEstadoVerificacion("aprobado");
            cliente1 = clienteService.save(clienteTemp1);
            System.out.println("Cliente 1 creado con ID: " + cliente1.getId());

            Cliente cliente2 = new Cliente();
            cliente2.setCi("87654321");
            cliente2.setNombre("Ana Martínez");
            cliente2.setTelefono("0123456789");
            cliente2.setDireccion("Avenida Real 456");
            cliente2.setEstado('A');
            cliente2.setUsuario(usuario1);
            cliente2.setCreditoAprobado(false);
            cliente2.setLimiteCredito(0.0);
            cliente2.setEstadoVerificacion("aprobado");
            cliente2 = clienteService.save(cliente2);
            System.out.println("Cliente 2 creado con ID: " + cliente2.getId());

            // Listar todos
            List<Cliente> clientes = clienteService.findAll();
            System.out.println("Total clientes: " + clientes.size());

            // Editar cliente1
            cliente1.setNombre("Pedro López Editado");
            cliente1 = clienteService.save(cliente1);
            System.out.println("Cliente 1 editado: " + cliente1.getNombre());

            // Borrar cliente2
            clienteService.delete(cliente2.getId());
            System.out.println("Cliente 2 borrado");

            // Listar de nuevo
            clientes = clienteService.findAll();
            System.out.println("Clientes restantes: " + clientes.size());
            
            // Mostrar tabla de clientes
            System.out.println("\nTabla de clientes:");
            System.out.println(ClienteMapper.obtenerTodosTable(clientes));

        } catch (Exception e) {
            System.out.println("Error en ClienteService: " + e.getMessage());
        }
        System.out.println();








        




        // 6. Prueba ProductoService - CRUD básico
        System.out.println("6. Probando ProductoService (CRUD):");
        try {
            // Crear dos productos (usando categoria1)
            Producto productoTemp1 = new Producto();
            productoTemp1.setNombre("Cerveza Pilsen");
            productoTemp1.setPrecio(BigDecimal.valueOf(25.0));
            
            productoTemp1.setCategoria(categoria1);
            producto1 = productoService.save(productoTemp1);
            System.out.println("Producto 1 creado con ID: " + producto1.getId());

            Producto producto2 = new Producto();
            producto2.setNombre("Vino Tinto");
            producto2.setPrecio(BigDecimal.valueOf(50.0));
          
            producto2.setCategoria(categoria1);
            producto2 = productoService.save(producto2);
            System.out.println("Producto 2 creado con ID: " + producto2.getId());

            // Listar todos
            List<Producto> productos = productoService.findAll();
            System.out.println("Total productos: " + productos.size());

            // Editar producto1
            producto1.setNombre("Cerveza Pilsen Premium");
            producto1 = productoService.save(producto1);
            System.out.println("Producto 1 editado: " + producto1.getNombre());

            // Borrar producto2
            productoService.delete(producto2.getId());
            System.out.println("Producto 2 borrado");

            // Listar de nuevo
            productos = productoService.findAll();
            System.out.println("Productos restantes: " + productos.size());
            
            // Mostrar tabla de productos
            System.out.println("\nTabla de productos:");
            System.out.println(ProductoMapper.obtenerTodosTable(productos));

        } catch (Exception e) {
            System.out.println("Error en ProductoService: " + e.getMessage());
        }
        System.out.println();




        // 7. Prueba ProveedorService - CRUD básico
        System.out.println("7. Probando ProveedorService (CRUD):");
        try {
            // Crear dos proveedores (usando usuario1)
            Proveedor proveedorTemp1 = new Proveedor();
            proveedorTemp1.setNombre("Distribuidora ABC");
            proveedorTemp1.setTelefono("099999999");
            proveedorTemp1.setDireccion("Calle Proveedor 123");
           
            proveedorTemp1.setUsuario(usuario1);
            proveedor1 = proveedorService.save(proveedorTemp1);
            System.out.println("Proveedor 1 creado con ID: " + proveedor1.getId());

            Proveedor proveedor2 = new Proveedor();
            proveedor2.setNombre("Bebidas XYZ");
            proveedor2.setTelefono("088888888");
            proveedor2.setDireccion("Avenida Proveedor 456");
           
            proveedor2.setUsuario(usuario1);
            proveedor2 = proveedorService.save(proveedor2);
            System.out.println("Proveedor 2 creado con ID: " + proveedor2.getId());

            // Listar todos
            List<Proveedor> proveedores = proveedorService.findAll();
            System.out.println("Total proveedores: " + proveedores.size());

            // Editar proveedor1
            proveedor1.setNombre("Distribuidora ABC Editada");
            proveedor1 = proveedorService.save(proveedor1);
            System.out.println("Proveedor 1 editado: " + proveedor1.getNombre());

            // Borrar proveedor2
            proveedorService.delete(proveedor2.getId());
            System.out.println("Proveedor 2 borrado");

            // Listar de nuevo
            proveedores = proveedorService.findAll();
            System.out.println("Proveedores restantes: " + proveedores.size());
            
            // Mostrar tabla de proveedores
            System.out.println("\nTabla de proveedores:");
            System.out.println(ProveedorMapper.obtenerTodosTable(proveedores));

        } catch (Exception e) {
            System.out.println("Error en ProveedorService: " + e.getMessage());
        }
        System.out.println();




        
        // 8. Prueba VendedorService - CRUD básico
        System.out.println("8. Probando VendedorService (CRUD):");
        try {
            // Crear dos vendedores (usando usuario1)
            Vendedor vendedorTemp1 = new Vendedor();
            vendedorTemp1.setCi("11111111");
            vendedorTemp1.setNombre("Carlos Vendedor");
            vendedorTemp1.setUsuario(usuario1);
            vendedor1 = vendedorService.save(vendedorTemp1);
            System.out.println("Vendedor 1 creado con ID: " + vendedor1.getId());

            Vendedor vendedor2 = new Vendedor();
            vendedor2.setCi("22222222");
            vendedor2.setNombre("Laura Vendedora");
            vendedor2.setUsuario(usuario1);
            vendedor2 = vendedorService.save(vendedor2);
            System.out.println("Vendedor 2 creado con ID: " + vendedor2.getId());

            // Listar todos
            List<Vendedor> vendedores = vendedorService.findAll();
            System.out.println("Total vendedores: " + vendedores.size());

            // Editar vendedor1
            vendedor1.setNombre("Carlos Vendedor Editado");
            vendedor1 = vendedorService.save(vendedor1);
            System.out.println("Vendedor 1 editado: " + vendedor1.getNombre());

            // Borrar vendedor2
            vendedorService.delete(vendedor2.getId());
            System.out.println("Vendedor 2 borrado");

            // Listar de nuevo
            vendedores = vendedorService.findAll();
            System.out.println("Vendedores restantes: " + vendedores.size());
            
            // Mostrar tabla de vendedores
            System.out.println("\nTabla de vendedores:");
            System.out.println(VendedorMapper.obtenerTodosTable(vendedores));

        } catch (Exception e) {
            System.out.println("Error en VendedorService: " + e.getMessage());
        }
        System.out.println();




        // 9. Prueba DetalleCompraService - Insert, Update, Delete
        System.out.println("9. Probando DetalleCompraService (Insert, Update, Delete):");
        try {
            // Crear una compra real usando CompraService
            Compra compraTemp = new Compra();
            compraTemp.setProveedor(proveedor1);
            compraTemp.setDescripcion("nota compra ");
            compraTemp.setFecha(LocalDate.now());
            compraTemp.setEstado("PENDIENTE");
           // compraTemp.setMontoTotal(BigDecimal.valueOf(0.0));
           
           compra1 = compraService.crearCompra(compraTemp);
            System.out.println("Compra creada con ID: " + compra1.getId());

            // Insertar dos detalles de compra
            DetalleCompra detalleTemp1 = new DetalleCompra();
            detalleTemp1.setCompra(compra1);
            detalleTemp1.setProducto(producto1); // Usando producto1 creado anteriormente
            detalleTemp1.setCantidad(5);
            detalleTemp1.setPrecioUnitario(BigDecimal.valueOf(25.0));
            detalleTemp1.setSubtotal(BigDecimal.valueOf(125.0));
            detalleCompra1 = detalleCompraService.insertar(detalleTemp1);
            System.out.println("DetalleCompra 1 insertado con ID: " + detalleCompra1.getId());

            DetalleCompra detalle2 = new DetalleCompra();
            detalle2.setCompra(compra1);
            detalle2.setProducto(producto1);
            detalle2.setCantidad(10);
            detalle2.setPrecioUnitario(BigDecimal.valueOf(20.0));
            detalle2.setSubtotal(BigDecimal.valueOf(200.0));
            detalle2 = detalleCompraService.insertar(detalle2);
            System.out.println("DetalleCompra 2 insertado con ID: " + detalle2.getId());

            // Listar todos
            List<DetalleCompra> detalles = detalleCompraService.findAll();
            System.out.println("Total detalles de compra: " + detalles.size());



            // Actualizar detalleCompra1
            detalleCompra1.setCantidad(8);
            detalleCompra1.setSubtotal(BigDecimal.valueOf(200.0));
            detalleCompra1 = detalleCompraService.actualizar(detalleCompra1);
            System.out.println("DetalleCompra 1 actualizado: cantidad = " + detalleCompra1.getCantidad());

            // Eliminar detalle2
            detalleCompraService.eliminar(detalle2.getId());
            System.out.println("DetalleCompra 2 eliminado");

            // Listar de nuevo
            detalles = detalleCompraService.findAll();
            System.out.println("Detalles de compra restantes: " + detalles.size());
            
            // Mostrar tabla de detalles de compra
            System.out.println("\nTabla de detalles de compra:");
            System.out.println(DetalleCompraMapper.obtenerTodosTable(detalles));

        } catch (Exception e) {
            System.out.println("Error en DetalleCompraService: " + e.getMessage());
        }
        System.out.println();



        // 4. Prueba CarritoService - CRUD básico (crear, listar, editar, borrar)
        System.out.println("4. Probando CarritoService (CRUD):");
        try {
            // Crear dos carritos (usando cliente1)
            Carrito carritoTemp1 = new Carrito();
            carritoTemp1.setUsuario(cliente1.getUsuario());
            carritoTemp1.setSessionId("session123");
            carritoTemp1.setCreatedAt(LocalDateTime.now());
            carritoTemp1.setUpdatedAt(LocalDateTime.now());
            carrito1 = carritoService.save(carritoTemp1);
            System.out.println("Carrito 1 creado con ID: " + carrito1.getId());

            // Agregar item al carrito1
            ItemCarrito itemTemp1 = new ItemCarrito();
            itemTemp1.setCarrito(carrito1);
            itemTemp1.setProducto(producto1);
            itemTemp1.setCantidad(3);
            itemTemp1.setPrecio(BigDecimal.valueOf(25.0));
            itemCarrito1 = itemCarritoService.agregarItem(itemTemp1);
            System.out.println("Item 1 agregado al carrito1 con ID: " + itemCarrito1.getId());

            // Listar items del carrito
            List<ItemCarrito> items = itemCarritoService.buscarPorCarrito(carrito1.getId());
            System.out.println("Items en carrito1: " + items.size());

            // Actualizar cantidad
            itemCarritoService.actualizarCantidad(itemCarrito1.getId(), 5);
            System.out.println("Cantidad del item1 actualizada");

            // Eliminar item
            itemCarritoService.eliminarItem(itemCarrito1.getId());
            System.out.println("Item1 eliminado del carrito");

            Carrito carrito2 = new Carrito();
            carrito2.setUsuario(cliente1.getUsuario());
            carrito2.setSessionId("session456");
            carrito2.setCreatedAt(LocalDateTime.now());
            carrito2.setUpdatedAt(LocalDateTime.now());
            carrito2 = carritoService.save(carrito2);
            System.out.println("Carrito 2 creado con ID: " + carrito2.getId());

            // Listar todos
            List<Carrito> carritos = carritoService.findAll();
            System.out.println("Total carritos: " + carritos.size());

            // Editar carrito1
            carrito1.setSessionId("session123_editado");
            carrito1.setUpdatedAt(LocalDateTime.now());
            carrito1 = carritoService.save(carrito1);
            System.out.println("Carrito 1 editado: " + carrito1.getSessionId());

            // Borrar carrito2
            carritoService.delete(carrito2.getId());
            System.out.println("Carrito 2 borrado");

            // Listar de nuevo
            carritos = carritoService.findAll();
            System.out.println("Carritos restantes: " + carritos.size());
            
            // Mostrar tabla de carritos
            System.out.println("\nTabla de carritos:");
            System.out.println(CarritoMapper.obtenerTodosTable(carritos));

        } catch (Exception e) {
            System.out.println("Error en CarritoService: " + e.getMessage());
        }
        System.out.println();








        // 5. Prueba VentaService - CRUD básico
        System.out.println("5. Probando VentaService (CRUD):");
        try {
            // Crear dos ventas (usando cliente1)
            Venta ventaTemp1 = new Venta();
            ventaTemp1.setCliente(cliente1);
            ventaTemp1.setUsuario(cliente1.getUsuario());
            ventaTemp1.setFecha(LocalDate.now());
            ventaTemp1.setEstado("PENDIENTE");
            ventaTemp1.setMontoTotal(BigDecimal.valueOf(100.0));
            ventaTemp1.setSaldo(BigDecimal.valueOf(100.0));
            venta1 = ventaService.save(ventaTemp1);
            System.out.println("Venta 1 creada con ID: " + venta1.getId());

            // Crear detalle de venta para venta1
            DetalleVenta detalleVentaTemp1 = new DetalleVenta();
            detalleVentaTemp1.setVenta(venta1);
            detalleVentaTemp1.setProducto(producto1);
            detalleVentaTemp1.setCantidad(2);
            detalleVentaTemp1.setPrecioUnitario(BigDecimal.valueOf(25.0));
            detalleVentaTemp1.setSubtotal(BigDecimal.valueOf(50.0));
            detalleVenta1 = detalleVentaService.insertar(detalleVentaTemp1);
            System.out.println("DetalleVenta 1 insertado para venta1 con ID: " + detalleVenta1.getId());

            // Crear pago completo para venta1
            Pago pagoTemp1 = new Pago();
            pagoTemp1.setVenta(venta1);
            pagoTemp1.setMonto(venta1.getSaldo()); // Pago completo igual al saldo
            pagoTemp1.setTipoPago("EFECTIVO");
            pago1 = pagoService.registrarPago(pagoTemp1);
            System.out.println("Pago 1 registrado para venta1 con ID: " + pago1.getId());

            // Verificar que la venta esté completada
            venta1 = ventaService.findById(venta1.getId()).orElse(null);
            System.out.println("Estado de venta1 después del pago: " + venta1.getEstado());

            Venta venta2 = new Venta();
            venta2.setCliente(cliente1);
            venta2.setUsuario(cliente1.getUsuario());
            venta2.setFecha(LocalDate.now());
            venta2.setEstado("PENDIENTE");
            venta2.setMontoTotal(BigDecimal.valueOf(200.0));
            venta2.setSaldo(BigDecimal.valueOf(200.0));
            venta2 = ventaService.save(venta2);
            System.out.println("Venta 2 creada con ID: " + venta2.getId());

            // Listar todas
            List<Venta> ventas = ventaService.findAll();
            System.out.println("Total ventas: " + ventas.size());

            // Editar venta1
            venta1.setEstado("COMPLETADA");
            venta1.setSaldo(BigDecimal.ZERO);
            venta1 = ventaService.save(venta1);
            System.out.println("Venta 1 editada: " + venta1.getEstado());

            // Borrar venta2
            ventaService.delete(venta2.getId());
            System.out.println("Venta 2 borrada");

            // Listar de nuevo
            ventas = ventaService.findAll();
            System.out.println("Ventas restantes: " + ventas.size());

            // Pruebas adicionales con métodos específicos
            // Completar venta1 usando completarVenta
            venta1 = ventaService.completarVenta(venta1.getId());
            System.out.println("Venta 1 completada: " + venta1.getEstado());

            // Crear otra venta usando crearVenta
            Venta venta3 = new Venta();
            venta3.setCliente(cliente1);
            venta3.setUsuario(cliente1.getUsuario());
            venta3.setMontoTotal(BigDecimal.valueOf(150.0));
            venta3.setSaldo(BigDecimal.valueOf(150.0));
            venta3 = ventaService.crearVenta(venta3);
            System.out.println("Venta 3 creada con crearVenta: " + venta3.getId());

            // Anular venta3
            ventaService.anularVenta(venta3.getId(), "Prueba de anulación");
            System.out.println("Venta 3 anulada");

            // Calcular total de ventas por cliente
            BigDecimal totalVentasCliente = ventaService.calcularTotalVentasPorCliente(cliente1.getId());
            System.out.println("Total ventas completadas del cliente: " + totalVentasCliente);

            // Obtener ventas pendientes
            List<Venta> ventasPendientes = ventaService.obtenerVentasPendientes();
            System.out.println("Ventas pendientes: " + ventasPendientes.size());

            // Obtener ventas completadas
            List<Venta> ventasCompletadas = ventaService.obtenerVentasCompletadas();
            System.out.println("Ventas completadas: " + ventasCompletadas.size());

            // Obtener ventas anuladas
            List<Venta> ventasAnuladas = ventaService.obtenerVentasAnuladas();
            System.out.println("Ventas anuladas: " + ventasAnuladas.size());
            
            // Mostrar tabla de ventas
            System.out.println("\nTabla de ventas:");
            System.out.println(VentaMapper.obtenerTodosTable(ventas));
            
            // Mostrar tabla de detalles de venta
            if (detalleVenta1 != null) {
                System.out.println("\nTabla de detalles de venta:");
                System.out.println(DetalleVentaMapper.obtenerUnoTable(detalleVenta1));
            }

        } catch (Exception e) {
            System.out.println("Error en VentaService: " + e.getMessage());
        }
        System.out.println();

        System.out.println("=== FIN DE PRUEBAS CRUD ===");
        System.out.println("Nota: Las pruebas asumen que las tablas están vacías inicialmente.");
        System.out.println("Asegúrate de tener PostgreSQL corriendo y las tablas creadas.");

        // Pruebas de nuevos métodos de venta
        System.out.println("\n=== PRUEBAS DE NUEVOS MÉTODOS DE VENTA ===");
        try {
            // Crear un carrito con items para prueba
            Carrito carritoTest = new Carrito();
            carritoTest.setUsuario(cliente1.getUsuario());
            carritoTest.setSessionId("test_session_carrito");
            carritoTest.setCreatedAt(LocalDateTime.now());
            carritoTest.setUpdatedAt(LocalDateTime.now());
            carritoTest = carritoService.save(carritoTest);

            // Agregar item al carrito
            ItemCarrito item = new ItemCarrito();
            item.setCarrito(carritoTest);
            item.setProducto(producto1);
            item.setCantidad(2);
            item.setPrecio(producto1.getPrecio());
            item = itemCarritoService.agregarItem(item);

            // Probar crearVentaDesdeCarrito
            Venta ventaDesdeCarrito = ((VentaServiceImpl) ventaService).crearVentaDesdeCarrito(carritoTest.getId(), cliente1.getId());
            System.out.println("Venta desde carrito creada con ID: " + ventaDesdeCarrito.getId() + ", Estado: " + ventaDesdeCarrito.getEstado());

            // Probar venta al contado directa
            Venta ventaDirecta = new Venta();
            ventaDirecta.setCliente(cliente1);
            ventaDirecta.setUsuario(cliente1.getUsuario());
            List<DetalleVenta> detalles = new ArrayList<>();
            DetalleVenta det = new DetalleVenta();
            det.setProducto(producto1);
            det.setCantidad(1);
            det.setPrecioUnitario(producto1.getPrecio());
            det.setSubtotal(producto1.getPrecio());
            detalles.add(det);

            Venta ventaAlContado = ((VentaServiceImpl) ventaService).procesarVentaAlContado(ventaDirecta, detalles);
            System.out.println("Venta al contado directa creada con ID: " + ventaAlContado.getId() + ", Estado: " + ventaAlContado.getEstado());

            // Probar venta por cuotas
            Venta ventaCuotas = new Venta();
            ventaCuotas.setCliente(cliente1);
            ventaCuotas.setUsuario(cliente1.getUsuario());
            Credito credito = new Credito();
            credito.setNumeroCuotas("3");

            Venta ventaPorCuotas = ((VentaServiceImpl) ventaService).procesarVentaPorCuotas(ventaCuotas, detalles, credito);
            System.out.println("Venta por cuotas creada con ID: " + ventaPorCuotas.getId() + ", Estado: " + ventaPorCuotas.getEstado());

        } catch (Exception e) {
            System.out.println("Error en pruebas de nuevos métodos: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== FIN DE TODAS LAS PRUEBAS ===");
    }



}