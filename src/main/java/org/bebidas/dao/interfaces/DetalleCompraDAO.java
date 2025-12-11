package org.bebidas.dao.interfaces;

import org.bebidas.model.DetalleCompra;

import java.util.List;

public interface DetalleCompraDAO extends GenericDAO<DetalleCompra, Long> {
    List<DetalleCompra> buscarPorCompra(Long compraId);
    List<DetalleCompra> buscarPorProducto(Long productoId);
    
    DetalleCompra insertar(DetalleCompra detalle);
    DetalleCompra actualizar(DetalleCompra detalle);
    void eliminar(Long id);
}