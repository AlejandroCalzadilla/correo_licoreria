package org.bebidas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bebidas.core.util.BaseEntity;
import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.mappers.CarritoMapper;
import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.categorias.mappers.CategoriaMapper;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.clientes.mappers.ClienteMapper;
import org.bebidas.modules.compras.Compra;
import org.bebidas.modules.compras.mappers.CompraMapper;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.creditos.mappers.CreditoMapper;
import org.bebidas.modules.inventario.Inventario;
import org.bebidas.modules.inventario.Producto;
import org.bebidas.modules.inventario.mappers.InventarioMapper;
import org.bebidas.modules.inventario.mappers.ProductoMapper;
import org.bebidas.modules.mail.crud_seleccion.ServiceProvider;
import org.bebidas.modules.proveedor.Proveedor;
import org.bebidas.modules.proveedor.mappers.ProveedorMapper;
import org.bebidas.modules.service.interfaces.GenericService;
import org.bebidas.modules.usuarios.Rol;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.usuarios.mappers.RolMapper;
import org.bebidas.modules.usuarios.mappers.UsuarioMapper;
import org.bebidas.modules.vendedores.Vendedor;
import org.bebidas.modules.vendedores.mappers.VendedorMapper;
import org.bebidas.modules.ventas.Venta;
import org.bebidas.modules.ventas.VentaMapper;

public class Main {

