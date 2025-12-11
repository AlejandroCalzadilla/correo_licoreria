package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.Compra;
import org.bebidas.util.TableMapper;

public class CompraMapper {
    
     private static final TableMapper<Compra> tableMapper = new TableMapper<>(Compra.class)
            .addColumn("nroCompra", "Nro. Compra", 15)
            .addColumn("fecha", "Fecha", 12)
            .addColumn("estado", "Estado", 12)
            .addColumn("descripcion", "Descripción", 40)
            .addColumn("proveedor", "Proveedor", 25);
    
    public static String obtenerTodosTable(List<Compra> compras) {
        return tableMapper.obtenerTodosTable(compras);
    }
    
    public static String obtenerUnoTable(Compra compra) {
        return tableMapper.obtenerUnoTable(compra);
    }
}