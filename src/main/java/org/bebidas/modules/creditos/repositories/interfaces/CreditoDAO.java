package org.bebidas.modules.creditos.repositories.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.creditos.Credito;

public interface CreditoDAO extends GenericDAO<Credito, Long> {
    List<Credito> buscarPorVenta(Long ventaId);
    List<Credito> buscarPorEstado(String estado);
    List<Credito> buscarPorCliente(Long clienteId);
    List<Credito> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    BigDecimal obtenerSaldoPendientePorCliente(Long clienteId);
    List<Credito> buscarCreditosVencidos();
}
