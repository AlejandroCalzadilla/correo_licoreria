package org.bebidas.modules.dao.interfaces;

import java.util.List;
import java.util.Optional;

import org.bebidas.modules.categorias.Categoria;

public interface CategoriaDAO extends GenericDAO<Categoria, Long> {
    Optional<Categoria> buscarPorNombre(String nombre);
    
}
