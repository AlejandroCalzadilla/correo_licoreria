package org.bebidas.service.impl;

import org.bebidas.dao.interfaces.DetalleVentaDAO;
import org.bebidas.model.DetalleVenta;
import org.bebidas.model.Inventario;
import org.bebidas.service.interfaces.DetalleVentaService;
import org.bebidas.service.interfaces.InventarioService;

import java.math.BigDecimal;
import java.util.List;

public class DetalleVentaServiceImpl extends GenericServiceImpl<DetalleVenta, Long> implements DetalleVentaService {

    private final DetalleVentaDAO detalleVentaDAO;
    private final InventarioService inventarioService;

    public DetalleVentaServiceImpl(DetalleVentaDAO detalleVentaDAO, InventarioService inventarioService) {
        super(detalleVentaDAO);
        this.detalleVentaDAO = detalleVentaDAO;
        this.inventarioService = inventarioService;
    }

    @Override
    public List<DetalleVenta> buscarPorVenta(Long ventaId) {
        return detalleVentaDAO.buscarPorVenta(ventaId);
    }

    @Override
    public List<DetalleVenta> buscarPorProducto(Long productoId) {
        return detalleVentaDAO.buscarPorProducto(productoId);
    }

    @Override
    public BigDecimal obtenerTotalVentasPorProducto(Long productoId) {
        return detalleVentaDAO.obtenerTotalVentasPorProducto(productoId);
    }

    @Override
    public Integer obtenerCantidadVendidaPorProducto(Long productoId) {
        return detalleVentaDAO.obtenerCantidadVendidaPorProducto(productoId);
    }

    @Override
    public DetalleVenta insertar(DetalleVenta detalle) {
        // Verificar stock disponible
        Integer stockActual = inventarioService.obtenerStockActual(detalle.getProducto().getId());
        if (stockActual < detalle.getCantidad()) {
            throw new IllegalArgumentException("No hay suficiente stock para el producto " + detalle.getProducto().getNombre());
        }

        // Registrar salida de inventario
        Inventario inventario = new Inventario();
        inventario.setProducto(detalle.getProducto());
        inventario.setCantidad(detalle.getCantidad());
        inventarioService.registrarSalida(inventario);

        return detalleVentaDAO.save(detalle);
    }

    @Override
    public DetalleVenta actualizar(DetalleVenta detalle) {
        DetalleVenta detalleActual = this.findById(detalle.getId())
            .orElseThrow(() -> new IllegalArgumentException("El detalle de venta con ID " + detalle.getId() + " no existe"));
        
        Integer stockActual = inventarioService.obtenerStockActual(detalle.getProducto().getId());
        Integer difference = detalle.getCantidad() - detalleActual.getCantidad();
        
        if (stockActual < difference) {
            throw new IllegalArgumentException("No hay suficiente stock para actualizar el detalle de venta");
        }

        // Ajustar inventario
        Inventario inventario = new Inventario();
        inventario.setProducto(detalle.getProducto());
        inventario.setCantidad(Math.abs(difference));
        if (difference > 0) {
            inventarioService.registrarSalida(inventario);
        } else if (difference < 0) {
            inventarioService.registrarEntrada(inventario);
        }

        return detalleVentaDAO.save(detalle);
    }

    @Override
    public void eliminar(Long id) {
        DetalleVenta detalleActual = this.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("El detalle de venta con ID " + id + " no existe"));

        // Registrar entrada para devolver el stock
        Inventario inventario = new Inventario();
        inventario.setProducto(detalleActual.getProducto());
        inventario.setCantidad(detalleActual.getCantidad());
        inventarioService.registrarEntrada(inventario);

        detalleVentaDAO.delete(id);
    }
}