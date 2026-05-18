package org.bebidas.modules.compras.repositories.interfaces;

import java.util.List;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.compras.models.DetalleCompra;

public interface DetalleCompraDAO extends GenericDAO<DetalleCompra, Long> {
    List<DetalleCompra> buscarPorCompra(Long compraId);
    List<DetalleCompra> buscarPorProducto(Long productoId);
    
    DetalleCompra insertar(DetalleCompra detalle);
    DetalleCompra actualizar(DetalleCompra detalle);
    void eliminar(Long id);
}