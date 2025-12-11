package org.bebidas.dao.interfaces;

import org.bebidas.model.Usuario;

import java.util.Optional;

public interface UsuarioDAO extends GenericDAO<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
