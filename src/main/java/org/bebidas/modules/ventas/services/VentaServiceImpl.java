package org.bebidas.modules.ventas.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.carrito.Carrito;
import org.bebidas.modules.carrito.ItemCarrito;
import org.bebidas.modules.carrito.services.ItemCarritoServiceImpl;
import org.bebidas.modules.carrito.services.interfaces.CarritoService;
import org.bebidas.modules.carrito.services.interfaces.ItemCarritoService;
import org.bebidas.modules.clientes.Cliente;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.creditos.services.CreditoServiceImpl;
import org.bebidas.modules.pagos.PagoCuotaService;
import org.bebidas.modules.service.interfaces.*;
import org.bebidas.modules.ventas.Venta;
import org.bebidas.modules.ventas.models.DetalleVenta;
import org.bebidas.modules.ventas.repositories.VentaDAO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.modules.clientes.services.interfaces.ClienteService;

public class VentaServiceImpl extends GenericServiceImpl<Venta, Long> implements VentaService {

    private final VentaDAO ventaDAO;
    private final CarritoService carritoService;
    private final DetalleVentaService detalleVentaService;
    private final PagoService pagoService;
    private final CreditoServiceImpl creditoService;
    private final PagoCuotaService pagoCuotaService;
    private final ItemCarritoService itemCarritoService;
    private final ClienteService clienteService;

    public VentaServiceImpl(VentaDAO ventaDAO, CarritoService carritoService,
            DetalleVentaService detalleVentaService, PagoService pagoService,
            CreditoServiceImpl creditoService, PagoCuotaService pagoCuotaService,
            ItemCarritoService itemCarritoService, ClienteService clienteService) {
        super(ventaDAO);
        this.ventaDAO = ventaDAO;
        this.carritoService = carritoService;
        this.detalleVentaService = detalleVentaService;
        this.pagoService = pagoService;
        this.creditoService = creditoService;
        this.pagoCuotaService = pagoCuotaService;
        this.itemCarritoService = itemCarritoService;
        this.clienteService = clienteService;
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
    public Venta completarVenta(Long ventaId) {
        Venta venta = findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + ventaId));
        // Validar que la venta se pueda completar
        if (!"PENDIENTE".equals(venta.getEstado())) {
            throw new IllegalStateException(
                    "La venta no puede ser completada desde el estado actual: " + venta.getEstado());
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

    @Override
    public Venta crearVentaConDetalle(String tipo, Long carritoId, String numeroCuotas, String metodoPago) {
        try {
            Carrito carrito = carritoService.findById(carritoId).orElse(null);
            if (carrito == null)
                throw new RuntimeException("Carrito no encontrado con ID: " + carritoId);
            List<ItemCarrito> items = itemCarritoService.buscarPorCarrito(carritoId);

            if (items == null || items.isEmpty())
                throw new RuntimeException("El carrito no tiene items");
            Venta venta = new Venta();
            String nroVenta = generarSiguienteNroVenta();
            venta.setNroVenta(nroVenta);

            if (carrito.getUsuario() == null) {
                throw new RuntimeException("El carrito no tiene un usuario asociado");
            }
            Long usuarioId = carrito.getUsuario().getId();
            Cliente cliente = clienteService.findByUsuarioId(usuarioId)
                    .orElseThrow(
                            () -> new RuntimeException("Cliente no encontrado para el usuario con ID: " + usuarioId));

            venta.setCliente(cliente);
            tipo = tipo.toLowerCase();
            if (!tipo.equals("credito") && !tipo.equals("contado")) {
                throw new IllegalArgumentException("Tipo debe ser 'credito' o 'contado'");
            }
            venta.setTipo(tipo);
            if (tipo.equals("credito")) {
                if (numeroCuotas == null)
                    throw new IllegalArgumentException("Se requiere numeroCuotas para tipo credito");
                venta.setNumeroCuotas(numeroCuotas);
            }
            if (tipo.equals("contado")) {
                if (metodoPago == null)
                    throw new IllegalArgumentException("Se requiere metodoPago para tipo contado");
                venta.setMetodoPago(metodoPago);
            }
            venta.setEstado("pendiente");
            venta.setEstadoPago("pendiente");
            venta.setFecha(LocalDate.now());
            Venta ventaCreada = save(venta);
            // Procesar items del carrito y crear detalles
            BigDecimal montoTotal = BigDecimal.ZERO;
            for (ItemCarrito item : items) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(ventaCreada);
                detalle.setProducto(item.getProducto());
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnitario(item.getPrecio());
                detalleVentaService.save(detalle);
                // Sumar monto
                BigDecimal subtotal = item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()));
                montoTotal = montoTotal.add(subtotal);
            }
            ventaCreada.setMontoTotal(montoTotal);
            ventaCreada.setSaldo(montoTotal);
            Venta ventaActualizada = save(ventaCreada);
            // Si es crédito, crear crédito
            if (venta.getTipo() != null && venta.getTipo().equals("credito")) {
                Credito credito = new Credito();
                credito.setVenta(ventaActualizada);
                credito.setMontoTotal(montoTotal);
                credito.setSaldo(montoTotal);
                credito.setNumeroCuotas(
                        ventaActualizada.getNumeroCuotas() != null ? ventaActualizada.getNumeroCuotas() : "1");
                credito.setEstado("ACTIVO");
                credito.setFechaInicio(LocalDate.now());
                creditoService.save(credito);
            } else {
                System.out.println("DEBUG: No se crea crédito - Tipo de venta: " + venta.getTipo());
            }
            return ventaActualizada;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear venta con detalle: " + e.getMessage());
        }
    }

    @Override
    public Venta crearVentaBasica(Long clienteId, String tipo, String numeroCuotas, String metodoPago) {
        try {
            Venta venta = new Venta();
            String nroVenta = generarSiguienteNroVenta();
            venta.setNroVenta(nroVenta);
            Cliente cliente = clienteService.findById(clienteId).orElseThrow();
            venta.setCliente(cliente);
            tipo = tipo.toLowerCase();
            if (!tipo.equals("credito") && !tipo.equals("contado")) {
                throw new IllegalArgumentException("Tipo debe ser 'credito' o 'contado'");
            }
            venta.setTipo(tipo);
            if (tipo.equals("credito")) {
                if (numeroCuotas == null)
                    throw new IllegalArgumentException("Se requiere numeroCuotas para tipo credito");
                venta.setNumeroCuotas(numeroCuotas);
            }
            if (tipo.equals("contado")) {
                if (metodoPago == null)
                    throw new IllegalArgumentException("Se requiere metodoPago para tipo contado");
                venta.setMetodoPago(metodoPago);
            }
            venta.setEstado("pendiente");
            venta.setEstadoPago("pendiente");
            venta.setFecha(LocalDate.now());
            return save(venta);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear venta básica: " + e.getMessage());
        }
    }

    private String generarSiguienteNroVenta() {
        try {
            List<Venta> ventas = this.findAll();
            int maxNumero = 0;
            for (Venta v : ventas) {
                if (v.getNroVenta() != null && v.getNroVenta().startsWith("V-")) {
                    try {
                        int numero = Integer.parseInt(v.getNroVenta().substring(2));
                        if (numero > maxNumero) {
                            maxNumero = numero;
                        }
                    } catch (NumberFormatException e) {
                        // Ignorar si no es válido
                    }
                }
            }
            int siguiente = maxNumero + 1;
            return "V-" + String.format("%06d", siguiente);
        } catch (Exception e) {
            // En caso de error, usar un número por defecto
            return "V-000001";
        }
    }
}
