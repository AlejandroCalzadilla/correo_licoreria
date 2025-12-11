package org.bebidas.modules.dao.interfaces;

import java.util.Optional;

import org.bebidas.modules.usuarios.Usuario;

public interface UsuarioDAO extends GenericDAO<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
