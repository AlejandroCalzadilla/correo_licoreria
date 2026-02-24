package org.bebidas.modules.ventas.repositories;

import java.time.LocalDate;
import java.util.List;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.ventas.Venta;

public interface VentaDAO extends GenericDAO<Venta, Long> {
    List<Venta> buscarPorFecha(LocalDate fecha);
    List<Venta> buscarPorCliente(Long clienteId);
    List<Venta> buscarPorEstado(String estado);
    List<Venta> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    List<Venta> buscarPorUsuario(Long usuarioId);
}
