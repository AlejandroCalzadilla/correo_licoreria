package org.bebidas.mapper;

import java.util.List;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.compras.DetalleCompra;

public class DetalleCompraMapper {
    
     private static final TableMapper<DetalleCompra> tableMapper = new TableMapper<>(DetalleCompra.class)
            .addColumn("cantidad", "Cantidad", 10)
            .addColumn("precioUnitario", "Precio Unit.", 15)
            .addColumn("subtotal", "Subtotal", 15);
    
    public static String obtenerTodosTable(List<DetalleCompra> detalles) {
        return tableMapper.obtenerTodosTable(detalles);
    }
    
    public static String obtenerUnoTable(DetalleCompra detalle) {
        return tableMapper.obtenerUnoTable(detalle);
    }
}