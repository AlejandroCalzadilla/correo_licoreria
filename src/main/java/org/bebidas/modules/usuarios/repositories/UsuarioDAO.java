package org.bebidas.modules.usuarios.repositories;

import java.util.Optional;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.usuarios.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
