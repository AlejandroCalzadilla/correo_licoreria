package org.bebidas.modules.ventas;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.creditos.services.CreditoServiceImpl;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.inventario.Inventario;
import org.bebidas.modules.inventario.Producto;
import org.bebidas.modules.service.interfaces.DetalleVentaService;
import org.bebidas.modules.service.interfaces.InventarioService;
import org.bebidas.modules.service.interfaces.PagoService;
import org.bebidas.modules.service.interfaces.VentaService;
import org.bebidas.modules.ventas.models.DetalleVenta;
import org.bebidas.modules.ventas.repositories.DetalleVentaDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class DetalleVentaServiceImpl extends GenericServiceImpl<DetalleVenta, Long> implements DetalleVentaService {

    private final DetalleVentaDAO detalleVentaDAO;
    private final InventarioService inventarioService;
    private VentaService ventaService;
    private PagoService pagoService;
    private CreditoServiceImpl creditoService;

    public DetalleVentaServiceImpl(DetalleVentaDAO detalleVentaDAO, InventarioService inventarioService) {
        super(detalleVentaDAO);
        this.detalleVentaDAO = detalleVentaDAO;
        this.inventarioService = inventarioService;
    }

    public void setVentaService(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    public void setPagoService(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    public void setCreditoService(CreditoServiceImpl creditoService) {
        this.creditoService = creditoService;
    }

    @Override
    public List<DetalleVenta> buscarPorVenta(Long ventaId) {
        return detalleVentaDAO.buscarPorVenta(ventaId);
    }

    @Override
    public List<DetalleVenta> buscarPorProducto(Long productoId) {
        return detalleVentaDAO.buscarPorProducto(productoId);
    }

    @Override
    public BigDecimal obtenerTotalVentasPorProducto(Long productoId) {
        return detalleVentaDAO.obtenerTotalVentasPorProducto(productoId);
    }

    @Override
    public Integer obtenerCantidadVendidaPorProducto(Long productoId) {
        return detalleVentaDAO.obtenerCantidadVendidaPorProducto(productoId);
    }

    @Override
    public DetalleVenta insertar(DetalleVenta detalle) {
        // Verificar stock disponible
        Integer stockActual = inventarioService.obtenerStockActual(detalle.getProducto().getId());
        if (stockActual < detalle.getCantidad()) {
            throw new IllegalArgumentException(
                    "No hay suficiente stock para el producto " + detalle.getProducto().getNombre());
        }

        // Registrar salida de inventario
        Inventario inventario = new Inventario();
        inventario.setProducto(detalle.getProducto());
        inventario.setCantidad(detalle.getCantidad());
        inventarioService.registrarSalida(inventario);

        return detalleVentaDAO.save(detalle);
    }

    @Override
    public DetalleVenta actualizar(DetalleVenta detalle) {
        DetalleVenta detalleActual = this.findById(detalle.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "El detalle de venta con ID " + detalle.getId() + " no existe"));

        Integer stockActual = inventarioService.obtenerStockActual(detalle.getProducto().getId());
        Integer difference = detalle.getCantidad() - detalleActual.getCantidad();

        if (stockActual < difference) {
            throw new IllegalArgumentException("No hay suficiente stock para actualizar el detalle de venta");
        }

        // Ajustar inventario
        Inventario inventario = new Inventario();
        inventario.setProducto(detalle.getProducto());
        inventario.setCantidad(Math.abs(difference));
        if (difference > 0) {
            inventarioService.registrarSalida(inventario);
        } else if (difference < 0) {
            inventarioService.registrarEntrada(inventario);
        }

        return detalleVentaDAO.save(detalle);
    }

    @Override
    public void eliminar(Long id) {
        DetalleVenta detalleActual = this.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El detalle de venta con ID " + id + " no existe"));

        // Registrar entrada para devolver el stock
        Inventario inventario = new Inventario();
        inventario.setProducto(detalleActual.getProducto());
        inventario.setCantidad(detalleActual.getCantidad());
        inventarioService.registrarEntrada(inventario);

        detalleVentaDAO.delete(id);
    }

    @Override
    public Venta procesarCreacionDetalleVenta(Long ventaId, Long productoId, Integer cantidad,
            BigDecimal precioUnitario) {
        if (ventaService == null || pagoService == null || creditoService == null) {
            throw new IllegalStateException("Dependencias de negocio no configuradas en DetalleVentaService");
        }

        Venta venta = ventaService.findById(ventaId).orElse(null);
        if (venta == null) {
            throw new IllegalArgumentException("Venta no encontrada con ID: " + ventaId);
        }

        boolean tienePagos = pagoService.findAll().stream()
                .anyMatch(p -> p.getVenta().getId().equals(ventaId));
        if (tienePagos) {
            throw new IllegalStateException(
                    "No se puede crear detalle de venta: ya existen pagos asociados a esta venta o problemas con la venta");
        }

        DetalleVenta detalle = new DetalleVenta();
        detalle.setVenta(venta);
        Producto producto = new Producto();
        producto.setId(productoId);
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        DetalleVenta detalleGuardado = save(detalle);

        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        BigDecimal montoActual = venta.getMontoTotal() != null ? venta.getMontoTotal() : BigDecimal.ZERO;
        BigDecimal nuevoMonto = montoActual.add(subtotal);
        venta.setMontoTotal(nuevoMonto);
        venta.setSaldo(nuevoMonto);
        ventaService.save(venta);
        System.out.println("debug= venta detalles ");
        if (venta.getTipo() != null && venta.getTipo().equals("credito")) {
            Credito creditoExistente = creditoService.findAll().stream()
                    .filter(c -> c.getVenta().getId().equals(venta.getId()))
                    .findFirst()
                    .orElse(null);

            if (creditoExistente != null) {
                creditoExistente.setMontoTotal(nuevoMonto);
                creditoExistente.setSaldo(nuevoMonto);
                creditoService.save(creditoExistente);
            } else {
                System.out.println("debug= el credito se esta creando");
                Credito credito = new Credito();
                credito.setVenta(venta);
                credito.setMontoTotal(nuevoMonto);
                credito.setSaldo(nuevoMonto);
                credito.setNumeroCuotas(venta.getNumeroCuotas() != null ? venta.getNumeroCuotas() : "1");
                credito.setEstado("ACTIVO");
                credito.setFechaInicio(LocalDate.now());
                creditoService.save(credito);
            }
        }
        if (venta.getDetalles() == null) {
            venta.setDetalles(new java.util.ArrayList<>());
        }
        venta.getDetalles().add(detalleGuardado);
        return venta;
    }
}