package org.bebidas.modules.ventas.mappers;

import java.util.List;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.ventas.models.DetalleVenta;

public class DetalleVentaMapper {

    private static final TableMapper<DetalleVenta> tableMapper = new TableMapper<>(DetalleVenta.class)
            .addColumn("venta_id", "VentaId", 10)
            .addColumn("cantidad", "Cantidad", 10)
            .addColumn("precioUnitario", "Precio Unit.", 15)
            .addColumn("subtotal", "Subtotal", 15);

    public static String obtenerTodosTable(List<DetalleVenta> detalles) {
        return tableMapper.obtenerTodosTable(detalles);
    }

    public static String obtenerUnoTable(DetalleVenta detalle) {
        return tableMapper.obtenerUnoTable(detalle);
    }
}