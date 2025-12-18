package org.bebidas.modules.ventas.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.ItemCarrito;
import org.bebidas.modules.carrito.services.interfaces.CarritoService;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.dao.interfaces.VentaDAO;

import org.bebidas.modules.creditos.services.interfaces.CreditoService;
import org.bebidas.modules.service.PagoCuotaService;
import org.bebidas.modules.service.interfaces.*;
import org.bebidas.modules.ventas.DetalleVenta;
import org.bebidas.modules.ventas.Pago;
import org.bebidas.modules.ventas.Venta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class VentaServiceImpl extends GenericServiceImpl<Venta, Long> implements VentaService {

    private final VentaDAO ventaDAO;
    private final CarritoService carritoService;
    private final DetalleVentaService detalleVentaService;
    private final PagoService pagoService;
    private final CreditoService creditoService;
    private final PagoCuotaService pagoCuotaService;

    public VentaServiceImpl(VentaDAO ventaDAO, CarritoService carritoService, 
                           DetalleVentaService detalleVentaService, PagoService pagoService,
                           CreditoService creditoService, PagoCuotaService pagoCuotaService) {
        super(ventaDAO);
        this.ventaDAO = ventaDAO;
        this.carritoService = carritoService;
        this.detalleVentaService = detalleVentaService;
        this.pagoService = pagoService;
        this.creditoService = creditoService;
        this.pagoCuotaService = pagoCuotaService;
    }

    @Override
    public List<Venta> buscarPorFecha(LocalDate fecha) {
        return ventaDAO.buscarPorFecha(fecha);
    }

    @Override
    public List<Venta> buscarPorCliente(Long clienteId) {
        return ventaDAO.buscarPorCliente(clienteId);
    }

    @Override
    public List<Venta> buscarPorEstado(String estado) {
        return ventaDAO.buscarPorEstado(estado);
    }

    @Override
    public List<Venta> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return ventaDAO.buscarPorRangoFechas(inicio, fin);
    }

    @Override
    public List<Venta> buscarPorUsuario(Long usuarioId) {
        return ventaDAO.buscarPorUsuario(usuarioId);
    }

    @Override
    public Venta crearVenta(Venta venta) {
        // Lógica para crear una nueva venta
        venta.setFecha(LocalDate.now());
        venta.setEstado("PENDIENTE");
        // Calcular totales, actualizar inventario, etc.
        return save(venta);
    }

    @Override
    public void anularVenta(Long ventaId, String motivo) {
        Venta venta = findById(ventaId).orElseThrow(() -> 
            new RuntimeException("Venta no encontrada con ID: " + ventaId));
        
        // Validar que la venta se pueda anular
        if ("ANULADA".equals(venta.getEstado())) {
            throw new IllegalStateException("La venta ya está anulada");
        }
        
        // Actualizar estado y motivo de anulación
        venta.setEstado("ANULADA");
        // Agregar motivo de anulación (podrías tener un campo para esto en el modelo)
        
        save(venta);
        
        // Aquí podrías agregar lógica para revertir el inventario si es necesario
    }

    @Override
    public Venta completarVenta(Long ventaId) {
        Venta venta = findById(ventaId).orElseThrow(() -> 
            new RuntimeException("Venta no encontrada con ID: " + ventaId));
        
        // Validar que la venta se pueda completar
        if (!"PENDIENTE".equals(venta.getEstado())) {
            throw new IllegalStateException("La venta no puede ser completada desde el estado actual: " + venta.getEstado());
        }
        
        // Actualizar estado a completada
        venta.setEstado("COMPLETADA");
        venta.setSaldo(BigDecimal.ZERO); // Asumiendo que se paga completamente
        
        return save(venta);
    }

    @Override
    public BigDecimal calcularTotalVentasPorCliente(Long clienteId) {
        List<Venta> ventas = buscarPorCliente(clienteId);
        return ventas.stream()
            .filter(v -> "COMPLETADA".equals(v.getEstado()))
            .map(Venta::getMontoTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Venta> obtenerVentasPendientes() {
        return buscarPorEstado("PENDIENTE");
    }

    @Override
    public List<Venta> obtenerVentasCompletadas() {
        return buscarPorEstado("COMPLETADA");
    }

    @Override
    public List<Venta> obtenerVentasAnuladas() {
        return buscarPorEstado("ANULADA");
    }

    // Método para crear venta desde carrito (al contado)
    public Venta crearVentaDesdeCarrito(Long carritoId, Long clienteId) {
        // Obtener carrito
        Carrito carrito = carritoService.findById(carritoId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado con ID: " + carritoId));

        // Verificar que el carrito tenga items
        List<ItemCarrito> items = carrito.getItems();
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        // Crear venta
        Venta venta = new Venta();
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        venta.setCliente(cliente);
        venta.setUsuario(carrito.getUsuario());
        venta.setFecha(LocalDate.now());
        venta.setEstado("PENDIENTE");

        // Calcular total y crear detalles
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrito item : items) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getProducto().getPrecio());
            detalle.setSubtotal(item.getProducto().getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
            
            venta.getDetalles().add(detalle);
            total = total.add(detalle.getSubtotal());
        }
        venta.setMontoTotal(total);
        venta.setSaldo(total);

        // Guardar venta y detalles
        Venta ventaGuardada = save(venta);
        for (DetalleVenta detalle : venta.getDetalles()) {
            detalleVentaService.save(detalle);
        }

        // Procesar pago al contado
        Pago pago = new Pago();
        pago.setVenta(ventaGuardada);
        pago.setFechaPago(java.time.LocalDateTime.now());
        pago.setMonto(total);
        pago.setTipoPago("EFECTIVO"); // O como se determine
        pagoService.save(pago);

        // Completar venta
        ventaGuardada.setEstado("COMPLETADA");
        ventaGuardada.setSaldo(BigDecimal.ZERO);
        save(ventaGuardada);

        // Limpiar carrito (eliminar items)
        // Asumiendo que hay un método para eliminar items
        // carritoService.eliminarItems(carritoId);

        return ventaGuardada;
    }

    // Método para venta directa al contado
    public Venta procesarVentaAlContado(Venta venta, List<DetalleVenta> detalles) {
        // Calcular totales
        BigDecimal total = detalles.stream()
            .map(DetalleVenta::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        venta.setMontoTotal(total);
        venta.setSaldo(BigDecimal.ZERO);
        venta.setEstado("COMPLETADA");
        venta.setFecha(LocalDate.now());

        // Guardar venta
        Venta ventaGuardada = save(venta);

        // Guardar detalles
        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(ventaGuardada);
            detalleVentaService.save(detalle);
        }

        // Crear pago
        Pago pago = new Pago();
        pago.setVenta(ventaGuardada);
        pago.setFechaPago(java.time.LocalDateTime.now());
        pago.setMonto(total);
        pago.setTipoPago("EFECTIVO"); // O parámetro
        pagoService.save(pago);

        return ventaGuardada;
    }

    // Método para venta directa por cuotas
    public Venta procesarVentaPorCuotas(Venta venta, List<DetalleVenta> detalles, Credito credito) {
        // Calcular totales
        BigDecimal total = detalles.stream()
            .map(DetalleVenta::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        venta.setMontoTotal(total);
        venta.setSaldo(total);
        venta.setEstado("PENDIENTE");
        venta.setFecha(LocalDate.now());

        // Guardar venta
        Venta ventaGuardada = save(venta);

        // Guardar detalles
        for (DetalleVenta detalle : detalles) {
            detalle.setVenta(ventaGuardada);
            detalleVentaService.save(detalle);
        }

        // Crear crédito
        credito.setVenta(ventaGuardada);
        credito.setMontoTotal(total);
        credito.setSaldo(total);
        credito.setFechaInicio(LocalDate.now());
        credito.setEstado("ACTIVO");
        Credito creditoGuardado = creditoService.save(credito);

        // Crear pagos de cuotas programados (opcional, dependiendo de numeroCuotas)
        // Por simplicidad, no crearlos aquí, se pueden crear después

        return ventaGuardada;
    }
}
