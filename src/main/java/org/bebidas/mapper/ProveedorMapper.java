package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.Proveedor;
import org.bebidas.util.TableMapper;

public class ProveedorMapper {
    
     private static final TableMapper<Proveedor> tableMapper = new TableMapper<>(Proveedor.class)
            .addColumn("nombre", "Nombre", 30)
            .addColumn("telefono", "Teléfono", 15)
            .addColumn("direccion", "Dirección", 35);
    
    public static String obtenerTodosTable(List<Proveedor> proveedores) {
        return tableMapper.obtenerTodosTable(proveedores);
    }
    
    public static String obtenerUnoTable(Proveedor proveedor) {
        return tableMapper.obtenerUnoTable(proveedor);
    }
}