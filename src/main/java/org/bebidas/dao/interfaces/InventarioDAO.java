package org.bebidas.dao.interfaces;

import org.bebidas.model.Inventario;

import java.time.LocalDate;
import java.util.List;

public interface InventarioDAO extends GenericDAO<Inventario, Long> {
    List<Inventario> buscarPorProducto(Long productoId);
    List<Inventario> buscarPorTipoMovimiento(String tipoMovimiento);
    List<Inventario> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    List<Inventario> buscarPorUsuario(Long usuarioId);
    Integer obtenerStockActual(Long productoId);
}
