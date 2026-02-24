package org.bebidas.modules.usuarios.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.usuarios.services.interfaces.RolService;
import org.bebidas.modules.usuarios.Rol;
import org.bebidas.modules.usuarios.repositories.RolDAO;

import java.util.List;
import java.util.Optional;

public class RolServiceImpl extends GenericServiceImpl<Rol, Long> implements RolService {

    private final RolDAO rolDAO;

    public RolServiceImpl(RolDAO rolDAO) {
        super(rolDAO);
        this.rolDAO = rolDAO;
    }

    @Override
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolDAO.buscarPorNombre(nombre);
    }

    @Override
    public List<Rol> buscarPorEstado(boolean activo) {
        return rolDAO.buscarPorEstado(activo);
    }

    @Override
    public List<Rol> buscarRolesConPermiso(String permiso) {
        return rolDAO.buscarRolesConPermiso(permiso);
    }
}