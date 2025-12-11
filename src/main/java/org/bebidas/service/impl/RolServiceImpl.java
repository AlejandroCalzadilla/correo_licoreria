package org.bebidas.service.impl;

import org.bebidas.dao.interfaces.RolDAO;
import org.bebidas.model.Rol;
import org.bebidas.service.interfaces.RolService;

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