package org.bebidas.dao.interfaces;

import org.bebidas.model.Producto;

import java.util.List;

public interface ProductoDAO extends GenericDAO<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorMarca(String marca);
    List<Producto> buscarPorPrecioMenorIgual(Double precioMaximo);
    List<Producto> buscarConStockDisponible();
}
