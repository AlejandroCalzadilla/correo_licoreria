package org.bebidas.service.interfaces;

import org.bebidas.model.DetalleVenta;

import java.math.BigDecimal;
import java.util.List;

public interface DetalleVentaService extends GenericService<DetalleVenta, Long> {
    List<DetalleVenta> buscarPorVenta(Long ventaId);
    List<DetalleVenta> buscarPorProducto(Long productoId);
    BigDecimal obtenerTotalVentasPorProducto(Long productoId);
    Integer obtenerCantidadVendidaPorProducto(Long productoId);
    
    DetalleVenta insertar(DetalleVenta detalle);
    DetalleVenta actualizar(DetalleVenta detalle);
    void eliminar(Long id);
}