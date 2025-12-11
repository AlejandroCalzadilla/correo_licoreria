package org.bebidas.dao.interfaces;

import org.bebidas.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaDAO extends GenericDAO<Categoria, Long> {
    Optional<Categoria> buscarPorNombre(String nombre);
    
}
