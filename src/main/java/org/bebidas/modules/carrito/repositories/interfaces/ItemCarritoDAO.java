package org.bebidas.modules.carrito.repositories.interfaces;

import java.util.List;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.carrito.ItemCarrito;

public interface ItemCarritoDAO extends GenericDAO<ItemCarrito, Long> {
    List<ItemCarrito> buscarPorCarrito(Long carritoId);
    List<ItemCarrito> buscarPorProducto(Long productoId);
    boolean existeProductoEnCarrito(Long carritoId, Long productoId);
    void actualizarCantidad(Long itemId, int nuevaCantidad);
    void eliminarPorCarrito(Long carritoId);
}
