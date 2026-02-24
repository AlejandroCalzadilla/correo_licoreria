package org.bebidas.modules.inventario.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.categorias.services.interfaces.CategoriaService;
import org.bebidas.modules.inventario.Producto;
import org.bebidas.modules.inventario.repositories.interfaces.ProductoDAO;
import org.bebidas.modules.service.interfaces.ProductoService;

import java.math.BigDecimal;
import java.util.List;

public class ProductoServiceImpl extends GenericServiceImpl<Producto, Long> implements ProductoService {

    private final ProductoDAO productoDAO;
    private final CategoriaService categoriaService;

    public ProductoServiceImpl(ProductoDAO productoDAO, CategoriaService categoriaService) {
        super(productoDAO);
        this.productoDAO = productoDAO;
        this.categoriaService = categoriaService;
    }

    @Override
    public Producto save(Producto producto) {
        // Validar que la categoría existe si está asignada
        if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
            categoriaService.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + producto.getCategoria().getId()));
        }
        
        return super.save(producto);
    }

    @Override
    public List<Producto> findByCategoriaId(Long categoriaId) {
        return productoDAO.findByCategoriaId(categoriaId);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoDAO.buscarPorNombre(nombre);
    }

    @Override
    public List<Producto> buscarPorMarca(String marca) {
        return productoDAO.buscarPorMarca(marca);
    }

    @Override
    public List<Producto> buscarPorPrecioMenorIgual(Double precioMaximo) {
        return productoDAO.buscarPorPrecioMenorIgual(precioMaximo);
    }

    @Override
    public List<Producto> buscarConStockDisponible() {
        return productoDAO.buscarConStockDisponible();
    }

   
    public void actualizarPrecio(Long productoId, BigDecimal nuevoPrecio) {
        if (nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        Producto producto = findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productoId));
        producto.setPrecio(nuevoPrecio);
        save(producto);
    }
}