    public static void main(String[] args) {
        ServiceProvider sp = ServiceProvider.getInstance();
        String seed = UUID.randomUUID().toString().substring(0, 8);

        try {
            System.out.println("===== INICIO PRUEBA CRUD MASIVA =====");

            Rol rol1 = new Rol();
            rol1.setNombre("ROL_A_" + seed);
            rol1.setDescripcion("Rol de prueba A");
            rol1 = sp.getRolService().save(rol1);

            Rol rol2 = new Rol();
            rol2.setNombre("ROL_B_" + seed);
            rol2.setDescripcion("Rol de prueba B");
            rol2 = sp.getRolService().save(rol2);

            rol1.setDescripcion("Rol de prueba A (editado)");
            sp.getRolService().save(rol1);
            sp.getRolService().delete(rol2.getId());
            System.out.println("ROLES -> " + "\n"+ RolMapper.obtenerTodosTable(sp.getRolService().findAll()));

            Categoria categoria1 = new Categoria();
            categoria1.setNombre("CAT_A_" + seed);
            categoria1 = sp.getCategoriaService().save(categoria1);

            Categoria categoria2 = new Categoria();
            categoria2.setNombre("CAT_B_" + seed);
            categoria2 = sp.getCategoriaService().save(categoria2);

            categoria1.setNombre("CAT_A_EDIT_" + seed);
            sp.getCategoriaService().save(categoria1);
            sp.getCategoriaService().delete(categoria2.getId());
            System.out.println("CATEGORIAS -> " + "\n"+ CategoriaMapper.obtenerTodosTable(sp.getCategoriaService().findAll()));

            Proveedor proveedor1 = new Proveedor();
            proveedor1.setNombre("Proveedor A " + seed);
            proveedor1.setTelefono("70000001");
            proveedor1.setNit("NIT-A-" + seed);
            proveedor1.setCorreo("prov.a." + seed + "@mail.com");
            proveedor1.setDireccion("Av. Demo 1");
            proveedor1 = sp.getProveedorService().save(proveedor1);

            Proveedor proveedor2 = new Proveedor();
            proveedor2.setNombre("Proveedor B " + seed);
            proveedor2.setTelefono("70000002");
            proveedor2.setNit("NIT-B-" + seed);
            proveedor2.setCorreo("prov.b." + seed + "@mail.com");
            proveedor2.setDireccion("Av. Demo 2");
            proveedor2 = sp.getProveedorService().save(proveedor2);

            proveedor1.setTelefono("71111111");
            sp.getProveedorService().save(proveedor1);
            sp.getProveedorService().delete(proveedor2.getId());
            System.out.println("PROVEEDORES -> " + "\n"+ ProveedorMapper.obtenerTodosTable(sp.getProveedorService().findAll()));

            Usuario usuario1 = new Usuario();
            usuario1.setNombre("Usuario A " + seed);
            usuario1.setCorreo("usuario.a." + seed + "@mail.com");
            usuario1.setClave("123456");
            usuario1.setEstado("activo");
            usuario1.setRol(rol1);
            usuario1 = sp.getUsuarioService().save(usuario1);

            Usuario usuario2 = new Usuario();
            usuario2.setNombre("Usuario B " + seed);
            usuario2.setCorreo("usuario.b." + seed + "@mail.com");
            usuario2.setClave("123456");
            usuario2.setEstado("activo");
            usuario2.setRol(rol1);
            usuario2 = sp.getUsuarioService().save(usuario2);

            usuario1.setNombre("Usuario A Editado " + seed);
            sp.getUsuarioService().save(usuario1);
            sp.getUsuarioService().delete(usuario2.getId());
            System.out.println("USUARIOS -> " + "\n"+ UsuarioMapper.obtenerTodosTable(sp.getUsuarioService().findAll()));

            Cliente cliente1 = new Cliente();
            cliente1.setCi("CI-A-" + seed);
            cliente1.setNombre("Cliente A " + seed);
            cliente1.setTelefono("72000001");
            cliente1.setDireccion("Calle Cliente A");
            cliente1.setEstado('A');
            cliente1.setUsuario(usuario1);
            cliente1.setCreditoAprobado(false);
            cliente1.setLimiteCredito(0.0);
            cliente1 = sp.getClienteService().save(cliente1);

            Cliente cliente2 = new Cliente();
            cliente2.setCi("CI-B-" + seed);
            cliente2.setNombre("Cliente B " + seed);
            cliente2.setTelefono("72000002");
            cliente2.setDireccion("Calle Cliente B");
            cliente2.setEstado('A');
            cliente2.setUsuario(usuario1);
            cliente2.setCreditoAprobado(false);
            cliente2.setLimiteCredito(0.0);
            cliente2 = sp.getClienteService().save(cliente2);

            cliente1.setTelefono("73333333");
            sp.getClienteService().save(cliente1);
            sp.getClienteService().delete(cliente2.getId());
            System.out.println("CLIENTES -> " + "\n"+ ClienteMapper.obtenerTodosTable(sp.getClienteService().findAll()));

            Vendedor vendedor1 = new Vendedor();
            vendedor1.setCi("VEN-A-" + seed);
            vendedor1.setNombre("Vendedor A " + seed);
            vendedor1.setUsuario(usuario1);
            vendedor1 = sp.getVendedorService().save(vendedor1);

            Vendedor vendedor2 = new Vendedor();
            vendedor2.setCi("VEN-B-" + seed);
            vendedor2.setNombre("Vendedor B " + seed);
            vendedor2.setUsuario(usuario1);
            vendedor2 = sp.getVendedorService().save(vendedor2);

            vendedor1.setNombre("Vendedor A Editado " + seed);
            sp.getVendedorService().save(vendedor1);
            sp.getVendedorService().delete(vendedor2.getId());
            System.out.println("VENDEDORES -> " + "\n"+ VendedorMapper.obtenerTodosTable(sp.getVendedorService().findAll()));

            Producto producto1 = new Producto();
            producto1.setCodigo("PROD-A-" + seed);
            producto1.setNombre("Producto A " + seed);
            producto1.setDescripcion("Descripcion A");
            producto1.setPrecio(new BigDecimal("10.50"));
            producto1.setMarca("Marca A");
            producto1.setCategoria(categoria1);
            producto1.setImagen("https://img.a");
            producto1 = sp.getProductoService().save(producto1);

            Producto producto2 = new Producto();
            producto2.setCodigo("PROD-B-" + seed);
            producto2.setNombre("Producto B " + seed);
            producto2.setDescripcion("Descripcion B");
            producto2.setPrecio(new BigDecimal("11.50"));
            producto2.setMarca("Marca B");
            producto2.setCategoria(categoria1);
            producto2.setImagen("https://img.b");
            producto2 = sp.getProductoService().save(producto2);

            producto1.setPrecio(new BigDecimal("12.00"));
            sp.getProductoService().save(producto1);
            sp.getProductoService().delete(producto2.getId());
            System.out.println("PRODUCTOS -> " + "\n"+ ProductoMapper.obtenerTodosTable(sp.getProductoService().findAll()));

            Carrito carrito1 = new Carrito();
            carrito1.setSessionId("SES-A-" + seed);
            carrito1.setUsuario(usuario1);
            carrito1.setCreatedAt(LocalDateTime.now());
            carrito1.setUpdatedAt(LocalDateTime.now());
            carrito1 = sp.getCarritoService().save(carrito1);

            Carrito carrito2 = new Carrito();
            carrito2.setSessionId("SES-B-" + seed);
            carrito2.setUsuario(usuario1);
            carrito2.setCreatedAt(LocalDateTime.now());
            carrito2.setUpdatedAt(LocalDateTime.now());
            carrito2 = sp.getCarritoService().save(carrito2);

            carrito1.setSessionId("SES-A-EDIT-" + seed);
            carrito1.setUpdatedAt(LocalDateTime.now());
            sp.getCarritoService().save(carrito1);
            sp.getCarritoService().delete(carrito2.getId());
            System.out.println("CARRITOS -> " + "\n"+ CarritoMapper.obtenerTodosTable(sp.getCarritoService().findAll()));

            Compra compra1 = new Compra();
            compra1.setNroCompra("C-DEMO-A-" + seed);
            compra1.setFecha(LocalDate.now());
            compra1.setEstado("completada");
            compra1.setProveedor(proveedor1);
            compra1.setDescripcion("Compra demo A");
            compra1 = sp.getCompraService().save(compra1);

            Compra compra2 = new Compra();
            compra2.setNroCompra("C-DEMO-B-" + seed);
            compra2.setFecha(LocalDate.now());
            compra2.setEstado("completada");
            compra2.setProveedor(proveedor1);
            compra2.setDescripcion("Compra demo B");
            compra2 = sp.getCompraService().save(compra2);

            compra1.setDescripcion("Compra demo A editada");
            sp.getCompraService().save(compra1);
            sp.getCompraService().delete(compra2.getId());
            System.out.println("COMPRAS -> " + "\n"+ CompraMapper.obtenerTodosTable(sp.getCompraService().findAll()));

            Inventario inventario1 = new Inventario();
            inventario1.setTipoMovimiento("ENTRADA");
            inventario1.setCantidad(10);
            inventario1.setFecha(LocalDate.now());
            inventario1.setStockActual(10);
            inventario1.setGlosa("Ingreso inicial");
            inventario1.setUsuario(usuario1);
            inventario1.setProducto(producto1);
            inventario1 = sp.getInventarioService().save(inventario1);

            Inventario inventario2 = new Inventario();
            inventario2.setTipoMovimiento("ENTRADA");
            inventario2.setCantidad(20);
            inventario2.setFecha(LocalDate.now());
            inventario2.setStockActual(30);
            inventario2.setGlosa("Ingreso secundario");
            inventario2.setUsuario(usuario1);
            inventario2.setProducto(producto1);
            inventario2 = sp.getInventarioService().save(inventario2);

            inventario1.setGlosa("Ingreso inicial editado");
            sp.getInventarioService().save(inventario1);
            sp.getInventarioService().delete(inventario2.getId());
            System.out.println("INVENTARIO -> " + "\n"+ InventarioMapper.obtenerTodosTable(sp.getInventarioService().findAll()));

            Venta venta1 = new Venta();
            venta1.setNroVenta("V-DEMO-A-" + seed);
            venta1.setFecha(LocalDate.now());
            venta1.setTipo("contado");
            venta1.setMontoTotal(new BigDecimal("100.00"));
            venta1.setSaldo(new BigDecimal("100.00"));
            venta1.setNumeroCuotas(null);
            venta1.setEstado("pendiente");
            venta1.setCliente(cliente1);
            venta1.setMetodoPago("efectivo");
            venta1.setEstadoPago("pendiente");
            venta1.setUsuario(usuario1);
            venta1 = sp.getVentaService().save(venta1);

            Venta venta2 = new Venta();
            venta2.setNroVenta("V-DEMO-B-" + seed);
            venta2.setFecha(LocalDate.now());
            venta2.setTipo("credito");
            venta2.setMontoTotal(new BigDecimal("200.00"));
            venta2.setSaldo(new BigDecimal("200.00"));
            venta2.setNumeroCuotas("4");
            venta2.setEstado("pendiente");
            venta2.setCliente(cliente1);
            venta2.setMetodoPago(null);
            venta2.setEstadoPago("pendiente");
            venta2.setUsuario(usuario1);
            venta2 = sp.getVentaService().save(venta2);

            venta1.setMontoTotal(new BigDecimal("110.00"));
            venta1.setSaldo(new BigDecimal("110.00"));
            sp.getVentaService().save(venta1);
            sp.getVentaService().delete(venta2.getId());
            System.out.println("VENTAS -> " + "\n"+ VentaMapper.obtenerTodosTable(sp.getVentaService().findAll()));

            Credito credito1 = new Credito();
            credito1.setVenta(venta1);
            credito1.setMontoTotal(new BigDecimal("110.00"));
            credito1.setSaldo(new BigDecimal("110.00"));
            credito1.setNumeroCuotas("2");
            credito1.setEstado("ACTIVO");
            credito1.setFechaInicio(LocalDate.now());
            credito1 = sp.getCreditoService().save(credito1);

            Credito credito2 = new Credito();
            credito2.setVenta(venta1);
            credito2.setMontoTotal(new BigDecimal("50.00"));
            credito2.setSaldo(new BigDecimal("50.00"));
            credito2.setNumeroCuotas("1");
            credito2.setEstado("ACTIVO");
            credito2.setFechaInicio(LocalDate.now());
            credito2 = sp.getCreditoService().save(credito2);

            credito1.setEstado("PENDIENTE");
            sp.getCreditoService().save(credito1);
            sp.getCreditoService().delete(credito2.getId());
            System.out.println("CREDITOS -> " + "\n"+ CreditoMapper.obtenerTodosTable(sp.getCreditoService().findAll()));

            System.out.println("===== LIMPIEZA FINAL (BORRAR TODO) =====");
            purgeAll("CREDITOS", sp.getCreditoService());
            purgeAll("VENTAS", sp.getVentaService());
            purgeAll("INVENTARIO", sp.getInventarioService());
            purgeAll("COMPRAS", sp.getCompraService());
            purgeAll("CARRITOS", sp.getCarritoService());
            purgeAll("VENDEDORES", sp.getVendedorService());
            purgeAll("CLIENTES", sp.getClienteService());
            purgeAll("PRODUCTOS", sp.getProductoService());
            purgeAll("USUARIOS", sp.getUsuarioService());
            purgeAll("PROVEEDORES", sp.getProveedorService());
            purgeAll("CATEGORIAS", sp.getCategoriaService());
            purgeAll("ROLES", sp.getRolService());

            System.out.println("===== FIN PRUEBA CRUD MASIVA =====");
        } catch (Exception e) {
            System.err.println("Error en ejecución CRUD masiva: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static <T extends BaseEntity> void purgeAll(String nombre, GenericService<T, Long> service) {
        List<T> registros = new ArrayList<>(service.findAll());
        for (T entidad : registros) {
            service.delete(entidad.getId());
        }
        System.out.println(nombre + " eliminados: " + registros.size());
    }
}
