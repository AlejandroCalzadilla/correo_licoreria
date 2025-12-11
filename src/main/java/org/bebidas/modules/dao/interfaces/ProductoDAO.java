package org.bebidas.modules.dao.interfaces;

import java.util.List;

import org.bebidas.modules.inventario.Producto;

public interface ProductoDAO extends GenericDAO<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorMarca(String marca);
    List<Producto> buscarPorPrecioMenorIgual(Double precioMaximo);
    List<Producto> buscarConStockDisponible();
}
