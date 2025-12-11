package org.bebidas.modules.dao.interfaces;

import java.time.LocalDate;
import java.util.List;

import org.bebidas.modules.inventario.Inventario;

public interface InventarioDAO extends GenericDAO<Inventario, Long> {
    List<Inventario> buscarPorProducto(Long productoId);
    List<Inventario> buscarPorTipoMovimiento(String tipoMovimiento);
    List<Inventario> buscarPorRangoFechas(LocalDate inicio, LocalDate fin);
    List<Inventario> buscarPorUsuario(Long usuarioId);
    Integer obtenerStockActual(Long productoId);
}
