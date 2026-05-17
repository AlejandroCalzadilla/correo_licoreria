package org.bebidas.modules.carrito.mappers;

import java.util.List;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.carrito.Carrito;

public class CarritoMapper {

    private static final TableMapper<Carrito> tableMapper = new TableMapper<>(Carrito.class)
            .addColumn("id", "ID", 10)
            .addColumn("usuarioId", "Usuario ID", 12)
            .addColumn("usuarioNombre", "Usuario", 20)
            .addColumn("createdAt", "Creado", 15)
            .addColumn("updatedAt", "Actualizado", 15);

    public static String obtenerTodosTable(List<Carrito> carritos) {
        return tableMapper.obtenerTodosTable(carritos);
    }

    public static String obtenerUnoTable(Carrito carrito) {
        return tableMapper.obtenerUnoTable(carrito);
    }
}