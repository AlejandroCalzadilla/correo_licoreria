package org.bebidas.modules.categorias.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.categorias.services.interfaces.CategoriaService;
import org.bebidas.modules.dao.interfaces.CategoriaDAO;

import java.util.List;
import java.util.Optional;


public class CategoriaServiceImpl extends GenericServiceImpl<Categoria, Long> implements CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaServiceImpl(CategoriaDAO categoriaDAO) {
        super(categoriaDAO);
        this.categoriaDAO = categoriaDAO;
    }

    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaDAO.buscarPorNombre(nombre);
    }

   
    public void desactivarCategoria(Long id) {
        Categoria categoria = findById(id).orElseThrow(
            () -> new RuntimeException("Categoría no encontrada con ID: " + id)
        );
        categoria.setActivo(false);
        save(categoria);
    }

    public void activarCategoria(Long id) {
        Categoria categoria = findById(id).orElseThrow(
            () -> new RuntimeException("Categoría no encontrada con ID: " + id)
        );
        categoria.setActivo(true);
        save(categoria);
    }

    public void deleteById(Long id) {
        Categoria categoria = findById(id).orElseThrow(
            () -> new RuntimeException("Categoría no encontrada con ID: " + id)
        );
        
       /*  // Verificar si la categoría tiene productos asociados
        if (!categoria.getProductos().isEmpty()) {
            throw new IllegalStateException(
                "No se puede eliminar la categoría porque tiene productos asociados"
            );
        } */
        
        // Verificar si tiene subcategorías
       // List<Categoria> subcategorias = buscarSubcategorias(id);
      
        
        super.delete(id);
    }
}
