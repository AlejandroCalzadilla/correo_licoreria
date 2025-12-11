package org.bebidas.modules.usuarios.mappers;

import java.util.List;

import org.bebidas.core.util.TableMapper;
import org.bebidas.modules.usuarios.Usuario;

public class UsuarioMapper {
    
     private static final TableMapper<Usuario> tableMapper = new TableMapper<>(Usuario.class)
            .addColumn("nombre", "Nombre", 25)
            .addColumn("correo", "Correo", 30)
            .addColumn("estado", "Estado", 10);
    
    public static String obtenerTodosTable(List<Usuario> usuarios) {
        return tableMapper.obtenerTodosTable(usuarios);
    }
    
    public static String obtenerUnoTable(Usuario usuario) {
        return tableMapper.obtenerUnoTable(usuario);
    }
}