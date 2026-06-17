package org.bebidas.modules.service.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.modules.pagos.Pago;

public interface PagoService extends GenericService<Pago, Long> {
    List<Pago> buscarPorVenta(Long ventaId);

    List<Pago> buscarPorEstado(String estado);

    List<Pago> buscarPorTipoPago(String tipoPago);

    List<Pago> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);

    List<Pago> buscarPorCliente(Long clienteId);

    BigDecimal obtenerTotalPagosPorVenta(Long ventaId);

    Pago registrarPago(Pago pago);

    Pago procesarPagoVenta(Long ventaId, String tipoPago, BigDecimal monto, String nombrePersona, String email);

    void anularPago(Long pagoId);
}
