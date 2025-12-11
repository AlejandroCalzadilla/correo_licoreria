package org.bebidas.dao.interfaces;

import org.bebidas.model.ItemCarrito;

import java.util.List;

public interface ItemCarritoDAO extends GenericDAO<ItemCarrito, Long> {
    List<ItemCarrito> buscarPorCarrito(Long carritoId);
    List<ItemCarrito> buscarPorProducto(Long productoId);
    boolean existeProductoEnCarrito(Long carritoId, Long productoId);
    void actualizarCantidad(Long itemId, int nuevaCantidad);
    void eliminarPorCarrito(Long carritoId);
}
