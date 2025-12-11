package org.bebidas.service.interfaces;

import org.bebidas.model.Producto;

import java.util.List;

public interface ProductoService extends GenericService<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorMarca(String marca);
    List<Producto> buscarPorPrecioMenorIgual(Double precioMaximo);
    List<Producto> buscarConStockDisponible();
}
