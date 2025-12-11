package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.Inventario;
import org.bebidas.util.TableMapper;

public class InventarioMapper {
    
     private static final TableMapper<Inventario> tableMapper = new TableMapper<>(Inventario.class)
            .addColumn("tipoMovimiento", "Tipo Mov.", 12)
            .addColumn("cantidad", "Cantidad", 10)
            .addColumn("fecha", "Fecha", 12)
            .addColumn("stockActual", "Stock Actual", 15)
            .addColumn("glosa", "Glosa", 25);
    
    public static String obtenerTodosTable(List<Inventario> inventarios) {
        return tableMapper.obtenerTodosTable(inventarios);
    }
    
    public static String obtenerUnoTable(Inventario inventario) {
        return tableMapper.obtenerUnoTable(inventario);
    }
}