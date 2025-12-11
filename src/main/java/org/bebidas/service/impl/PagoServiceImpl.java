package org.bebidas.service.impl;

import org.bebidas.dao.interfaces.PagoDAO;
import org.bebidas.model.Pago;
import org.bebidas.model.Venta;
import org.bebidas.service.interfaces.PagoService;
import org.bebidas.service.interfaces.VentaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PagoServiceImpl extends GenericServiceImpl<Pago, Long> implements PagoService {

    private final PagoDAO pagoDAO;
    private VentaService ventaService;

    public PagoServiceImpl(PagoDAO pagoDAO) {
        super(pagoDAO);
        this.pagoDAO = pagoDAO;
    }

    public void setVentaService(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Override
    public List<Pago> buscarPorVenta(Long ventaId) {
        return pagoDAO.buscarPorVenta(ventaId);
    }

    @Override
    public List<Pago> buscarPorEstado(String estado) {
        return pagoDAO.buscarPorEstado(estado);
    }

    @Override
    public List<Pago> buscarPorTipoPago(String tipoPago) {
        return pagoDAO.buscarPorTipoPago(tipoPago);
    }

    @Override
    public List<Pago> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return pagoDAO.buscarPorRangoFechas(inicio, fin);
    }

    @Override
    public List<Pago> buscarPorCliente(Long clienteId) {
        return pagoDAO.buscarPorCliente(clienteId);
    }

    @Override
    public BigDecimal obtenerTotalPagosPorVenta(Long ventaId) {
        return pagoDAO.obtenerTotalPagosPorVenta(ventaId);
    }

    @Override
    public Pago registrarPago(Pago pago) {
        // Validar que la venta existe
        Venta venta = ventaService.findById(pago.getVenta().getId())
            .orElseThrow(() -> new IllegalArgumentException("Venta no encontrada"));
        
        // Validar que el monto no sea negativo
        if (pago.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor a cero");
        }
        if( pago.getMonto() != venta.getSaldo() ){
            throw new IllegalArgumentException("El monto del pago no puede ser diferente al saldo de la venta");
        }
        
        // Establecer fecha y estado por defecto
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado("PAGO_COMPLETADO");
        // Guardar el pago
        Pago pagoGuardado = save(pago);
        // Actualizar el estado de la venta si es necesario
        actualizarEstadoVenta(venta);

        return pagoGuardado;
    }

    @Override
    public void anularPago(Long pagoId) {
        Pago pago = findById(pagoId)
            .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));
        
        // Solo se pueden anular pagos pendientes o confirmados
        if (!pago.getEstado().equals("PAGO_COMPLETADO") && !pago.getEstado().equals("CONFIRMADO")) {
            throw new IllegalStateException("Solo se pueden anular pagos pendientes o confirmados");
        }
        
        // Marcar como anulado
        pago.setEstado("ANULADO");
        save(pago);
        
        // Actualizar el estado de la venta
        Venta venta = pago.getVenta();
        actualizarEstadoVenta(venta);
    }
    
    private void actualizarEstadoVenta(Venta venta) {
        BigDecimal totalVenta = venta.getMontoTotal();
        BigDecimal totalPagado = obtenerTotalPagosPorVenta(venta.getId());
        
        String nuevoEstado;
        if (totalPagado.compareTo(BigDecimal.ZERO) == 0) {
            nuevoEstado = "PENDIENTE_PAGO";
        } else if (totalPagado.compareTo(totalVenta) < 0) {
            nuevoEstado = "PAGO_PARCIAL";
        } else if (totalPagado.compareTo(totalVenta) == 0) {
            nuevoEstado = "PAGO_COMPLETO";
        } else {
            nuevoEstado = "PAGO_EXCEDENTE";
        }
        
        if (!venta.getEstado().equals(nuevoEstado)) {
            venta.setEstado(nuevoEstado);
            ventaService.save(venta);
        }
    }
}
