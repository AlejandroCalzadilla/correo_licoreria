package org.bebidas.modules.usuarios.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.usuarios.services.interfaces.UsuarioService;
import org.bebidas.modules.usuarios.Usuario;
import org.bebidas.modules.usuarios.repositories.UsuarioDAO;

import java.util.Optional;


public class UsuarioServiceImpl extends GenericServiceImpl<Usuario, Long> implements UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioServiceImpl(UsuarioDAO usuarioDAO) {
        super(usuarioDAO);
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioDAO.findByCorreo(correo);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return usuarioDAO.existsByCorreo(correo);
    }
}
