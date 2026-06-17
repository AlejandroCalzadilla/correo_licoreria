package org.bebidas.modules.ventas.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.ventas.models.DetalleVenta;

public interface DetalleVentaDAO extends GenericDAO<DetalleVenta, Long> {
    List<DetalleVenta> buscarPorVenta(Long ventaId);

    List<DetalleVenta> buscarPorProducto(Long productoId);

    BigDecimal obtenerTotalVentasPorProducto(Long productoId);

    Integer obtenerCantidadVendidaPorProducto(Long productoId);
}
