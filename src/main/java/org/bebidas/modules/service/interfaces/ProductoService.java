package org.bebidas.modules.service.interfaces;

import java.util.List;

import org.bebidas.modules.inventario.Producto;

public interface ProductoService extends GenericService<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorMarca(String marca);
    List<Producto> buscarPorPrecioMenorIgual(Double precioMaximo);
    List<Producto> buscarConStockDisponible();
}
