package org.bebidas.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableMapper<T> {
    
    private final Class<T> clazz;
    private final Map<String, Integer> columnWidths;
    private final List<String> columnNames;
    
    public TableMapper(Class<T> clazz) {
        this.clazz = clazz;
        this.columnWidths = new LinkedHashMap<>();
        this.columnNames = new ArrayList<>();
    }
    
    /**
     * Configura una columna con su nombre de visualización y ancho
     */
    public TableMapper<T> addColumn(String fieldName, String displayName, int width) {
        columnNames.add(fieldName);
        columnWidths.put(fieldName, width);
        return this;
    }
    
    /**
     * Genera la tabla para una lista de items
     */
    public String obtenerTodosTable(List<T> items) {
        if (items == null || items.isEmpty()) {
            return "No hay datos para mostrar";
        }
        
        StringBuilder sb = new StringBuilder();
        
        // Línea superior
        sb.append("┌");
        for (int i = 0; i < columnNames.size(); i++) {
            String field = columnNames.get(i);
            sb.append("─".repeat(columnWidths.get(field)));
            if (i < columnNames.size() - 1) {
                sb.append("┬");
            }
        }
        sb.append("┐\n");
        
        // Encabezados
        sb.append("│");
        for (String field : columnNames) {
            int width = columnWidths.get(field);
            sb.append(String.format("%-" + width + "s│", capitalize(field)));
        }
        sb.append("\n");
        
        // Separador de encabezado
        sb.append("├");
        for (int i = 0; i < columnNames.size(); i++) {
            String field = columnNames.get(i);
            sb.append("─".repeat(columnWidths.get(field)));
            if (i < columnNames.size() - 1) {
                sb.append("┼");
            }
        }
        sb.append("┤\n");
        
        // Filas de datos
        for (int idx = 0; idx < items.size(); idx++) {
            T item = items.get(idx);
            
            // Obtener valores y envolverlos
            List<List<String>> wrappedColumns = new ArrayList<>();
            int maxLines = 1;
            
            for (String field : columnNames) {
                String value = getFieldValue(item, field);
                int width = columnWidths.get(field);
                List<String> lines = wrap(value, width);
                wrappedColumns.add(lines);
                maxLines = Math.max(maxLines, lines.size());
            }
            
            // Imprimir todas las líneas de esta fila
            for (int lineIdx = 0; lineIdx < maxLines; lineIdx++) {
                sb.append("│");
                for (int colIdx = 0; colIdx < columnNames.size(); colIdx++) {
                    String field = columnNames.get(colIdx);
                    int width = columnWidths.get(field);
                    List<String> lines = wrappedColumns.get(colIdx);
                    sb.append(getLine(lines, lineIdx, width)).append("│");
                }
                sb.append("\n");
            }
            
            // Línea divisoria entre registros (solo si no es el último)
            if (idx < items.size() - 1) {
                sb.append("├");
                for (int i = 0; i < columnNames.size(); i++) {
                    String field = columnNames.get(i);
                    sb.append("─".repeat(columnWidths.get(field)));
                    if (i < columnNames.size() - 1) {
                        sb.append("┼");
                    }
                }
                sb.append("┤\n");
            }
        }
        
        // Línea inferior
        sb.append("└");
        for (int i = 0; i < columnNames.size(); i++) {
            String field = columnNames.get(i);
            sb.append("─".repeat(columnWidths.get(field)));
            if (i < columnNames.size() - 1) {
                sb.append("┴");
            }
        }
        sb.append("┘\n");
        
        return sb.toString();
    }
    
    /**
     * Genera la tabla para un solo item
     */
    public String obtenerUnoTable(T item) {
        return obtenerTodosTable(List.of(item));
    }
    
    /**
     * Obtiene el valor de un campo usando reflexión
     */
    private String getFieldValue(T item, String fieldName) {
        try {
            // Intentar primero con getter
            String getterName = "get" + capitalize(fieldName);
            try {
                Method getter = clazz.getMethod(getterName);
                Object value = getter.invoke(item);
                return value != null ? value.toString() : "";
            } catch (NoSuchMethodException e) {
                // Si no hay getter, intentar acceso directo al campo
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(item);
                return value != null ? value.toString() : "";
            }
        } catch (Exception e) {
            // Si el campo no existe en la clase directamente, buscar en la superclase (BaseEntity)
            try {
                String getterName = "get" + capitalize(fieldName);
                Method getter = clazz.getSuperclass().getMethod(getterName);
                Object value = getter.invoke(item);
                return value != null ? value.toString() : "";
            } catch (Exception ex) {
                return "";
            }
        }
    }
    
    /**
     * Divide un texto en líneas según el ancho especificado
     */
    private List<String> wrap(String text, int width) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }
        
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + width, text.length());
            lines.add(text.substring(start, end));
            start = end;
        }
        return lines;
    }
    
    /**
     * Obtiene una línea específica de la lista, o espacios si no existe
     */
    private String getLine(List<String> lines, int index, int width) {
        if (index < lines.size()) {
            String line = lines.get(index);
            return String.format("%-" + width + "s", line);
        }
        return " ".repeat(width);
    }
    
    /**
     * Capitaliza la primera letra de un string
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
