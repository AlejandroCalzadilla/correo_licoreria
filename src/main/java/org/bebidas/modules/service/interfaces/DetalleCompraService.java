package org.bebidas.modules.service.interfaces;

import java.util.List;

import org.bebidas.modules.compras.models.DetalleCompra;

public interface DetalleCompraService extends GenericService<DetalleCompra, Long> {
    List<DetalleCompra> buscarPorCompra(Long compraId);
    List<DetalleCompra> buscarPorProducto(Long productoId);
    
    DetalleCompra insertar(DetalleCompra detalle);
    DetalleCompra actualizar(DetalleCompra detalle);
    void eliminar(Long id);
}