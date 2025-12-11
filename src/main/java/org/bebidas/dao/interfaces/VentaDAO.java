package org.bebidas.dao.interfaces;

import org.bebidas.model.Venta;

import java.time.LocalDate;
import java.util.List;

public interface VentaDAO extends GenericDAO<Venta, Long> {
    List<Venta> buscarPorFecha(LocalDate fecha);
    List<Venta> buscarPorCliente(Long clienteId);
    List<Venta> buscarPorEstado(String estado);
    List<Venta> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    List<Venta> buscarPorUsuario(Long usuarioId);
}
