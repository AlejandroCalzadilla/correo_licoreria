package org.bebidas.modules.dao.interfaces;

import java.util.List;

import org.bebidas.modules.compras.DetalleCompra;

public interface DetalleCompraDAO extends GenericDAO<DetalleCompra, Long> {
    List<DetalleCompra> buscarPorCompra(Long compraId);
    List<DetalleCompra> buscarPorProducto(Long productoId);
    
    DetalleCompra insertar(DetalleCompra detalle);
    DetalleCompra actualizar(DetalleCompra detalle);
    void eliminar(Long id);
}