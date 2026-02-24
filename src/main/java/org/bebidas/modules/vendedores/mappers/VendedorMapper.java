package org.bebidas.modules.vendedores.mappers;

import java.util.List;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.vendedores.Vendedor;

public class VendedorMapper {
    
     private static final TableMapper<Vendedor> tableMapper = new TableMapper<>(Vendedor.class)
             .addColumn("id", "ID", 10)
            .addColumn("ci", "CI", 15)
            .addColumn("nombre", "Nombre", 30);
    
    public static String obtenerTodosTable(List<Vendedor> vendedores) {
        return tableMapper.obtenerTodosTable(vendedores);
    }
    
    public static String obtenerUnoTable(Vendedor vendedor) {
        return tableMapper.obtenerUnoTable(vendedor);
    }
}