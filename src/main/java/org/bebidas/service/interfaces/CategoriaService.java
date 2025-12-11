package org.bebidas.service.interfaces;

import org.bebidas.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService extends GenericService<Categoria, Long> {
    Optional<Categoria> buscarPorNombre(String nombre);
   
    void desactivarCategoria(Long id);
    void activarCategoria(Long id);
}
