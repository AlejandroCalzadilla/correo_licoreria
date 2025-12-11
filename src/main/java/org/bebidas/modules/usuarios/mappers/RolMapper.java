package org.bebidas.modules.usuarios.mappers;

import java.util.List;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.usuarios.Rol;

public class RolMapper {
    
     private static final TableMapper<Rol> tableMapper = new TableMapper<>(Rol.class)
            .addColumn("nombre", "Nombre", 20)
            .addColumn("descripcion", "Descripción", 35)
            .addColumn("activo", "Activo", 8);
    
    public static String obtenerTodosTable(List<Rol> roles) {
        return tableMapper.obtenerTodosTable(roles);
    }
    
    public static String obtenerUnoTable(Rol rol) {
        return tableMapper.obtenerUnoTable(rol);
    }
}