package org.bebidas.modules.compras.mappers;

import java.util.List;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.compras.Compra;

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