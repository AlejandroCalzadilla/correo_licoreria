package org.bebidas.modules.service.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.modules.ventas.Pago;

public interface PagoService extends GenericService<Pago, Long> {
    List<Pago> buscarPorVenta(Long ventaId);
    List<Pago> buscarPorEstado(String estado);
    List<Pago> buscarPorTipoPago(String tipoPago);
    List<Pago> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    List<Pago> buscarPorCliente(Long clienteId);
    BigDecimal obtenerTotalPagosPorVenta(Long ventaId);
    Pago registrarPago(Pago pago);
    void anularPago(Long pagoId);
}
