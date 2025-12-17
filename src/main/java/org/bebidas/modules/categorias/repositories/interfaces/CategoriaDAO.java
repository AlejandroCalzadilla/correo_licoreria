package org.bebidas.modules.categorias.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.dao.interfaces.GenericDAO;

public interface CategoriaDAO extends GenericDAO<Categoria, Long> {
    Optional<Categoria> buscarPorNombre(String nombre);
    
}
