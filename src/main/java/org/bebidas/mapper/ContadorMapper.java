package org.bebidas.mapper;

import java.util.List;

import org.bebidas.model.Contador;
import org.bebidas.util.TableMapper;

public class ContadorMapper {
    
     private static final TableMapper<Contador> tableMapper = new TableMapper<>(Contador.class)
            .addColumn("tipo", "Tipo", 15)
            .addColumn("prefijo", "Prefijo", 10)
            .addColumn("valorActual", "Valor Actual", 15)
            .addColumn("longitud", "Longitud", 10)
            .addColumn("descripcion", "Descripción", 35);
    
    public static String obtenerTodosTable(List<Contador> contadores) {
        return tableMapper.obtenerTodosTable(contadores);
    }
    
    public static String obtenerUnoTable(Contador contador) {
        return tableMapper.obtenerUnoTable(contador);
    }
}