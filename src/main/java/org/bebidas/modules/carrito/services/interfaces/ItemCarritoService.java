package org.bebidas.modules.carrito.services.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.bebidas.modules.carrito.ItemCarrito;
import org.bebidas.modules.service.interfaces.GenericService;

public interface ItemCarritoService extends GenericService<ItemCarrito, Long> {
    List<ItemCarrito> buscarPorCarrito(Long carritoId);
    List<ItemCarrito> buscarPorProducto(Long productoId);
    ItemCarrito agregarItem(ItemCarrito item);
    void actualizarCantidad(Long itemId, int nuevaCantidad);
    void eliminarItem(Long itemId);
    void vaciarCarrito(Long carritoId);
    BigDecimal calcularSubtotal(Long itemId);
}
