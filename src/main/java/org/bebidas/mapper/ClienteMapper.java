package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.Cliente;
import org.bebidas.util.TableMapper;

public class ClienteMapper {
    
     private static final TableMapper<Cliente> tableMapper = new TableMapper<>(Cliente.class)
            .addColumn("ci", "CI", 15)
            .addColumn("nombre", "Nombre", 30)
            .addColumn("telefono", "Teléfono", 15)
            .addColumn("direccion", "Dirección", 35)
            .addColumn("estado", "Estado", 10)
            .addColumn("estadoVerificacion", "Verificación", 20);
    
    public static String obtenerTodosTable(List<Cliente> clientes) {
        return tableMapper.obtenerTodosTable(clientes);
    }
    
    public static String obtenerUnoTable(Cliente cliente) {
        return tableMapper.obtenerUnoTable(cliente);
    }
}
