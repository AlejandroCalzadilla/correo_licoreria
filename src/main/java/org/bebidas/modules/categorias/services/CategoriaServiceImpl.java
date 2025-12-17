package org.bebidas.modules.categorias.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.categorias.repositories.interfaces.CategoriaDAO;
import org.bebidas.modules.categorias.services.interfaces.CategoriaService;
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


    public void deleteById(Long id) {
        Categoria categoria = findById(id).orElseThrow(
            () -> new RuntimeException("Categoría no encontrada con ID: " + id)
        );
        super.delete(id);
    }
}
