package org.bebidas.modules.categorias.services.interfaces;

import java.util.Optional;
import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.service.interfaces.GenericService;

public interface CategoriaService extends GenericService<Categoria, Long> {
    Optional<Categoria> buscarPorNombre(String nombre);
   
   
}
