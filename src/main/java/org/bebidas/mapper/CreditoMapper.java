package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.Credito;
import org.bebidas.util.TableMapper;

public class CreditoMapper {
    
     private static final TableMapper<Credito> tableMapper = new TableMapper<>(Credito.class)
            .addColumn("montoTotal", "Monto Total", 15)
            .addColumn("saldo", "Saldo", 15)
            .addColumn("numeroCuotas", "Nro. Cuotas", 12)
            .addColumn("fechaInicio", "Fecha Inicio", 12)
            .addColumn("estado", "Estado", 12);
    
    public static String obtenerTodosTable(List<Credito> creditos) {
        return tableMapper.obtenerTodosTable(creditos);
    }
    
    public static String obtenerUnoTable(Credito credito) {
        return tableMapper.obtenerUnoTable(credito);
    }
}