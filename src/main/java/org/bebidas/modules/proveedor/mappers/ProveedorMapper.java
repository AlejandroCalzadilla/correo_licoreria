package org.bebidas.modules.proveedor.mappers;

import java.util.List;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.proveedor.Proveedor;

public class ProveedorMapper {
    
     private static final TableMapper<Proveedor> tableMapper = new TableMapper<>(Proveedor.class)
            .addColumn("id", "ID", 10)
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