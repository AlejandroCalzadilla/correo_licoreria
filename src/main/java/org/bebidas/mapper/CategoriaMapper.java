package org.bebidas.mapper;

import org.bebidas.model.Categoria;
import org.bebidas.util.TableMapper;

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
