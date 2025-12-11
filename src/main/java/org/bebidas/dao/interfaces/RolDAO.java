package org.bebidas.dao.interfaces;

import org.bebidas.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolDAO extends GenericDAO<Rol, Long> {
    Optional<Rol> buscarPorNombre(String nombre);
    List<Rol> buscarPorEstado(boolean activo);
    List<Rol> buscarRolesConPermiso(String permiso);
}
