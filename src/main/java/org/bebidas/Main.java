package org.bebidas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bebidas.core.util.BaseEntity;
import org.bebidas.modules.mail.crud_seleccion.Actualizar;
import org.bebidas.modules.mail.crud_seleccion.Crear;
import org.bebidas.modules.mail.crud_seleccion.Eliminar;
import org.bebidas.modules.mail.crud_seleccion.Listar;
import org.bebidas.modules.mail.crud_seleccion.ServiceProvider;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.service.interfaces.GenericService;
import org.bebidas.modules.ventas.Venta;

public class Main {

    private static final Pattern ID_DIRECTO = Pattern.compile("(?i)id\\s*:\\s*(\\d+)");
    private static final Pattern CUALQUIER_NUMERO = Pattern.compile("\\b(\\d+)\\b");

    public static void main(String[] args) {
        String seed = UUID.randomUUID().toString().substring(0, 8);
        ServiceProvider sp = ServiceProvider.getInstance();
        Crear crear = new Crear();
        Listar listar = new Listar();

        try {


            System.out.println("===== INICIO CARGA CON CLASES CRUD =====");

            purgeAll("ITEMCARRITOS", sp.getItemCarritoService());
            purgeAll("CARRITOS", sp.getCarritoService());
            purgeAll("DETALLEVENTAS", sp.getDetalleVentaService());
            purgeAll("CREDITO", sp.getCreditoService());
            purgeAll("VENTAS", sp.getVentaService());
            purgeAll("PAGOS", sp.getPagoService());
            purgeAll("DETALLECOMPRAS", sp.getDetalleCompraService());
            purgeAll("INVENTARIO", sp.getInventarioService());
            purgeAll("COMPRAS", sp.getCompraService());
            purgeAll("PRODUCTOS", sp.getProductoService());
            purgeAll("CATEGORIAS", sp.getCategoriaService());
            purgeAll("CLIENTES", sp.getClienteService());
            purgeAll("PROVEEDORES", sp.getProveedorService());
            purgeAll("USUARIOS", sp.getUsuarioService());
            purgeAll("ROLES", sp.getRolService());  

            Long rolAdmin = crearConId(crear, "ROLES", "ADMIN_" + seed + ",Rol administrador");
            Long rolCliente = crearConId(crear, "ROLES", "CLIENTE_" + seed + ",Rol cliente");

            Long usuarioOperador = crearConId(crear, "USUARIOS",
                "Operador " + seed + ",operador." + seed + "@mail.com,123456,activo," + rolAdmin);

            Long proveedorA = crearConId(crear, "PROVEEDORES",
                "Proveedor A " + seed + ",70000001,Av. Demo 1,NIT-A-" + seed + ",prov.a." + seed + "@mail.com");
            Long proveedorB = crearConId(crear, "PROVEEDORES",
                "Proveedor B " + seed + ",70000002,Av. Demo 2,NIT-B-" + seed + ",prov.b." + seed + "@mail.com");

            Long categoriaCervezas = crearConId(crear, "CATEGORIAS", "CERVEZAS_" + seed);
            Long categoriaVinos = crearConId(crear, "CATEGORIAS", "VINOS_" + seed);
            Long categoriaLicores = crearConId(crear, "CATEGORIAS", "LICORES_" + seed);

            Long clienteA = crearConId(crear, "CLIENTES",
                "CIA" + seed + ",Cliente A " + seed + ",60000001,Av Cliente 1,A,cliA" + seed + ",clia" + seed + "@mail.com,123456," + rolCliente);
            Long clienteB = crearConId(crear, "CLIENTES",
                "CIB" + seed + ",Cliente B " + seed + ",60000002,Av Cliente 2,A,cliB" + seed + ",clib" + seed + "@mail.com,123456," + rolCliente);
            Long clienteC = crearConId(crear, "CLIENTES",
                "CIC" + seed + ",Cliente C " + seed + ",60000003,Av Cliente 3,A,cliC" + seed + ",clic" + seed + "@mail.com,123456," + rolCliente);

            Long producto1 = crearConId(crear, "PRODUCTOS",
                categoriaCervezas + ",Corona 355ml,15.00,COR-" + seed + ",Cerveza rubia,Corona");
            Long producto2 = crearConId(crear, "PRODUCTOS",
                categoriaCervezas + ",Paceña 355ml,12.00,PAC-" + seed + ",Cerveza nacional,Paceña");
            Long producto3 = crearConId(crear, "PRODUCTOS",
                categoriaVinos + ",Cabernet 750ml,45.00,CAB-" + seed + ",Vino tinto,Campos");
            Long producto4 = crearConId(crear, "PRODUCTOS",
                categoriaVinos + ",Sauvignon 750ml,42.00,SAU-" + seed + ",Vino blanco,Solana");
            Long producto5 = crearConId(crear, "PRODUCTOS",
                categoriaLicores + ",Ron Añejo,65.00,RON-" + seed + ",Ron oscuro,OldBarrel");
            Long producto6 = crearConId(crear, "PRODUCTOS",
                categoriaLicores + ",Vodka 700ml,58.00,VOD-" + seed + ",Vodka premium,Frost");

            Long[] proveedores = { proveedorA, proveedorB };
            Long[] clientes = { clienteA, clienteB, clienteC };
            Long[] productos = { producto1, producto2, producto3, producto4, producto5, producto6 };
            BigDecimal[] precios = {
                new BigDecimal("15.00"),
                new BigDecimal("12.00"),
                new BigDecimal("45.00"),
                new BigDecimal("42.00"),
                new BigDecimal("65.00"),
                new BigDecimal("58.00")
            };

            int diasSimulados = 30;

            for (int dia = 1; dia <= diasSimulados; dia++) {
            Long proveedor = proveedores[dia % proveedores.length];
            Long compraId = crearConId(crear, "COMPRAS", proveedor + ",Compra simulada día " + dia + " " + seed);

            int idxProdA = dia % productos.length;
            int idxProdB = (dia + 2) % productos.length;
            int cantA = ThreadLocalRandom.current().nextInt(8, 20);
            int cantB = ThreadLocalRandom.current().nextInt(6, 18);

            crearConId(crear, "DETALLECOMPRAS",
                compraId + "," + productos[idxProdA] + "," + cantA + "," + precios[idxProdA]);
            crearConId(crear, "DETALLECOMPRAS",
                compraId + "," + productos[idxProdB] + "," + cantB + "," + precios[idxProdB]);
            }

            for (int dia = 1; dia <= diasSimulados; dia++) {
            Long cliente = clientes[dia % clientes.length];

            Long ventaContado = crearConId(crear, "VENTAS", cliente + ",contado,NA,efectivo");
            int idxProdVenta1 = (dia + 1) % productos.length;
            int idxProdVenta2 = (dia + 3) % productos.length;
            int cantV1 = ThreadLocalRandom.current().nextInt(1, 4);
            int cantV2 = ThreadLocalRandom.current().nextInt(1, 3);

            crearConId(crear, "DETALLEVENTAS",
                ventaContado + "," + productos[idxProdVenta1] + "," + cantV1 + "," + precios[idxProdVenta1]);
            crearConId(crear, "DETALLEVENTAS",
                ventaContado + "," + productos[idxProdVenta2] + "," + cantV2 + "," + precios[idxProdVenta2]);
            seedPagoContado(crear, sp, ventaContado, dia, seed);

            Long ventaCredito = crearConId(crear, "VENTAS", cliente + ",credito,3");
            int idxProdCred = (dia + 4) % productos.length;
            int cantCred = ThreadLocalRandom.current().nextInt(1, 4);
            crearConId(crear, "DETALLEVENTAS",
                ventaCredito + "," + productos[idxProdCred] + "," + cantCred + "," + precios[idxProdCred]);
            seedPagosCredito(crear, sp, ventaCredito, dia, seed);

            Long carrito = crearConId(crear, "CARRITOS", clienteA.toString());
            int idxCarA = (dia + 2) % productos.length;
            int idxCarB = (dia + 5) % productos.length;
            int cantCarA = ThreadLocalRandom.current().nextInt(1, 3);
            int cantCarB = ThreadLocalRandom.current().nextInt(1, 3);

            crearConId(crear, "ITEMCARRITOS",
                carrito + "," + productos[idxCarA] + "," + cantCarA + "," + precios[idxCarA]);
            crearConId(crear, "ITEMCARRITOS",
                carrito + "," + productos[idxCarB] + "," + cantCarB + "," + precios[idxCarB]);

            listarEntidad(listar, "CARRITOS");
            listarEntidad(listar, "ITEMCARRITOS");    
            crearConId(crear, "VENTASCONDETALLE", cliente + ",contado," + carrito + ",NA,efectivo");
            }
            System.out.println("Simulación mensual completada: " + diasSimulados + " días de compras/ventas/carrito");
            listarEntidad(listar, "VENTAS");
            System.out.println("===== FIN CARGA CON CLASES CRUD =====");
        } catch (Exception e) {
            System.err.println("Error en ejecución de carga CRUD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Long crearConId(Crear crear, String entidad, String parametros) throws Exception {
        String respuesta = crear.ejecutarCrear(entidad, parametros);
        System.out.println("CREAR " + entidad + " -> " + respuesta);

        Long id = extraerId(respuesta);
        if (id == null) {
            throw new IllegalStateException("No se pudo extraer ID al crear " + entidad + ". Respuesta: " + respuesta);
        }
        return id;
    }

    private static void actualizarYMostrar(Actualizar actualizar, Listar listar, String entidad, String parametros)
            throws Exception {
        String respuestaUpdate = actualizar.ejecutarActualizar(entidad, parametros);
        System.out.println("ACTUALIZAR " + entidad + " -> " + respuestaUpdate);
        listarEntidad(listar, entidad);
    }

    private static void eliminarSiExiste(Eliminar eliminar, String entidad, Long id) throws Exception {
        if (id == null) {
            return;
        }
        String respuestaDelete = eliminar.ejecutarEliminar(entidad, id);
        System.out.println("ELIMINAR " + entidad + "(" + id + ") -> " + respuestaDelete);
    }

    private static void listarEntidad(Listar listar, String entidad) throws Exception {
        String listado = listar.ejecutarConsultaListar(entidad);
        System.out.println(entidad + " -> \n" + listado);
    }

    private static Long extraerId(String texto) {
        Matcher matcherDirecto = ID_DIRECTO.matcher(texto);
        if (matcherDirecto.find()) {
            return Long.parseLong(matcherDirecto.group(1));
        }

        Matcher matcherNumero = CUALQUIER_NUMERO.matcher(texto);
        if (matcherNumero.find()) {
            return Long.parseLong(matcherNumero.group(1));
        }

        return null;
    }

    private static void seedPagoContado(Crear crear, ServiceProvider sp, Long ventaId, int dia, String seed) throws Exception {
        Venta venta = sp.getVentaService().findById(ventaId)
                .orElseThrow(() -> new IllegalStateException("Venta no encontrada para pago contado: " + ventaId));

        BigDecimal saldo = venta.getSaldo();
        if (saldo == null || saldo.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        String parametrosPago = ventaId + ",efectivo," + saldo + ",Seeder Contado " + dia + ",contado." + seed + "." + dia + "@mail.com";
        crearConId(crear, "PAGOS", parametrosPago);
    }

    private static void seedPagosCredito(Crear crear, ServiceProvider sp, Long ventaId, int dia, String seed) throws Exception {
        Credito credito = sp.getCreditoService().findAll().stream()
                .filter(c -> c.getVenta() != null && c.getVenta().getId().equals(ventaId))
                .findFirst()
                .orElse(null);

        if (credito == null) {
            return;
        }

        int cuotas = Integer.parseInt(credito.getNumeroCuotas());
        if (cuotas <= 0) {
            return;
        }

        BigDecimal saldoTotal = credito.getSaldo().setScale(2, RoundingMode.HALF_UP);
        BigDecimal cuotaBase = saldoTotal.divide(BigDecimal.valueOf(cuotas), 2, RoundingMode.DOWN);
        BigDecimal saldoRestante = saldoTotal;

        for (int cuota = 1; cuota <= cuotas; cuota++) {
            BigDecimal montoPago = (cuota == cuotas) ? saldoRestante : cuotaBase;
            System.out.println("Simulando pago crédito - Venta ID: " + ventaId + ", Cuota: " + cuota
                    + ", Monto: " + montoPago);
            String parametrosPago = ventaId + ",transferencia," + montoPago + ",Seeder Credito " + dia + "-" + cuota
                    + ",credito." + seed + "." + dia + "." + cuota + "@mail.com";
            crearConId(crear, "PAGOS", parametrosPago);
            saldoRestante = saldoRestante.subtract(montoPago).setScale(2, RoundingMode.HALF_UP);
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
