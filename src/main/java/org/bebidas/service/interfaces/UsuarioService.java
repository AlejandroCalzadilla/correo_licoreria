package org.bebidas.service.interfaces;

import org.bebidas.model.Usuario;

import java.util.Optional;

public interface UsuarioService extends GenericService<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
