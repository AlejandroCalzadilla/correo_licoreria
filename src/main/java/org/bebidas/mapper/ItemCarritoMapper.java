package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.ItemCarrito;
import org.bebidas.util.TableMapper;

public class ItemCarritoMapper {
    
     private static final TableMapper<ItemCarrito> tableMapper = new TableMapper<>(ItemCarrito.class)
            .addColumn("cantidad", "Cantidad", 10)
            .addColumn("precio", "Precio", 12);
    
    public static String obtenerTodosTable(List<ItemCarrito> items) {
        return tableMapper.obtenerTodosTable(items);
    }
    
    public static String obtenerUnoTable(ItemCarrito item) {
        return tableMapper.obtenerUnoTable(item);
    }
}