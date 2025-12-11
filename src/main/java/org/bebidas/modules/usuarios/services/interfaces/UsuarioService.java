package org.bebidas.modules.usuarios.services.interfaces;

import java.util.Optional;

import org.bebidas.modules.service.interfaces.GenericService;
import org.bebidas.modules.usuarios.Usuario;

public interface UsuarioService extends GenericService<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
