package org.bebidas.modules.dao.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.bebidas.modules.ventas.DetalleVenta;

public interface DetalleVentaDAO extends GenericDAO<DetalleVenta, Long> {
    List<DetalleVenta> buscarPorVenta(Long ventaId);
    List<DetalleVenta> buscarPorProducto(Long productoId);
    BigDecimal obtenerTotalVentasPorProducto(Long productoId);
    Integer obtenerCantidadVendidaPorProducto(Long productoId);
}
