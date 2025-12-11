package org.bebidas.modules.service.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.modules.creditos.Credito;

public interface CreditoService extends GenericService<Credito, Long> {
    List<Credito> buscarPorVenta(Long ventaId);
    List<Credito> buscarPorEstado(String estado);
    List<Credito> buscarPorCliente(Long clienteId);
    List<Credito> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    BigDecimal obtenerSaldoPendientePorCliente(Long clienteId);
    List<Credito> buscarCreditosVencidos();
    Credito generarCredito(Credito credito);
    void registrarPago(Long creditoId, BigDecimal monto);
    void marcarComoVencido(Long creditoId);
}
