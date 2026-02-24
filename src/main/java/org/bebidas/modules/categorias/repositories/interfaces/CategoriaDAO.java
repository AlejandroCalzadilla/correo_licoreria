package org.bebidas.modules.categorias.repositories.interfaces;

import java.util.Optional;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.categorias.Categoria;

public interface CategoriaDAO extends GenericDAO<Categoria, Long> {
    Optional<Categoria> buscarPorNombre(String nombre);
    
}
