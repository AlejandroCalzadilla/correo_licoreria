package org.bebidas.modules.categorias.mappers;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.categorias.Categoria;

import java.util.List;

public class CategoriaMapper {
    
    private static final TableMapper<Categoria> tableMapper = new TableMapper<>(Categoria.class)
            .addColumn("id", "ID", 10)
            .addColumn("nombre", "Nombre", 25)
            .addColumn("descripcion", "Descripción", 35)
            .addColumn("activo", "Activo", 10)
            .addColumn("tipo", "Tipo", 15);
    
    public static String obtenerTodosTable(List<Categoria> categorias) {
        return tableMapper.obtenerTodosTable(categorias);
    }
    
    public static String obtenerUnoTable(Categoria categoria) {
        return tableMapper.obtenerUnoTable(categoria);
    }
}
