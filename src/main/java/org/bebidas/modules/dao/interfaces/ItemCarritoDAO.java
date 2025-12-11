package org.bebidas.modules.dao.interfaces;

import java.util.List;

import org.bebidas.modules.carrito.ItemCarrito;

public interface ItemCarritoDAO extends GenericDAO<ItemCarrito, Long> {
    List<ItemCarrito> buscarPorCarrito(Long carritoId);
    List<ItemCarrito> buscarPorProducto(Long productoId);
    boolean existeProductoEnCarrito(Long carritoId, Long productoId);
    void actualizarCantidad(Long itemId, int nuevaCantidad);
    void eliminarPorCarrito(Long carritoId);
}
