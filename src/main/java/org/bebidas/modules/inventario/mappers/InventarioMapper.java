package org.bebidas.modules.inventario.mappers;

import java.util.List;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.inventario.Inventario;

public class InventarioMapper {

    private static final TableMapper<Inventario> tableMapper = new TableMapper<>(Inventario.class)
            .addColumn("id", "ID", 10)
            .addColumn("tipoMovimiento", "Tipo Mov.", 15)
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