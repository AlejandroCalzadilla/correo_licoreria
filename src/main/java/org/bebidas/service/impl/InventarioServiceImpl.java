package org.bebidas.service.impl;

import org.bebidas.dao.interfaces.InventarioDAO;
import org.bebidas.dao.interfaces.ProductoDAO;
import org.bebidas.model.Inventario;
import org.bebidas.model.Producto;
import org.bebidas.service.interfaces.InventarioService;

import java.time.LocalDate;
import java.util.List;

public class InventarioServiceImpl extends GenericServiceImpl<Inventario, Long> implements InventarioService {

    private final InventarioDAO inventarioDAO;
    private final ProductoDAO productoDAO;

    public InventarioServiceImpl(InventarioDAO inventarioDAO, ProductoDAO productoDAO) {
        super(inventarioDAO);
        this.inventarioDAO = inventarioDAO;
        this.productoDAO = productoDAO;
    }

    @Override
    public List<Inventario> buscarPorProducto(Long productoId) {
        return inventarioDAO.buscarPorProducto(productoId);
    }

    @Override
    public List<Inventario> buscarPorTipoMovimiento(String tipoMovimiento) {
        return inventarioDAO.buscarPorTipoMovimiento(tipoMovimiento);
    }

    @Override
    public List<Inventario> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return inventarioDAO.buscarPorRangoFechas(inicio, fin);
    }

    @Override
    public List<Inventario> buscarPorUsuario(Long usuarioId) {
        return inventarioDAO.buscarPorUsuario(usuarioId);
    }

    @Override
    public Integer obtenerStockActual(Long productoId) {
        return inventarioDAO.obtenerStockActual(productoId);
    }

    @Override
    public void registrarEntrada(Inventario movimiento) {
        validarMovimiento(movimiento);
        movimiento.setTipoMovimiento("INGRESO");
        movimiento.setFecha(LocalDate.now());
        
        // Actualizar stock actual
        int stockActual = obtenerStockActual(movimiento.getProducto().getId());
        movimiento.setStockActual(stockActual + movimiento.getCantidad());
        
        save(movimiento);
    }

    @Override
    public void registrarSalida(Inventario movimiento) {
        validarMovimiento(movimiento);
        movimiento.setTipoMovimiento("SALIDA");
        movimiento.setFecha(LocalDate.now());
        
        // Verificar stock disponible
        int stockActual = obtenerStockActual(movimiento.getProducto().getId());
        if (stockActual < movimiento.getCantidad()) {
            throw new IllegalStateException("Stock insuficiente para realizar la salida");
        }
        
        movimiento.setStockActual(stockActual - movimiento.getCantidad());
        save(movimiento);
    }

    @Override
    public void ajustarInventario(Inventario movimiento, String motivo) {
        validarMovimiento(movimiento);
        movimiento.setTipoMovimiento("AJUSTE");
        movimiento.setFecha(LocalDate.now());
        movimiento.setGlosa("Ajuste de inventario: " + motivo);
        
        // El stock actual se establece directamente en el movimiento
        save(movimiento);
    }

    private void validarMovimiento(Inventario movimiento) {
        if (movimiento.getProducto() == null || movimiento.getProducto().getId() == null) {
            throw new IllegalArgumentException("El producto es requerido");
        }
        
        if (movimiento.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        
        // Verificar que el producto existe
        Producto producto = productoDAO.findById(movimiento.getProducto().getId())
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        
        movimiento.setProducto(producto);
    }
}
