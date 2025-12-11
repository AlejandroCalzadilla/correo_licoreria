package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.Carrito;
import org.bebidas.util.TableMapper;

public class CarritoMapper {
    
     private static final TableMapper<Carrito> tableMapper = new TableMapper<>(Carrito.class)
            .addColumn("sessionId", "Session ID", 20)
            .addColumn("createdAt", "Creado", 15)
            .addColumn("updatedAt", "Actualizado", 15);
    
    public static String obtenerTodosTable(List<Carrito> carritos) {
        return tableMapper.obtenerTodosTable(carritos);
    }
    
    public static String obtenerUnoTable(Carrito carrito) {
        return tableMapper.obtenerUnoTable(carrito);
    }
}