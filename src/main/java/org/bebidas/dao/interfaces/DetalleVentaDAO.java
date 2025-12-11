package org.bebidas.dao.interfaces;

import org.bebidas.model.DetalleVenta;

import java.math.BigDecimal;
import java.util.List;

public interface DetalleVentaDAO extends GenericDAO<DetalleVenta, Long> {
    List<DetalleVenta> buscarPorVenta(Long ventaId);
    List<DetalleVenta> buscarPorProducto(Long productoId);
    BigDecimal obtenerTotalVentasPorProducto(Long productoId);
    Integer obtenerCantidadVendidaPorProducto(Long productoId);
}
