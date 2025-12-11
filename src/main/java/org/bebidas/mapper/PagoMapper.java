package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.Pago;
import org.bebidas.util.TableMapper;

public class PagoMapper {
    
     private static final TableMapper<Pago> tableMapper = new TableMapper<>(Pago.class)
            .addColumn("nroPago", "Nro. Pago", 12)
            .addColumn("tipoPago", "Tipo", 12)
            .addColumn("estado", "Estado", 10)
            .addColumn("monto", "Monto", 12)
            .addColumn("fechaPago", "Fecha Pago", 15);
    
    public static String obtenerTodosTable(List<Pago> pagos) {
        return tableMapper.obtenerTodosTable(pagos);
    }
    
    public static String obtenerUnoTable(Pago pago) {
        return tableMapper.obtenerUnoTable(pago);
    }
}