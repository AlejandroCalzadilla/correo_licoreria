package org.bebidas.modules.inventario.mappers;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.inventario.Producto;

import java.util.List;

public class ProductoMapper {
    
    private static final TableMapper<Producto> tableMapper = new TableMapper<>(Producto.class)
            .addColumn("id", "ID", 10)
            .addColumn("nombre", "Nombre", 30)
            .addColumn("descripcion", "Descripción", 40)
            .addColumn("marca", "Marca", 20)
            .addColumn("precio", "Precio", 12);
    
    public static String obtenerTodosTable(List<Producto> productos) {
        return tableMapper.obtenerTodosTable(productos);
    }
    
    public static String obtenerUnoTable(Producto producto) {
        return tableMapper.obtenerUnoTable(producto);
    }
}
