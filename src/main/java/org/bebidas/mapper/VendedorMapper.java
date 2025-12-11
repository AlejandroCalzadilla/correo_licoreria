package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.Vendedor;
import org.bebidas.util.TableMapper;

public class VendedorMapper {
    
     private static final TableMapper<Vendedor> tableMapper = new TableMapper<>(Vendedor.class)
            .addColumn("ci", "CI", 15)
            .addColumn("nombre", "Nombre", 30);
    
    public static String obtenerTodosTable(List<Vendedor> vendedores) {
        return tableMapper.obtenerTodosTable(vendedores);
    }
    
    public static String obtenerUnoTable(Vendedor vendedor) {
        return tableMapper.obtenerUnoTable(vendedor);
    }
}