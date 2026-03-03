package org.bebidas.modules.service.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.bebidas.modules.ventas.Venta;
import org.bebidas.modules.ventas.DetalleVenta;

public interface DetalleVentaService extends GenericService<DetalleVenta, Long> {
    List<DetalleVenta> buscarPorVenta(Long ventaId);
    List<DetalleVenta> buscarPorProducto(Long productoId);
    BigDecimal obtenerTotalVentasPorProducto(Long productoId);
    Integer obtenerCantidadVendidaPorProducto(Long productoId);
    
    DetalleVenta insertar(DetalleVenta detalle);
    DetalleVenta actualizar(DetalleVenta detalle);
    void eliminar(Long id);
    Venta procesarCreacionDetalleVenta(Long ventaId, Long productoId, Integer cantidad, BigDecimal precioUnitario);
}