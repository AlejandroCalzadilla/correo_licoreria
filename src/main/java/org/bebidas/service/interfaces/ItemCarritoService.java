package org.bebidas.service.interfaces;

import org.bebidas.model.ItemCarrito;

import java.math.BigDecimal;
import java.util.List;

public interface ItemCarritoService extends GenericService<ItemCarrito, Long> {
    List<ItemCarrito> buscarPorCarrito(Long carritoId);
    List<ItemCarrito> buscarPorProducto(Long productoId);
    ItemCarrito agregarItem(ItemCarrito item);
    void actualizarCantidad(Long itemId, int nuevaCantidad);
    void eliminarItem(Long itemId);
    void vaciarCarrito(Long carritoId);
    BigDecimal calcularSubtotal(Long itemId);
}
