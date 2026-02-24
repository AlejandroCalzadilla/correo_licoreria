package org.bebidas.modules.ventas;

import java.util.List;

import org.bebidas.core.util.TableMapper;

public class VentaMapper {

    private static final TableMapper<Venta> tableMapper = new TableMapper<>(Venta.class)
            .addColumn("id", "ID", 10)
            .addColumn("fecha", "Fecha", 12)
            .addColumn("estado", "Estado", 12)
            .addColumn("montoTotal", "Monto Total", 15)
            .addColumn("saldo", "Saldo", 15)
            .addColumn("numeroCuotas", "Número de Cuotas", 15);

    public static String obtenerTodosTable(List<Venta> ventas) {
        return tableMapper.obtenerTodosTable(ventas);
    }

    public static String obtenerUnoTable(Venta venta) {
        return tableMapper.obtenerUnoTable(venta);
    }
}